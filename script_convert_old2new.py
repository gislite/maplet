import os
import sys
import json
import yaml
import tornado.escape

from bs4 import BeautifulSoup

pwd = os.getcwd()
(qian, hou) = os.path.split(pwd)
sys.path.append(qian)

from torcms.model.app_model import MApp
from model.tabapp_model import *
from torcms.core import tools

mequ = MApp()
mwetland = MWetland()
myunsuan = MYunsuan()
mmap = MMaplet()
mmapfig = MMapfig()



def do_wetland():

    recs_wetland = mwetland.query_all()
    for rec in recs_wetland:
        new_id  = tools.get_uu4d()
        while mequ.get_by_uid(new_id):
            new_id = tools.get_uu4d()
        js_dic = {
            'sig': new_id,
            'title': rec.title ,
            'desc': rec.desc,
            # 'type': 1,
            # 'html_path': tornado.escape.xhtml_escape(os.path.splitext(html_path)[0]),
            'cnt_md': rec.cnt_md,
            'cnt_html': rec.cnt_html,
        }
        ext_dic = rec.extinfo
        ext_dic['def_cat_uid'] = "0301"
        mequ.addata_init(js_dic, ext_dic)
    return True

def do_yunsuan():
    recs_wetland = myunsuan.query_all()
    for rec in recs_wetland:
        new_id = tools.get_uu4d()
        while mequ.get_by_uid(new_id):
            new_id = tools.get_uu4d()
        js_dic = {
            'sig': new_id,
            'title': rec.title,
            'desc': rec.desc,
            # 'type': 1,
            # 'html_path': tornado.escape.xhtml_escape(os.path.splitext(html_path)[0]),
            'cnt_md': rec.cnt_md,
            'cnt_html': rec.cnt_html,
        }
        ext_dic = {
            'def_uid': rec.uid,
            'def_from': 'maplet',
            'def_cat_uid': '2501',
            'def_html_path': rec.html_path
        }
        mequ.addata_init(js_dic, ext_dic)


    return True

def do_map():

    recs_wetland = mmap.query_all()
    for rec in recs_wetland:
        new_id = tools.get_uu4d()
        while mequ.get_by_uid(new_id):
            new_id = tools.get_uu4d()
        js_dic = {
            'sig': rec.uid,
            'title': rec.title ,
            'desc': rec.desc,
            # 'type': 1,
            # 'html_path': tornado.escape.xhtml_escape(os.path.splitext(html_path)[0]),
            'cnt_md': rec.cnt_md,
            'cnt_html': rec.cnt_html,
        }
        ext_dic = {
            'def_uid': rec.uid,
            'def_cat_uid': '0901',
            'def_from': 'maplet',
            'ext_lon': rec.lon,
            'ext_lat': rec.lat,
            'ext_zoom_max': rec.zoom_max,
            'ext_zoom_min' : rec.zoom_min,
            'ext_zoom_current': rec.zoom_current
        }


        mequ.addata_init(js_dic, ext_dic)


    return True

def do_mapfig():

    recs_wetland = mmapfig.query_all()
    for rec in recs_wetland:
        new_id = tools.get_uu4d()
        while mequ.get_by_uid(new_id):
            new_id = tools.get_uu4d()
        js_dic = {
            'sig': new_id,
            'title': rec.title ,
            'desc': rec.desc,
            # 'type': 1,
            # 'html_path': tornado.escape.xhtml_escape(os.path.splitext(html_path)[0]),
            'cnt_md': rec.cnt_md,
            'cnt_html': rec.cnt_html,
        }
        ext_dic = {
            'def_uid': rec.uid,
            'def_cat_uid': '0501',
            'ext_yaml': rec.yaml,
            'def_json': json.dumps(yaml.load(rec.yaml)),
        }


        mequ.addata_init(js_dic, ext_dic)


if __name__ == '__main__':
    # javascript_ws = './templates/ware_app'
    # for wroot, wdirs, wfiles in os.walk(javascript_ws):


    # do_wetland()
    # do_yunsuan()
    do_map()
    # do_mapfig()
