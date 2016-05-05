# -*- coding:utf-8 -*-


# from torcms.handlers.info_handler import InfoHandler as AppHandler

from torcms.handlers.index import IndexHandler as  AppIndexHandler
from torcms.handlers.user_info_list_handler import UserListHandler
from torcms.handlers.tag_hanlder import TagHandler
# from torcms.handlers.label_hander import AppLabelHandler
# from torcms.handlers.labellist_hander import AppLabellistHandler
from torcms.handlers.collect_handler import CollectHandler
from torcms.handlers.evaluation_handler import EvaluationHandler
from torcms.handlers.post_info_relation_handler import RelHandler
from torcms.handlers.app2reply_handler import App2ReplyHandler
from torcms.handlers.redirect_handler import RedirectHandler
from maplet.handlers.overlay_handler import MapOverlayHandler

from maplet.handlers.map_handler import MapHandler
from maplet.handlers.geojson import GeoJsonHandler
from maplet.handlers.layout_handler import LayoutHandler

urls = [
    ('/map/overlay/(.*)', MapOverlayHandler, dict()),
    ('/map/', AppIndexHandler, dict()),

    ('/map/toreply/(.*)', App2ReplyHandler, dict()),
    ('/map/(.*)', MapHandler, dict()),
    ('/geojson/(.*)', GeoJsonHandler, dict()),
    ('/layout/(.*)', LayoutHandler, dict()),

]
