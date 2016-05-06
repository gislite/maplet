# coding:utf-8

import os
import sys
from torcms.modules.modef import core_modules
from maplet.modules.map_modules import *
from config import cfg
from urls import urls

cur_modues = {'app_layout': app_layout,
              'app_json': app_json,
              }

modules = dict(core_modules, **cur_modues)

SETTINGS = {
    "template_path": os.path.join(os.path.dirname(__file__), "templates"),
    "static_path": os.path.join(os.path.dirname(__file__), "static"),
    'debug': cfg['DEBUG'],
    "cookie_secret": config.cookie_secret,
    "login_url": "/user/login",
    'ui_modules': modules,
}

PORT = '8246'

if __name__ == "__main__":
    tornado.locale.set_default_locale('zh_CN')
    tornado.locale.load_gettext_translations('locale', 'yunsuan')
    if len(sys.argv) > 1:
        PORT = sys.argv[1]

    application = tornado.web.Application(
        handlers=urls,
        **SETTINGS
    )
    application.listen(PORT)
    print ('Development server is running at http://127.0.0.1:{0}/'.format(PORT))
    print ('Quit the server with CONTROL-C')
    tornado.ioloop.IOLoop.instance().start()
