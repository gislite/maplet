# -*- coding:utf-8 -*-

import json

import tornado.web

from torcms.core.base_handler import BaseHandler
from torcms.model.usage_model import MUsage
from torcms.core import tools
from maplet.model.json_model import MJson

class GeoJsonHandler(BaseHandler):
    def initialize(self):
        self.init()
        self.mjson = MJson()
        self.musage = MUsage()


    def get(self, url_str=''):
        url_arr = self.parse_url(url_str)

        if len(url_arr) == 0:
            self.index()

        elif url_str == 'draw':
            self.show_geojson('')

        # elif len(url_arr) == 1:
        #     rec = self.mjson.get_by_id(url_str)
        #     #  看一下得到的是什么鬼
        #     # print('=' * 50)
        #     # xx = json.dumps(rec.json, indent=1)
        #     # print(xx)
        #     # for xx in rec.json.keys():
        #     #     print(rec.json[xx])
        #     # print('=' * 50)
        #     # print(rec.json)
        #     # print('=' * 50)
        #     if rec:
        #         return json.dump(rec.json, self)
        #     else:
        #         return False
        elif url_arr[0] == 'list':
            self.list()
        elif len(url_arr) == 1 and len(url_str) == 4 :
            self.show_geojson(url_str)

        elif len(url_arr) == 2:
            if url_arr[0] == 'gson':
                rec = self.mjson.get_by_id(url_arr[1])

                # 这里主要是检查一下保存成了个什么鬼。结果还是差强人意。
                # print('=' * 50)
                # xx = json.dumps(rec.json, indent=1)
                # print(xx)
                # for xx in rec.json.keys():
                #     print(rec.json[xx])
                # print('=' * 50)
                # print(rec.json)
                # print('=' * 50)
                if rec:
                    return json.dump(rec.json, self)
                else:
                    return False

            elif url_arr[0] == 'download':
                self.download(url_arr[1])
            elif url_arr[0] == 'delete':
                self.delete(url_arr[1])
    def show_geojson(self, gid):
        kwd = {
            'pager': '',
            'url': self.request.uri,
            'geojson': gid,
            'tdesc': '',
            'login': 1 if self.get_current_user() else 0,

        }


        map_hist = []
        if self.get_secure_cookie('map_hist'):
            for xx in range(0, len(self.get_secure_cookie('map_hist').decode('utf-8')), 4):
                map_hist.append(self.get_secure_cookie('map_hist').decode('utf-8')[xx: xx + 4])
        self.render('tmpl_applite/app/full_screen_draw.html',
                    kwd=kwd,
                    userinfo=self.userinfo,
                    unescape=tornado.escape.xhtml_unescape,
                    recent_apps=self.musage.query_recent(self.get_current_user(), 6)[1:] if self.userinfo else [],

                    )

    def index(self):
        self.render('tmpl_applite/geojson/index.html',
                    kwd={},
                    userinfo=self.userinfo,
                    unescape=tornado.escape.xhtml_unescape,
                    json_arr = self.mjson.query_recent(self.userinfo.uid, 20) if self.userinfo else [],

                    )
    @tornado.web.authenticated
    def list(self):

        kwd = {}
        self.render('tmpl_applite/geojson/gson_recent.html',
                    kwd=kwd,
                    userinfo=self.userinfo,
                    unescape=tornado.escape.xhtml_unescape,
                    json_arr = self.mjson.query_recent(self.userinfo.uid, 10),
                    )

    @tornado.web.authenticated
    def delete(self, uid):
        self.mjson.delete_by_uid(uid)

    @tornado.web.authenticated
    def download(self, pa_str):

        uid = pa_str.split('_')[-1].split('.')[0]

        self.set_header('Content-Type', 'application/force-download')
        # self.set_header('Content-Disposition', 'attachment; filename=%s' % file_name)
        rec = self.mjson.get_by_id(uid)

        geojson = rec.json

        out_arr = []
        for key in geojson.keys():
            out_arr = out_arr + geojson[key]['features']

        out_dic = {"type": "FeatureCollection",
                   "features": out_arr}

        if rec:
            return json.dump(out_dic, self)

    def post(self, url_str=''):
        # print (url_str)
        # print('-' *20)
        url_arr = self.parse_url(url_str)

        if len(url_arr) <= 1:
            self.add_data(url_str)
        elif len(url_arr) == 2:
            if self.get_current_user():
                self.add_data_with_map(url_arr)
            else:
                self.set_status('403')
                return False
        else:
            self.set_status('403')
            return False



    @tornado.web.authenticated
    def add_data(self, gson_uid):
        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)

        geojson_str = post_data['geojson'][0]

        xx = json.loads(geojson_str)

        out_dic = {}
        index = 0

        for x in xx['features']:
            bcbc = x['geometry']
            if 'features' in bcbc:
                if bcbc['features'][0]['geometry']['coordinates'] in [[], [[None]]]:
                    continue
            else:
                if bcbc['coordinates'] in [[], [[None]]]:
                    continue

                bcbc = {'features': [{'geometry': bcbc,
                                      "properties": {},
                                      "type": "Feature"}],
                        'type': "FeatureCollection"}

            out_dic[index] = bcbc
            index = index + 1


        if gson_uid == 'draw' or gson_uid == '':
            uid = tools.get_uu4d()
            while self.mjson.get_by_id(uid):
                uid = tools.get_uu4d()
            return_dic = {'sig': uid}

        elif len(gson_uid) == 4:
            uid = gson_uid
            return_dic = {'sig': ''}

            cur_info = self.mjson.get_by_id(uid)

            if cur_info.user.uid ==  self.userinfo.uid:
                pass
            else:
                return_dic['status'] = 0
                return json.dump(return_dic, self)
        else:
            return

        try:
            self.mjson.add_json(uid, self.userinfo.uid,  out_dic)
            return_dic['status'] = 1
            return json.dump(return_dic, self)
        except:
            self.set_status(400)
            return False


    @tornado.web.authenticated
    def add_data_with_map(self, url_arr):

        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)

        geojson_str = post_data['geojson'][0]

        xx = json.loads(geojson_str)

        out_dic = {}
        index = 0

        for x in xx['features']:
            bcbc = x['geometry']
            if 'features' in bcbc:
                if bcbc['features'][0]['geometry']['coordinates'] in [[], [[None]]]:
                    continue
            else:
                if bcbc['coordinates'] in [[], [[None]]]:
                    continue

                bcbc = {'features': [{'geometry': bcbc,
                                      "properties": {},
                                      "type": "Feature"}],
                        'type': "FeatureCollection"}

            out_dic[index] = bcbc
            index = index + 1



        if len(url_arr[1]) == 4:
            uid = url_arr[1]
            return_dic = {'sig': ''}

            cur_info = self.mjson.get_by_id(uid)

            if cur_info.user.uid ==  self.userinfo.uid:
                pass
            else:
                return_dic['status'] = 0
                return json.dump(return_dic, self)
        else:
            uid = tools.get_uu4d()
            while self.mjson.get_by_id(uid):
                uid = tools.get_uu4d()
            return_dic = {'sig': uid}

        try:
            self.mjson.add_or_update(uid, self.userinfo.uid, url_arr[0], out_dic)
            return_dic['status'] = 1
            return json.dump(return_dic, self)
        except:
            self.set_status(400)
            return False
