# -*- coding:utf-8 -*-
from torcms.core import router

from torcms.handlers.infolabel_hander import InfoLabelHandler
from torcms.handlers.redirect_handler import RedirectHandler

from router import urls as info_urls

urls = router.urls + info_urls + [

    # ("/map/label_list/(.*)", AppLabellistHandler, dict()),
    ("/list_label/(.*)", InfoLabelHandler, dict()),

    ("/(.*)", RedirectHandler, dict()),
]
