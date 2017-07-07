u2 = ->
  osm = new (L.TileLayer)('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png')
  mywms = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'sheng'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '中国')
  xian = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'xian'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '分县')
  chart = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'chart'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: '柱状图')
  river = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/sheng.map',
    layers: 'river'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: 'myattribution')
  jilin = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/shihang.map',
    layers: 'sheng'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: 'myattribution')
  landuse = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/shihang.map',
    layers: 'landuse'
    format: 'image/png'
    transparent: true
    srs: 'EPSG:3857'
    attribution: 'landuse')

  shucai = L.tileLayer.wms('http://192.168.4.166/cgi-bin/mapserv?map=/opt/mapws/mapfile/shihang.map',
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
    '土地利用': landuse
    '柱状图': chart
  map.addControl new (L.Control.Layers)(baseMaps, overlayMaps), {}
  return

$(document).ready ->
  u2()
  return
