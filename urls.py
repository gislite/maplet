# -*- coding:utf-8 -*-
from torcms.core import router

from torcms.handlers.redirect_handler import RedirectHandler

from router import urls as info_urls

urls = info_urls + router.urls + [

    ("/(.*)", RedirectHandler, dict()),
]
