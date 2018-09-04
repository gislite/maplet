# -*- coding:utf-8 -*-


import os
import tornado.web
from torcms.modules.modef import core_modules
import torcms.core.uifunction as uifuncs
# from extor.modules.map_modules import *
from torcms_maplet.modules.modef import maplet_modules
from config import SITE_CFG
from urls import urls
from torcms.modules.modef import core_modules as cmodules
from extor.modules.extends import Ext_category_menu,Ext_category_list

# cur_modues = {'app_layout': app_layout,
#               'app_json': app_json,
#               }


# Above Python 3.5.
cmodules = {**core_modules, **maplet_modules}

cmodules['ext_category_menu'] = Ext_category_menu
cmodules['ext_category_list'] = Ext_category_list

SETTINGS = {
    "template_path": os.path.join(os.path.dirname(__file__), "templates"),
    "static_path": os.path.join(os.path.dirname(__file__), "static"),
    'debug': SITE_CFG['DEBUG'],
    "cookie_secret": SITE_CFG['cookie_secret'],
    "login_url": "/user/login",
    'ui_modules': cmodules,
    'ui_methods': uifuncs,
}

app = tornado.web.Application(
    handlers=urls,
    **SETTINGS
)