# -*- coding:utf-8 -*-


from torcms_maplet.handlers.geojson import GeoJsonHandler, GeoJsonAjaxHandler
from torcms_maplet.handlers.map_handler import MapPostHandler, MapLayoutHandler, MapOverlayHandler, \
    MapAdminHandler

maplet_urls = [
    ('/overlay/(.*)', MapOverlayHandler, dict()),
    ('/map/overlay/(.*)', MapOverlayHandler, dict()),  # Deprecated, repaled by `/overlay/` .

    ('/admin_map/(.*)', MapAdminHandler, dict()),
    ("/map/(.*)", MapPostHandler, dict(kind='m')),

    ('/geojson/(.*)', GeoJsonHandler, dict()),
    ('/geojson_j/(.*)', GeoJsonAjaxHandler, dict()),
    ('/layout/(.*)', MapLayoutHandler, dict()),

]
