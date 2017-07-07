/**
 * Created by bukun on 2015/3/25.
 */


var imageGroup = new OpenLayers.LayerGroup({
    layers: [new OpenLayers.Layer.WMTS({
        name: "影像图",
        url: "http://t0.tianditu.com/img_c/wmts",//影像图
        layer: "img",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: true,
        mirrorUrl: ["http://t0.tianditu.com/img_c/wmts", "http://t1.tianditu.com/img_c/wmts", "http://t2.tianditu.com/img_c/wmts", "http://t3.tianditu.com/img_c/wmts",
            "http://t4.tianditu.com/img_c/wmts", "http://t5.tianditu.com/img_c/wmts", "http://t6.tianditu.com/img_c/wmts", "http://t7.tianditu.com/img_c/wmts"]
    }), new OpenLayers.Layer.WMTS({
        name: "影像图注记",
        url: "http://t0.tianditu.com/cia_c/wmts",//中国影像图注记
        layer: "cia",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: false
    })]
});
var vectorGroup = new OpenLayers.LayerGroup({
    layers: [new OpenLayers.Layer.WMTS({
        name: "中国底图(矢量)",
        url: "http://t0.tianditu.com/vec_c/wmts",//中国底图
        layer: "vec",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: true
    }), new OpenLayers.Layer.WMTS({
        name: "中国底图注记",
        url: "http://t0.tianditu.com/cva_c/wmts",//中国底图注记
        layer: "cva",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: false
    })]
});
var terrGroup = new OpenLayers.LayerGroup({
    layers: [new OpenLayers.Layer.WMTS({
        name: "地形图",
        url: "http://t0.tianditu.com/ter_c/wmts",//地形图
        layer: "ter",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: true
    }), new OpenLayers.Layer.WMTS({
        name: "地形图注记",
        url: "http://t0.tianditu.com/cta_c/wmts",//地形图注记
        layer: "cta",
        style: "default",
        matrixSet: "c",
        format: "tiles",
        isBaseLayer: false
    })]
});


zoomIn = 1.0;
zoomRate = 4.0;

var dxh_style = new OpenLayers.StyleMap({
    "default": new OpenLayers.Style({
            pointRadius: "${ptRadius}",
            fillColor: "${ptCol}",
            strokeColor: "#fe0000",
            strokeWidth: 1,
            graphicZIndex: 1
        },
        {
            context: {
                ptRadius: function (feature) {
                    var num = feature.attributes.mag;
                    if (num >= 8) {
                        return 12;
                    }
                    else if (num < 8 && num >= 7) {
                        return 10;
                    }
                    else if (num < 7 && num >= 5) {
                        return 8;
                    }
                    else if (num < 5 && num >= 3) {
                        return 5;
                    }
                    else {
                        return 3;
                    }
                },
                ptCol: function (feature) {
                    var num = feature.attributes.mag;
                    if (num >= 8) {
                        return "#FF0000";
                    }
                    else if (num < 8 && num >= 7) {
                        return "#FF8000";
                    }
                    else if (num < 7 && num >= 5) {
                        return "#FcF256";
                    }
                    else if (num < 5 && num >= 3) {
                        return "#0022FE";
                    }
                    else {
                        return "#00FE1A";
                    }
                }
            }
        }),
    "select": new OpenLayers.Style({
        fillColor: "#66ccff",
        strokeColor: "#3399ff",
        graphicZIndex: 2
    })
});


function init_toolbar() {
    var loadImage = function () {
        map.loadLayerGroup(imageGroup);//影像图
    }
    var loadVector = function () {
        map.loadLayerGroup(vectorGroup);
    }
    var imageButton = new OpenLayers.Control.Button({
        displayClass: 'olControlImage',
        trigger: loadImage
    })
    var vectorButton = new OpenLayers.Control.Button({
        displayClass: 'olControlVector',
        trigger: loadVector
    })
    var control_panel = new OpenLayers.Control.Panel({});
    //Add some controls to it
    control_panel.addControls([imageButton, vectorButton])
    map.addControl(control_panel);
    control_panel.moveTo(new OpenLayers.Pixel(450, 0));
    imageButton.activate();

    var ls = new OpenLayers.Control.LayerSwitcher();
    map.addControl(ls);
    ls.maximizeControl();
}

function init_map(lon, lat, timeStr, epicenter, numMag, zoom) {
    var option = {
        controls: [new OpenLayers.Control.Navigation(),
            new OpenLayers.Control.PanZoomBar()
        ],
        numZoomLevels: 9
    };
    map = new OpenLayers.Map('tdt_map', option);
    //map.loadLayerGroup(vectorGroup);//矢量图
    map.loadLayerGroup(imageGroup);//影像图
    markersLayer = new OpenLayers.Layer.Markers("makersLayer");
    map.addLayer(markersLayer);
    map.maxZoom = 14;//设置地图的最大缩放级别
    map.minZoom = 9;
    //init_toolbar();

    ll = new OpenLayers.LonLat(lon, lat);
    var popHTML = "<div align='left' style='font-size:14px'> 发震时间：" + timeStr + "<br />经度：" + lon + "<br />纬度：" + lat + "<br />  参考震中：" + epicenter + "<br />震级：" + numMag + "<br /> </div> ";
    var pointFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(lon, lat),
        {lon: lon, lat: lat, orgTime: timeStr, locat: epicenter, mag: numMag, html: popHTML}
    );
    if (zoom == true) {
        addMarker_dyna(map, ll, popHTML);
        map.setCenter(ll, 3);
        var int = self.setInterval("zoom()", 4000);//1.8秒放大或缩小图形一次
    }
    else {
        addMarker_stat(map, ll, pointFeature);
        map.setCenter(ll, map.minZoom);
    }

}

function addMarker_dyna(map, ll, popHTML) {
    var feature = new OpenLayers.Feature(markersLayer, ll);
    var size = new OpenLayers.Size(15, 22);
    var theIcon = new OpenLayers.Icon('img/eq/eq.gif', size);//标注点的图标
    //     var theSize = new OpenLayers.Size(350, 200);//弹出窗口的大小
    feature.data.popupContentHTML = popHTML;//点击标注点显示的内容，html格式
    var marker = new OpenLayers.Marker(ll, theIcon);
    marker.feature = feature;
    map.setCenter(ll);
    markersLayer.addMarker(marker);
    //add the popup and relative text
    popup = new OpenLayers.Popup("dxh", ll, new OpenLayers.Size(200, 200), popHTML, false);
    popup.closeOnMove = false;
    popup.backgroundColor = "pink",
        popup.opacity = 0.8;
    popup.autoSize = true;
    popup.padding = 10;
    map.addPopup(popup);
    popup.show();
}
function addMarker_stat(map, ll, pointFeature) {
    var vectorLayer = new OpenLayers.Layer.Vector("earthquake", {styleMap: dxh_style});
    vectorLayer.addFeatures(pointFeature);
    map.addLayer(vectorLayer);

    function onPopupClose(evt) {
        selectControl.unselect(selectedFeature);
    }

    function onFeatureSelect(feature) {
        selectedFeature = feature;
        popHtml = "<div>发震时间：" + feature.attributes.orgTime + "<br /></div><div>震级:" +
        feature.attributes.mag + "<br /></div><div>经纬度:(" + feature.attributes.lon + "," + feature.attributes.lat + ")<br /></div><div>参考震中:" + feature.attributes.locat + "<br /></div><div>震源深度:" + feature.attributes.dept + "<br /></div><div></div>";
        popup = new OpenLayers.Popup.FramedCloud("chicken",
            feature.geometry.getBounds().getCenterLonLat(),
            new OpenLayers.Size(1000, 500),
            popHtml,
            null,
            true,
            onPopupClose
        );
        feature.popup = popup;
        map.addPopup(popup);
    }

    function onFeatureUnselect(feature) {
        map.removePopup(feature.popup);
        feature.popup.destroy();
        feature.popup = null;
    }

    selectControl = new OpenLayers.Control.SelectFeature([vectorLayer],
        {
            clickout: true, toggle: false,
            multiple: false, hover: false,
            toggleKey: "ctrlKey", // ctrl key removes from selection
            multipleKey: "shiftKey" // shift key adds to selection
        }
    );
    map.addControl(selectControl);
    selectControl.activate();
    vectorLayer.events.on({
        "featureselected": function (e) {
            onFeatureSelect(e.feature);
        },
        "featureunselected": function (e) {
            onFeatureUnselect(e.feature);
        }
    });
}
function zoom() {
    if (zoomIn == 1)
        zoomRate++;//放大底图
    else
        zoomRate--;//缩小底图

    if (zoomRate < 3) {
        zoomIn = 1;
        zoomRate = 5;
    }
    if (zoomRate > 11)//国内地图的放大级数一般为15级，个别城市能达到18级
    {
        zoomRate = 11;
        zoomIn = 0
    }

    map.setCenter(ll, zoomRate);
}

