<?php include('../sec6/config.php'); ?>
<?php include('../sec6/header1.php'); ?>

<style type="text/css">
    #mapdiv {
        width: 1000px;
        height: 600px;
        border: 1px solid black;
    }
</style>


<script type="text/javascript">

    var navigation_control = new OpenLayers.Control.Navigation({});
    var controls_array = [
        navigation_control,
        new OpenLayers.Control.PanZoomBar({}),
        new OpenLayers.Control.LayerSwitcher({}),
        new OpenLayers.Control.Permalink(),
        new OpenLayers.Control.Graticule({interval: [10, 5, 3, 2], targetSize: 500}),
        new OpenLayers.Control.MousePosition({})
    ];

    window.onload = function () {
        map = new OpenLayers.Map('mapdiv', {controls: controls_array, units: "degrees"});
///////////////////////////////////////////////////////////////////////////////////
        var layer_dt = new OpenLayers.Layer.WMS("底图",
            "/cgi-bin/tilecache.cgi?",
            {layers: 'dt',
                format: 'image/png'
            },
            {
                reproject: false,
                maxExtent: new OpenLayers.Bounds(73.4, 17, 135.4, 53.6),
                resolutions: [0.060546875, 0.0302734375, 0.01513671875, 0.007568359375, 0.0037841796875, 0.00189208984375]
            }
        );

// maxResolution:     0.12,
// levels: 7
//				resolutions: [0.0608,0.0304,0.0152,0.0076,0.0038,0.0019]
//      resolutions:[0.02,0.01,0.005,0.0025,0.00125]


/////////////////////////////////////////////////////////////////////////////////////////////
        var layer_ms = new OpenLayers.Layer.WMS("沼泽湿地",
            "/cgi-bin/tilecache.cgi?",
            {layers: 'ms',
                format: 'image/png'
            },
            {
                reproject: false,
                maxExtent: new OpenLayers.Bounds(73.4, 17, 135.4, 53.6),
                resolutions: [0.060546875, 0.0302734375, 0.01513671875, 0.007568359375, 0.0037841796875, 0.00189208984375]
            }
        );
        layer_ms.setIsBaseLayer(false);
//////////////////////////////////////////////////////////////////////////////////////////////

        var serverURL = "/cgi-bin/mapserv";
        var tmp = new OpenLayers.Layer.WMS(
            "沼泽湿地",
            serverURL,
            {
                layers: 'marsh2kgeo',
                map: '/opt/webgis/data/geocn.map',
                format: 'png'

            }
            ,
            {
                isBaseLayer: false,
//  reproject: true,
//   maxExtent: new OpenLayers.Bounds(73.4, 3.8, 135.4, 53.6),
// resolutions: [0.060546875,0.0302734375,0.01513671875,0.007568359375,0.0037841796875,0.00189208984375]

            }
        );
        ////////////////////////////////////////////////////////////////////////

        var serverURL = "/cgi-bin/mapserv";
        var tmp_dt = new OpenLayers.Layer.WMS(
            "底图",
            serverURL,
            {
                layers: 'BOUNT_poly,bou2_4l,diquJie_polyline',
                map: '/opt/webgis/data/geocn.map',
                format: 'png'
            }
            ,
            {
                reproject: true,
                maxExtent: new OpenLayers.Bounds(73.4, 17, 135.4, 53.6),
                resolutions: [0.060546875, 0.0302734375, 0.01513671875, 0.007568359375, 0.0037841796875, 0.00189208984375]

            }
        );

        // 使用TileCache的方式加入
        map.addLayers([layer_dt, layer_ms ]);
        // 使用MapServer的方式加入
        // map.addLayers([tmp_dt, tmp]);

        map.addControl(new OpenLayers.Control.ScaleLine());
        var center = new OpenLayers.LonLat(105, 36);
        map.setCenter(center, 0);
        // var center = new OpenLayers.LonLat(124, 46);
        // map.setCenter(center, 6);
        // map.zoomToMaxExtent();
    }
</script>

<?php include('../sec6/header2.php'); ?>

<h3>中国沼泽湿地调查2000年沼泽湿地分布（土地利用分类）在线地图</h3>

<div></div>
<p/>

<div id="mapdiv"></div>

<p/>

<p/>

<p/>
<hr/>
<p/>

<p/>

<p/>

<div style="text-align:center">科研成果WebGIS演示系统--中国科学院东北地理与农业生态研究所</div>



<?php include('../sec6/footer.php'); ?>
