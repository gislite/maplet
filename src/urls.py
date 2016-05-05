
from torcms.core import router
# from torcms.handlers.info_handler import InfoHandler as AppHandler


from torcms.handlers.redirect_handler import RedirectHandler

from router import urls as info_urls
urls = router.urls + info_urls + [



    # ("/map/label_list/(.*)", AppLabellistHandler, dict()),
    # ("/map/label/(.*)", AppLabelHandler, dict()),

    ("/(.*)", RedirectHandler, dict()),
]
