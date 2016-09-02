# -*- coding:utf-8 -*-

# Todo: shouldn't be here.
from torcms.handlers.info2reply_handler import  Info2ReplyHandler as App2ReplyHandler
from maplet.handlers.overlay_handler import MapOverlayHandler
from maplet.handlers.map_handler import MapHandler
from maplet.handlers.geojson import GeoJsonHandler
from maplet.handlers.layout_handler import LayoutHandler


urls = [
    ('/map/overlay/(.*)', MapOverlayHandler, dict()),
    ('/map/toreply/(.*)', App2ReplyHandler, dict()),
    ('/map/(.*)', MapHandler, dict()),
    ('/geojson/(.*)', GeoJsonHandler, dict()),
    ('/layout/(.*)', LayoutHandler, dict()),
]
