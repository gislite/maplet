# coding:utf-8

import tornado.escape
import tornado.web

import config
from torcms.core.base_handler import BaseHandler
from torcms.applite.model.app_model import MApp


def average(num_arr):
    return sum(num_arr) / len(num_arr)

class MapOverlayHandler(BaseHandler):
    def initialize(self):
        self.init()
        self.mapplication = MApp()


    def get(self, url_str=''):
        if len(url_str) > 0:
            url_arr = url_str.split('/')
        else:
            url_arr = []
        if len(url_arr) > 1:
            self.show_overlay(url_arr)
        else:
            kwd = {
                'title': '',
                'info': '',
            }
            self.render('html/404.html', kwd=kwd,
                        userinfo = self.userinfo,)

    def show_overlay(self, app_arr):
        '''
        打开App.
        '''
        app_info_arr = []
        lon_arr = []
        lat_arr = []
        zoom_max_arr = []
        zoom_min_arr = []
        zoom_current_zrr = []

        # self.set_secure_cookie('over_kay', app_arr)

        for app_rr in app_arr:
            c_ap = self.mapplication.get_by_uid(app_rr)
            app_info_arr.append(c_ap)
            lon_arr.append(c_ap.lon)
            lat_arr.append(c_ap.lat)
            zoom_max_arr.append(c_ap.zoom_max)
            zoom_min_arr.append(c_ap.zoom_min)
            zoom_current_zrr.append(c_ap.zoom_current)

        kwd = {'url': 1,
               "cookie_str": '',
               'lon': average(lon_arr),
               'lat': average(lat_arr),
               'zoom_max': max(zoom_max_arr),
               'zoom_min': min(zoom_min_arr),
               'zoom_current': int(average(zoom_current_zrr)),
               }
        if 'fullscreen' in self.request.arguments:
            tmpl = 'tmpl_applite/overlay/overlay_full.html'
        else:
            tmpl = 'tmpl_applite/overlay/overlay.html'
        self.render(tmpl,
                    topmenu='',
                    kwd=kwd,
                    userinfo=self.userinfo,
                    unescape=tornado.escape.xhtml_unescape,
                    app_arr=app_info_arr,
                    app_str='/'.join(app_arr),
                    wcs_svr=config.wcs_svr,
                    )
