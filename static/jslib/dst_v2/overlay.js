(function(){$(document).ready(function(){var app_arr,app_url,baseMaps,ii,jj,lyrs,map,mycars,osm,overlayMaps;for(lyrs=new L.LayerGroup,mycars=new Array,app_url=$("#app_ctrl").val(),app_arr=app_url.split("/"),jj=0;jj<app_arr.length;)mycars[jj]=L.tileLayer.wms("http://wcs.osgeo.cn:8088/service?",{layers:"maplet_"+app_arr[jj].substring(1),format:"image/png",transparent:!0,attribution:"Maplet"}),mycars[jj].addTo(lyrs),jj++;for(osm=L.tileLayer.chinaProvider("Google.Normal.Map",{maxZoom:18,minZoom:1}),map=L.map("map",{center:[vlat,vlon],zoom:vzoom_current,maxZoom:vzoom_max,minZoom:vzoom_min,layers:[lyrs]}),osm.addTo(map),baseMaps={osm:osm},overlayMaps={},ii=0;ii<app_arr.length;)overlayMaps[app_arr[ii]]=mycars[ii],ii++;return L.control.layers(baseMaps,overlayMaps).addTo(map)})}).call(this);