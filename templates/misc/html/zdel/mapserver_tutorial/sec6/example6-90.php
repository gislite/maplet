
<?php include('config.php'); ?>

<?php include('header1.php'); ?>

    <style type="text/css">
        ##mapdiv {
            width: 800px;
            height: 475px;
            border: 1px solid black;
        }
    </style>



    <script type="text/javascript">
        //Google has 20 scales
        //Resolutions always start big to small, descending order
        //numZoomLevels=20 (corresponds to Googles 20 zoom levels)
        //map.setCenter(center, 19); (Zoom Level starts at 0 and ends in 19)


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

            var serverURL = "/cgi-bin/mapserv";


            var layer_obj3 = new OpenLayers.Layer.WMS(
                    "EX2f",
                    serverURL,
                    {
                        layers: 'BOUNT_poly,bou2_4l,diquJie_polyline',
                        map: '/opt/webgis/data/geocn.map',
                        format: 'png'
                    },
            {isBaseLayer: true}
            );


            var layer_ms = new OpenLayers.Layer.WMS(
                    "marsh",
                    serverURL,
                    {
                        layers: 'marsh2kgeo',
                        map: '/opt/webgis/data/geocn.map',
                        format: 'png'
                    },
            {isBaseLayer: false}

            );

            //layer_ms.setBaseLayer(false);

            // map.addLayers([layer_obj, layer_obj2, google_streets]);
            map.addLayer(layer_obj3);

            map.addLayer(layer_ms);

            map.addControl(new OpenLayers.Control.ScaleLine());

            var center = new OpenLayers.LonLat(105, 35);
            map.setCenter(center, 4);
            // map.zoomToMaxExtent();
        }

    </script>


<?php include('header2.php'); ?>
<h3>使用MapServer发布服务：中国分县地图</h3>

<p>后台使用MapServer。</p>
<p>前台使用OpenLayers。</p>
<div id="mapdiv"></div>
`
<?php include('footer.php'); ?>

