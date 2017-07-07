
<?php include('config.php'); ?>

<?php include('header1.php'); ?>


    <style type="text/css">
        ##mapdiv {
            width: 800px;
            height: 475px;
            border: 1px solid black;
        }
    </style>


    <script src="http://maps.google.com/maps/api/js?v=3.2&sensor=false"></script>


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

            map = new OpenLayers.Map('mapdiv', {controls: controls_array},
                    {
                        maxExtent: new OpenLayers.Bounds(//设置地图域
                                -128 * 156543.0339,
                                -128 * 156543.0339,
                                128 * 156543.0339,
                                128 * 156543.0339),
                        maxResolution: 156543.0339,//最大分辨率
                        units: 'm',//度量单位
                        projection: new OpenLayers.Projection('EPSG:900913'),//投影规则
                        displayProjection: new OpenLayers.Projection("EPSG:4326"), //显示的投影规则
                        maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34, 20037508.34, 20037508.34)
                    });

            map.addLayer(markers);//地图初始化添加

            var serverURL = "/cgi-bin/mapserv";

            layer = new OpenLayers.Layer.WMS("OpenLayers WMS",
                    "http://vmap0.tiles.osgeo.org/wms/vmap0", {layers: 'basic'});


            var gmap = new OpenLayers.Layer.Google(
                    "Google Streets", // the default
                    {numZoomLevels: 20},
                    {isBaseLayer: false}
                    // default type, no change needed here
            );


            var layer_obj3 = new OpenLayers.Layer.WMS(
                    "EX2f",
                    serverURL,

                    {
                        // projection: new OpenLayers.Projection("EPSG:4326"),
                        //sphericalMercator: true,
                        layers: 'marsh2k',
                        map: '/opt/webgis/data/marshland.map',
                        format: 'png',
                        visibility: false,
                        trs: 'EPSG:900913'

                    },
                    {
                        reproject: true,
                        'numZoomLevels': 20,
                        gutter: 15,
                        buffer: 0
                    },
                    {isBaseLayer: false}
            );


            var serverURL = "/cgi-bin/tilecache.cgi?";
            var layer_obj2 = new OpenLayers.Layer.WMS("EX2f",
                    "/cgi-bin/tilecache.cgi?", {layers: 'ne', format: 'image/png' });


            layer_obj3.setIsBaseLayer(false);
            // map.addLayers([layer_obj, layer_obj2, google_streets]);
            // map.addLayer(gmap);
            map.addLayer(gmap);

            map.addControl(new OpenLayers.Control.ScaleLine());

            //var center = new OpenLayers.LonLat(124, 46);
            //map.setCenter(center, 6);

            // map.zoomToExtent(layer_obj3.getDataExtent());
            // map.zoomToMaxExtent();
        }

    </script>


<?php include('config.php'); ?>

<?php include('header2.php'); ?>

<h3>添加Google Street数据</h3>

<div id="mapdiv"></div>



<?php include('footer.php'); ?>