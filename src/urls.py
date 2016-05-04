from torcms.applite.handlers.app_handler import AppHandler
from torcms.applite.handlers.index_app import AppIndexHandler
from torcms.applite.handlers.user_list_handler import UserListHandler

from torcms.core import router

# from applite.handlers.javascript import JavascriptHandler
from torcms.applite.handlers.tag_hanlder import TagHandler
from torcms.applite.handlers.label_hander import AppLabelHandler
from torcms.applite.handlers.labellist_hander import AppLabellistHandler

from torcms.applite.handlers.collect_handler import CollectHandler
from torcms.applite.handlers.evaluation_handler import EvaluationHandler
from torcms.applite.handlers.rel_handler import RelHandler
from torcms.applite.handlers.app2reply_handler import App2ReplyHandler
from torcms.applite.handlers.redirect_handler import RedirectHandler
from maplet.handlers.overlay_handler import MapOverlayHandler

from maplet.handlers.geojson import GeoJsonHandler
from maplet.handlers.layout_handler import LayoutHandler
from router import urls as info_urls
urls = router.urls + info_urls + [
    ("/collect/(.*)", CollectHandler, dict()),
    ('/map/overlay/(.*)', MapOverlayHandler, dict()),
    ('/rel/(.*)', RelHandler, dict()),
    ("/user_list/(.*)", UserListHandler, dict()),
    ("/tag/(.*)", TagHandler, dict()),

    ("/map/label_list/(.*)", AppLabellistHandler, dict()),
    ("/map/label/(.*)", AppLabelHandler, dict()),

    ("/evaluate/(.*)", EvaluationHandler, dict()),
    ('/map/', AppIndexHandler, dict()),

    ('/map/toreply/(.*)', App2ReplyHandler, dict()),
    ('/map/(.*)', AppHandler, dict()),
    ('/geojson/(.*)', GeoJsonHandler, dict()),
    ('/layout/(.*)', LayoutHandler, dict()),
    ("/(.*)", RedirectHandler, dict()),
]
