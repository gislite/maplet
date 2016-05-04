# coding:utf-8

import os
import sys

from torcms.torlite.modules.modef import core_modules as modules
from torcms.claslite.module.modules import *
from urls import urls

cur_modues = {'Topline': ToplineModule,
              'Banner': BannerModule,
              'BreadCrumb': BreadCrumb,
              'ContactInfo': ContactInfo,
              'BreadcrumbPublish': BreadcrumbPublish,
              'ImgSlide': ImgSlide,
              'user_info': UserInfo,
              'vip_info': VipInfo,

              }

from torcms.applite.modules.extends import *
from maplet.modules.map_modules import *

modules['rel_post2app'] = rel_post2app
modules['rel_app2post'] = rel_app2post
modules['app_most_used'] = app_most_used
modules['app_random_choose'] = app_random_choose
modules['app_tags'] = app_tags
modules['app_menu'] = app_menu
modules['app_user_recent'] = app_user_recent
modules['app_user_most'] = app_user_most
modules['app_recent_used'] = app_recent_used
modules['label_count'] = label_count


modules['amazon_ad'] = amazon_ad
modules['baidu_search'] = baidu_search
modules['site_ad'] = site_ad
modules['widget_search'] = widget_search
modules['app_most_used_by_cat'] = app_most_used_by_cat
modules['app_least_used_by_cat'] = app_least_use_by_cat
modules['app_user_recent_by_cat'] = app_user_recent_by_cat

modules['app_layout'] = app_layout
modules['app_json'] = app_json
modules['app_catalog_of'] = app_catalog_of

modules = dict(modules, **cur_modues)

SETTINGS = {
    "template_path": os.path.join(os.path.dirname(__file__), "templates"),
    "static_path": os.path.join(os.path.dirname(__file__), "static"),
    'debug': True,
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
    # 此处为解决手机站的问题
    if PORT[1] == '1':
        # 形如：  8188, 8199
        SETTINGS['template_path'] = os.path.join(os.path.dirname(__file__), "templates_m")


    application = tornado.web.Application(
        handlers=urls,
        **SETTINGS
    )
    application.listen(PORT)
    print ('Development server is running at http://127.0.0.1:{0}/'.format(PORT))
    print ('Quit the server with CONTROL-C')
    tornado.ioloop.IOLoop.instance().start()
