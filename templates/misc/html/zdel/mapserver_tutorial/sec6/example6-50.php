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
                layers: 'd2001_out,rail,djs,sjj,dqm,jmd,yx',
                map: '/opt/webgis/data/landuse.map',
                format: 'png'
            },
            {
                reproject: true,
                'numZoomLevels': 20,
                gutter: 15,
                buffer: 0
            }
        );


        var serverURL = "/cgi-bin/tilecache.cgi?";
        var layer_obj2 = new OpenLayers.Layer.WMS("EX2f",
            "/cgi-bin/tilecache.cgi?", {layers: 'ne', format: 'image/png' });


        // map.addLayers([layer_obj, layer_obj2, google_streets]);
        map.addLayers([layer_obj3]);

        map.addControl(new OpenLayers.Control.ScaleLine());

        var center = new OpenLayers.LonLat(124, 46);
        map.setCenter(center, 6);
        // map.zoomToMaxExtent();
    }

</script>


<?php include('header2.php'); ?>

<h2>使用TileCache作为服务发布</h2>

<div id="mapdiv"></div>



<?php include('footer.php'); ?>
