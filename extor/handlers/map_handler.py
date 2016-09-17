# -*- coding:utf-8 -*-
import random

import tornado.escape
import tornado.web
from torcms.core import tools

import config
from torcms.core.base_handler import BaseHandler
from torcms.handlers.info_handler import InfoHandler


class InforRedirectHandler(BaseHandler):
    def get(self, url_str):
        self.redirect('/map/{0}'.format(url_str))

class MapHandler(InfoHandler):

    def extra_kwd(self, info_rec):

        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)

        out_dic =  {
            'marker': 1 if 'marker' in post_data else 0,
            'geojson': post_data['gson'][0] if 'gson' in post_data else '',
            'map_hist_arr': self.extra_view(info_rec.uid),

        }
        if 'zoom' in post_data :
            out_dic['vzoom'] = post_data['zoom'][0]
        if 'lat' in post_data :
            out_dic['vlat'] = post_data['lat'][0]
        if 'lon' in post_data :
            out_dic['vlon'] = post_data['lon'][0]
        return out_dic


    def extra_view(self, app_id):
        qian = self.get_secure_cookie('map_hist')
        if qian:
            qian = qian.decode('utf-8')
        else:
            qian = ''
        self.set_secure_cookie('map_hist', (app_id + qian)[:20])
        map_hist = []
        if self.get_secure_cookie('map_hist'):
            for xx in range(0, len(self.get_secure_cookie('map_hist').decode('utf-8')), 4):
                map_hist.append(self.get_secure_cookie('map_hist').decode('utf-8')[xx: xx + 4])
        return map_hist

    def get_tmpl_name(self, rec):
        if 'fullscreen' in self.request.arguments:
            tmpl = 'infor/app/full_screen.html'
        else:

            tmpl = 'infor/app/show_map.html'
        return tmpl
