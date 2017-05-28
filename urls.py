# -*- coding:utf-8 -*-

from torcms.core import router
from torcms.handlers.static_handler import StaticHandler
from router import urls as info_urls
from torcms_maplet.core.router import maplet_urls

urls = info_urls + maplet_urls + router.urls + [

    ("/(.*)", StaticHandler, dict()),
]
