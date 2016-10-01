# coding:utf-8

import tornado.web

from extor.model.layout_model import MLayout
from torcms.model.info_model import  MInfor as MApp
from torcms.core.base_handler import BaseHandler


class LayoutHandler(BaseHandler):
    def initialize(self):
        self.init()
        self.mequa = MApp()
        self.mlayout = MLayout()


    def get(self, url_str):

        if len(url_str) > 0:
            url_arr = url_str.split('/')

        if len(url_arr) == 2 :
            if url_arr[0] == 'delete':
                self.delete(url_arr[1])
    @tornado.web.authenticated
    def delete(self, uid):
        self.mlayout.delete_by_uid(uid)


    def post(self, url_str = ''):
        if url_str == 'save':
            self.save_layout()

    @tornado.web.authenticated
    def save_layout(self):
        post_data = {}
        for key in self.request.arguments:
            post_data[key] = self.get_arguments(key)
        if 'zoom' in post_data:
            pass
        else:
            self.set_status(403)
            return
        post_data['user'] = self.userinfo.uid
        self.mlayout.add_or_update(post_data)


