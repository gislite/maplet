<?php include('../sec6/config.php'); ?>

<?php include('../sec6/header1.php'); ?>


    <style type="text/css">
        #mapdiv {
            width: 800px;
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

        var markers = new OpenLayers.Layer.Markers("Markers");

        window.onload = function () {

            map = new OpenLayers.Map('mapdiv', {controls: controls_array});

            map.addLayer(markers);//地图初始化添加

            // var serverURL = "/cgi-bin/mapserv";
            var serverURL = "/cgi-bin/tilecache.cgi?";


            var layer_obj1 = new OpenLayers.Layer.WMS(
                "EX2f",
                serverURL,
                {
                    layers: 'ne_shiji_quhua,ne_xianji_quhua_line,ne_shengjiquhua_line,ne_xiangzhen_point',
                    map: '/opt/webgis/data/geocn.map',
                    format: 'png'
                },
                {
                    reproject: true,
                    'numZoomLevels': 20,
                    gutter: 15,
                    buffer: 0
                }
            );


            var layer_obj2 = new OpenLayers.Layer.WMS("EX2f",
                "/cgi-bin/tilecache.cgi?", {layers: 'nexzqh', format: 'image/png' });


            // map.addLayers([layer_obj, layer_obj2, google_streets]);
            map.addLayers([layer_obj2]);

            map.addControl(new OpenLayers.Control.ScaleLine());

            var center = new OpenLayers.LonLat(125, 46);
            map.setCenter(center, 6);
            // map.zoomToMaxExtent();
        }

    </script>



<?php include('../sec6/header2.php'); ?>

    <h2>东北地区行政区划参考图 </h2>
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