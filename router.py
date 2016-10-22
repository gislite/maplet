# -*- coding:utf-8 -*-

from extor.handlers.overlay_handler import MapOverlayHandler
from extor.handlers.map_handler import MapHandler
from extor.handlers.geojson import GeoJsonHandler
from extor.handlers.layout_handler import LayoutHandler
from torcms.handlers.infolabel_hander import InfoLabelHandler
from extor.handlers.map_handler import InforRedirectHandler
from extor.handlers.meta_ext_handler import MetaExtHnadler
urls = [
    ('/meta/(.*)', MetaExtHnadler, dict()),
    ('/info/(.*)', InforRedirectHandler, dict()),
    ('/map/overlay/(.*)', MapOverlayHandler, dict()),
    ('/map/(.*)', MapHandler, dict()),
    ("/list_label/(.*)", InfoLabelHandler, dict()),
    ('/geojson/(.*)', GeoJsonHandler, dict()),
    ('/layout/(.*)', LayoutHandler, dict()),
]
