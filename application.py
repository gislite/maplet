# -*- coding:utf-8 -*-


import os
import tornado.web
from torcms.modules.modef import core_modules
# from extor.modules.map_modules import *
from config import SITE_CFG
from urls import urls

# cur_modues = {'app_layout': app_layout,
#               'app_json': app_json,
#               }

# modules = dict(core_modules, **cur_modues)

SETTINGS = {
    "template_path": os.path.join(os.path.dirname(__file__), "templates"),
    "static_path": os.path.join(os.path.dirname(__file__), "static"),
    'debug': SITE_CFG['DEBUG'],
    "cookie_secret": SITE_CFG['cookie_secret'],
    "login_url": "/user/login",
    'ui_modules': core_modules,
}

app = tornado.web.Application(
    handlers=urls,
    **SETTINGS
)