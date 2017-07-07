u2 = ->
  osm = new (L.TileLayer)('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png')
#  mywms = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
#    layers: 'sheng'
#    format: 'image/png'
#    transparent: true
#    srs: 'EPSG:3857'
#    attribution: '中国')
  mywms = L.tileLayer.wms('http://121.42.45.218:8011/service?',
    layers: 'osm'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '中国')

  # mywms = L.tileLayer('http://121.42.45.218:8011/tms/1.0.0/osm/webmercator/{z}/{x}/{y}.png');

  xian = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'xian'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '分县')
  chart = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'chart'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '柱状图')
  river = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'river'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: 'myattribution')
  jilin = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/shihang.map',
    layers: 'sheng'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: 'myattribution')
  shucai = L.tileLayer.wms('http://g.osgeo.cn/cgi-bin/mapserv?map=/opt/mapws/mapfile/shihang.map',
    layers: 'shucai'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '开放地理空间实验室')
  # mywms.addTo(map);
  # var map = L.map('map').setView([35, 115], 8);
  map = new (L.Map)('map',
    center: new (L.LatLng)(38, 105)
    zoom: 3
    layers: [      osm,      mywms    ])
  baseMaps = 'OpenStreetMap': osm
  overlayMaps =
    '中国': mywms
    '分县': xian
    '河流': river
    '吉林省': jilin
    # '蔬菜': shucai
    '柱状图': chart
  map.addControl new (L.Control.Layers)(baseMaps, overlayMaps), {}
  return

$(document).ready ->
  u2()
  return
