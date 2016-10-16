reply_zan = (sig, reply_id, id_num) ->
  id_num = id_num.toString()
  zans = $("#text_zan").val()
  AjaxUrl = "/" + sig + "/toreply/zan/" + reply_id
  $.getJSON AjaxUrl, (Json) ->
    # window.location.href = '/user/login';
    $("#text_zan_" + id_num).html Json.text_zan  unless Json.text_zan is 0

reply_del = (sig, reply_id, id_num) ->
  id_num = id_num.toString()
  AjaxUrl = "/" + sig + "/toreply/delete_reply/" + reply_id
  $.getJSON AjaxUrl, (Json) ->
    $("#del_zan_" + id_num).html ""

reply_it = (sig, view_id) ->
  txt = $("#cnt_md").val()
  return  if txt.length < 30
  $.post "/" + sig + "/toreply/add/" + view_id,
    cnt_md: txt
  , (result) ->
    msg_json = $.parseJSON(result)
    $("#pinglun").load "/reply/get/" + msg_json.pinglun

  $("#cnt_md").val ""
  $("#cnt_md").attr "disabled", true
  $("#btn_submit_reply").attr "disabled", true

# $('#reply_form').hide();

del_layout = (layout_id) ->
  $.ajax
    url: "/layout/delete/" + layout_id
    type: "GET"
    cache: false
    data: {}
    dataType: "html"
    timeout: 1000
    error: ->
      alert "删除失败！"

    success: (result) ->
      alert "删除成功！暂请手动刷新页面！"

del_geojson = (gson_id) ->
  $.ajax
    url: "/geojson/delete/" + gson_id
    type: "GET"
    cache: false
    data: {}
    dataType: "html"
    timeout: 1000
    error: ->
      alert "删除失败！"

    success: (result) ->
      alert "删除成功，暂请手动刷新页面！"


$(document).ready ->
  $("#act_collect").click ->
    $.ajax
      url: "/collect/" + map_uid
      type: "GET"
      cache: false
      data: {}
      dataType: "html"
      timeout: 1000
      error: ->
        alert "请登陆后进行收藏！"

      success: (result) ->
        $("#text_collect").text "成功收藏"
        $("#text_collect").css "color", "red"


  $("#act_eval0").click ->
    $.ajax
      url: "/evaluate/" + map_uid + "/0"
      type: "GET"
      cache: false
      data: {}
      dataType: "html"
      timeout: 1000
      error: ->
        alert "请登陆后进行评价！"

      success: (result) ->
        result = JSON.parse(result)
        $("#eval0").text result["eval0"]
        $("#eval1").text result["eval1"]


  $("#act_eval1").click ->
    $.ajax
      url: "/evaluate/" + map_uid + "/1"
      type: "GET"
      cache: false
      data: {}
      dataType: "html"
      timeout: 1000
      error: ->
        alert "请登陆后进行评价！"

      success: (result) ->
        result = JSON.parse(result)
        $("#eval0").text result["eval0"]
        $("#eval1").text result["eval1"]


  $("#btn_overlay").click ->
    sig_map_1 = $("#over_map_1").val()
    sig_map_2 = $("#over_map_2").val()
    url_new = "/map/overlay/" + map_uid + "/" + sig_map_1
    url_new = url_new + "/" + sig_map_2  unless sig_map_2 is ""
    window.location.href = url_new

  $("#save_view").click ->
    view_url = $("#current_view_url").attr("href").split("?")[1] + "&map=" + map_uid

    # alert(view_url)
    $.ajax
      url: "/layout/save"
      type: "POST"
      cache: false
      data: view_url
      dataType: "html"
      timeout: 1000
      error: ->
        $("#current_view_url").text "请登陆后保存视图，或检查是否已经开始浏览地图！"
        $("#current_view_url").css "color", "red"

      success: (result) ->
        $("#current_view_url").text "视图已成功保存！"
  # 下面开始地图的操作
  ###############################################
  onMapClick = (e) ->
    popup.setLatLng e.latlng
    popup.setContent "坐标位置" + e.latlng.toString()
    currentZoom = map.getZoom()
    cmap_coor = e.latlng
    link_str = "http://www.maplet.org/map/" + map_uid + "?zoom=" + currentZoom + "&lat=" + cmap_coor.lat.toFixed(4) + "&lon=" + cmap_coor.lng.toFixed(4) + "&marker=1"
    link_str = link_str + "&geojson=" + geojsonid  unless geojsonid is ""
    $("#current_view_url").html link_str
    $("#current_view_url").attr "href", link_str
    popup.openOn map
  onZoomend = ->
    currentZoom = map.getZoom()
    cmap_coor = map.getCenter()
    link_str = "http://www.maplet.org/map/" + map_uid + "?zoom=" + currentZoom + "&lat=" + cmap_coor.lat.toFixed(4) + "&lon=" + cmap_coor.lng.toFixed(4)
    link_str = link_str + "&geojson=" + geojsonid  unless geojsonid is ""
    $("#current_view_url").css "color", ""
    $("#current_view_url").html link_str
    $("#current_view_url").attr "href", link_str
  popup = L.popup()
  cities = new L.LayerGroup()
  drawnItems = new L.FeatureGroup()
  # wcs = "121.42.29.253"
  nexrad = L.tileLayer.wms("http://wcs.osgeo.cn:8088/service?",
    layers: "maplet_" + map_uid
    format: "image/png"
    transparent: true
    attribution: "Maplet"
  )
  osm = L.tileLayer("https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiYnVrdW4iLCJhIjoiY2lqeWFjZmo4MXFubndka2lzcnZ1M2tzciJ9.C1dZUQkRZSIEKfg-DaFYpw",
    maxZoom: 18
    attribution: "Map data &copy; <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, " + "<a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, " + "Imagery © <a href=\"http://mapbox.com\">Mapbox</a>"
    id: "mapbox.streets"
  )
  nexrad.addTo cities
  osm.addTo cities
  map = L.map("map",
    center: [ vlat, vlon ]
    zoom: vzoom_current
    maxZoom: vzoom_max
    minZoom: vzoom_min
    layers: [ cities ]
  )

  # 此处为视图可见
  L.marker([ vlat, vlon ]).addTo map  if vmarker.toString() is "1"
  AjaxUrl = "/geojson/gson/" + geojsonid
  unless geojsonid is ""
    $.getJSON AjaxUrl, (gjson) ->
      gson_arr = new Array()
      $.each gjson, (i, item) ->
        gson_arr[i] = item

      L.geoJson(gson_arr).addTo map

  map.on "zoomend", onZoomend
  map.on "moveend", onZoomend
  map.on "click", onMapClick
  baseMaps = osm: osm
  overlayMaps = "专题地图": nexrad
  L.control.layers(baseMaps, overlayMaps).addTo map
