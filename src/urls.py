
from torcms.core import router
# from torcms.handlers.info_handler import InfoHandler as AppHandler

from torcms.applite.handlers.label_hander import AppLabelHandler
from torcms.handlers.redirect_handler import RedirectHandler

from router import urls as info_urls
urls = router.urls + info_urls + [



    # ("/map/label_list/(.*)", AppLabellistHandler, dict()),
     ("/list_label/(.*)", AppLabelHandler, dict()),

    ("/(.*)", RedirectHandler, dict()),
]
