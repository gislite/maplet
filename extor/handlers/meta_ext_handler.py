# -*- coding:utf-8 -*-
import tornado.web
from torcms.handlers.meta_handler import MetaHandler





class MetaExtHnadler(MetaHandler):
    def __fix_lon(self, lonval):
        '''
        Convert the longtude in (-180, 180)
        :return:
        '''
        return int(((lonval + 180) % 360 - 180) * 1000) / 1000

    @tornado.web.authenticated
    def extra_data(self, ext_dic, post_data):
        '''
        The additional information.
        :param post_data:
        :return: directory.
        '''
        lon = float(ext_dic['ext_lon'])
        ext_dic['ext_lon'] = str(self.__fix_lon(lon))
        return ext_dic
