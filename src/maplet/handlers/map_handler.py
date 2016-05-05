# -*- coding:utf-8 -*-
import random

import tornado.escape
import tornado.web
from torcms.core import tools

import config
from torcms.handlers.info_handler import InfoHandler


class MapHandler(InfoHandler):
    def view_info(self, app_id):
        qian = self.get_secure_cookie('map_hist')

        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)

        if qian:
            qian = qian.decode('utf-8')
        else:
            qian = ''
        self.set_secure_cookie('map_hist', (app_id + qian)[:20])
        replys = self.mreply.get_by_id(app_id)
        rec = self.minfo.get_by_uid(app_id)

        if rec == False:
            kwd = {
                'info': '您要找的云算应用不存在。',
            }
            self.render('html/404.html', kwd=kwd,
                        userinfo=self.userinfo, )
            return False

        if 'zoom' in post_data:
            rec.zoom_current = post_data['zoom'][0]
        if 'lat' in post_data:
            rec.lat = post_data['lat'][0]
        if 'lon' in post_data:
            rec.lon = post_data['lon'][0]

        if 'lng' in post_data:
            rec.lon = post_data['lng'][0]

        last_map_id = self.get_secure_cookie('use_app_uid')

        if last_map_id:
            last_map_id = last_map_id.decode('utf-8')

        self.set_secure_cookie('use_app_uid', app_id)

        if last_map_id and self.minfo.get_by_uid(last_map_id):
            self.add_relation(last_map_id, app_id)

        cookie_str = tools.get_uuid()
        kwd = {
            'pager': '',
            'url': self.request.uri,
            'cookie_str': cookie_str,
            'marker': 1 if 'marker' in post_data  else 0,
            'geojson': post_data['gson'][0] if 'gson' in post_data else '',
            'signature': app_id,
            'tdesc': '',
            'eval_0': self.mevaluation.app_evaluation_count(app_id, 0),
            'eval_1': self.mevaluation.app_evaluation_count(app_id, 1),
            'site_url': config.site_url,
            'login': 1 if self.get_current_user() else 0,

        }

        self.minfo.view_count_increase(app_id)

        if self.get_current_user():
            self.musage.add_or_update(self.userinfo.uid, app_id)

        self.set_cookie('user_pass', cookie_str)

        map_hist = []
        if self.get_secure_cookie('map_hist'):
            for xx in range(0, len(self.get_secure_cookie('map_hist').decode('utf-8')), 4):
                map_hist.append(self.get_secure_cookie('map_hist').decode('utf-8')[xx: xx + 4])

        if 'fullscreen' in self.request.arguments:
            tmpl = 'tmpl_applite/app/full_screen.html'
            # if self.userinfo.privilege[4] == '1':
            #     tmpl = 'tmpl_applite/app/full_vip.html'
            # else:
            #     tmpl = 'tmpl_applite/app/full_screen.html'
        else:

            tmpl = 'tmpl_applite/app/show_map.html'

        rel_recs = self.mrel.get_app_relations(rec.uid, 4)

        rand_recs = self.minfo.query_random(4 - rel_recs.count() + 2)

        self.render(tmpl,
                    kwd=kwd,
                    calc_info=rec,
                    userinfo=self.userinfo,
                    relations=rel_recs,
                    rand_recs=rand_recs,
                    unescape=tornado.escape.xhtml_unescape,
                    ad_switch=random.randint(1, 18),
                    tag_info=self.mapp2tag.get_by_id(app_id),
                    recent_apps=self.musage.query_recent(self.get_current_user(), 6)[1:],
                    map_hist=map_hist,

                    replys=replys,
                    )
