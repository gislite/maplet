import json
import random

import tornado.escape
import tornado.web
from torcms.core import tools
from torcms.core.base_handler import BaseHandler

import config
from torcms.model.app2catalog_model import MApp2Catalog
from torcms.model.app2label_model import MApp2Label
from torcms.model.app_model import MApp
from torcms.model.app_rel_model import MAppRel
from torcms.model.app_reply_model import MApp2Reply
from torcms.model.evaluation_model import MEvaluation
from torcms.model.mappcatalog import MAppCatalog
from torcms.model.usage_model import MUsage



class MapHandler(BaseHandler):
    def initialize(self):
        self.init()
        self.mevaluation = MEvaluation()
        self.mapp2catalog = MApp2Catalog()
        self.mapp2tag = MApp2Label()
        self.mapp = MApp()
        self.musage = MUsage()
        self.mtag = MAppCatalog()
        self.mrel = MAppRel()
        self.mreply = MApp2Reply()


    def get(self, url_str=''):

        url_arr = self.parse_url(url_str)

        if url_arr[0] == 'to_add':
            self.user_to_add()
        elif url_arr[0] == 'catalog':
            self.catalog()
        elif len(url_arr) == 1 and len(url_str) == 4:
            self.show_app(url_str)
        elif len(url_arr) == 2:
            if url_arr[0] == 'edit':
                self.to_edit_app(url_arr[1])
            elif url_arr[0] == 'add':
                self.to_add_app(url_arr[1])
            else:
                '''
                从相关计算中过来的。
                '''
                self.mrel.update_relation(url_arr[1], url_arr[0])
                self.redirect('/{0}/{1}'.format(config.app_url_name, url_arr[0]))
        else:
            kwd = {
                'title': '',
                'info': '',
            }
            self.render('html/404.html', kwd=kwd,
                        userinfo=self.userinfo, )

    def post(self, url_str=''):

        url_arr = self.parse_url(url_str)

        if url_arr[0] == 'to_add':
            self.add()
        elif url_arr[0] == 'rel':
            if self.get_current_user():
                self.add_relation(url_arr[1])
            else:
                self.redirect('/user/login')
        elif url_arr[0] == 'comment_add':
            self.add_comment(url_arr[1])
        elif url_arr[0] == 'edit':
            self.update(url_arr[1])
        elif url_arr[0] == 'add':
            self.add(url_arr[1])
        else:
            return False

    def catalog(self):
        self.render('tmpl_applite/app/catalog.html',
                        userinfo=self.userinfo,
                        kwd={'uid': '',}
                        )


    @tornado.web.authenticated
    def user_to_add(self):
        self.render('tmpl_applite/app/add.html',
                        tag_infos=self.mtag.query_all(),
                        userinfo=self.userinfo,
                        kwd={'uid': '',}
                        )

    @tornado.web.authenticated
    def to_add_app(self, uid):
        if self.mapp.get_by_uid(uid):
            self.redirect('/map/edit/{0}'.format(uid))
        else:
            self.render('tmpl_applite/app/add.html',
                        tag_infos=self.mtag.query_all(),
                        userinfo=self.userinfo,
                        kwd={'uid': uid,}
                        )

    @tornado.web.authenticated
    def to_edit_app(self, app_id):
        if self.userinfo.privilege[4] == '1':
            info = self.mapp.get_by_uid(app_id)
            self.render('tmpl_applite/app/edit.html',
                        userinfo = self.userinfo,
                        app_info=info,
                        unescape=tornado.escape.xhtml_escape,
                        tag_infos=self.mtag.query_all(),
                        app2tag_info=self.mapp2catalog.query_by_app_uid(app_id),
                        app2label_info=self.mapp2tag.get_by_id(app_id),
                        )
        else:
            return False

    @tornado.web.authenticated
    def update(self, uid):
        if self.userinfo.privilege[4] == '1':
            post_data = {}
            for key in self.request.arguments:
                post_data[key] = self.get_arguments(key)
            self.mapp.modify_meta(uid, post_data)
            self.update_catalog(post_data['uid'][0])
            self.update_tag(post_data['uid'][0])
            self.redirect('/{0}/{1}'.format(config.app_url_name, post_data['uid'][0]))
        else:
            return False

    @tornado.web.authenticated
    def add(self, uid = ''):
        if self.userinfo.privilege[4] == '1':
            pass
        else:
            return False

        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)


        if uid == '':
            print('sadfasd')
            uid = tools.get_uu4d()
            while self.mapp.get_by_uid(uid):
                uid = tools.get_uu4d()
            post_data['uid'][0] = uid
        print(uid)

        self.mapp.modify_meta(uid, post_data)
        self.update_catalog( uid )
        self.update_tag( uid )
        self.redirect('/{0}/{1}'.format(config.app_url_name, uid ))


    @tornado.web.authenticated
    def update_tag(self, signature):
        if self.userinfo.privilege[4] == '1':
            pass
        else:
            return False
        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)
        current_tag_infos = self.mapp2tag.get_by_id(signature)

        tags_arr = [x.strip() for x in post_data['tags'][0].split(',')]
        for tag_name in tags_arr:
            if tag_name == '':
                pass
            else:
                self.mapp2tag.add_record(signature, tag_name, 1)

        for cur_info in current_tag_infos:
            if cur_info.tag.name in tags_arr:
                pass
            else:
                self.mapp2tag.remove_relation(signature, cur_info.tag)

    @tornado.web.authenticated
    def update_catalog(self, signature):
        if self.userinfo.privilege[4] == '1':
            pass
        else:
            return False
        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)
        current_catalog_infos = self.mapp2catalog.query_by_app_uid(signature)

        new_tag_arr = []
        for key in ['cat_1', 'cat_2', 'cat_3', 'cat_4', 'cat_5']:
            if post_data[key][0] == '':
                pass
            else:
                new_tag_arr.append(post_data[key][0])
                self.mapp2catalog.add_record(signature, post_data[key][0], int(key[-1]))
        for cur_info in current_catalog_infos:
            if str(cur_info.catalog.uid).strip() in new_tag_arr:
                pass
            else:
                self.mapp2catalog.remove_relation(signature, cur_info.catalog)

    @tornado.web.authenticated
    def add_comment(self, id_post):
        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)
        post_data['user_id'] = self.userinfo.uid
        post_data['user_name'] = self.userinfo.user_name
        comment_uid = self.mreply.insert_data(post_data, id_post)
        if comment_uid:
            output = {
                'pinglun': comment_uid,
            }
        else:
            output = {
                'pinglun': 0,
            }
        return json.dump(output, self)

    def show_app(self, app_id):
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
        rec = self.mapp.get_by_uid(app_id)

        if rec == False:
            kwd = {
                'info': '您要找的云算应用不存在。',
            }
            self.render('html/404.html', kwd=kwd,
                        userinfo = self.userinfo,)
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

        if last_map_id and self.mapp.get_by_uid(last_map_id):
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

        self.mapp.view_count_increase(app_id)

        if self.get_current_user():
            self.musage.add_or_update(self.userinfo.uid, app_id)

            # json_recs = self.mjson.query_by_app(app_id, self.userinfo.uid)
            # layout_recs = self.mlayout.query_by_app(app_id, self.userinfo.uid)
            #
            # layout_links = []
            #
            # for layout_rec in layout_recs:
            #     out_link = '{0}?zoom={1}&lat={2}&lon={3}'.format(layout_rec.app.uid, layout_rec.zoom, layout_rec.lat,
            #                                                      layout_rec.lon)
            #     if layout_rec.marker != 0:
            #         out_link = out_link + '&marker=1'
            #     if layout_rec.json != '':
            #         out_link = out_link + '&gson={0}'.format(layout_rec.json)
            #     layout_links.append({'uid': layout_rec.uid, 'link': out_link})


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

        rand_recs = self.mapp.query_random(4 - rel_recs.count() + 2)

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
                    # json_recs=json_recs,
                    # layout_links=layout_links,
                    replys=replys,
                    )

    def add_relation(self, f_uid, t_uid):
        if False == self.mapp.get_by_uid(t_uid):
            return False
        if f_uid == t_uid:
            '''
            关联其本身
            '''
            return False
        self.mrel.add_relation(f_uid, t_uid, 2)
        self.mrel.add_relation(t_uid, f_uid, 1)
        return True
