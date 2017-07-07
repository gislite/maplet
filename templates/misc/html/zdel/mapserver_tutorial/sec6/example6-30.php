<?php include('config.php'); ?>

<?php include('header1.php'); ?>


<style type="text/css">
    ##mapdiv {
        width: 800px;
        height: 475px;
        border: 1px solid black;
    }
</style>


<script type="text/javascript"
        src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAru6g_mT9_XWxUQOmw8T5ioY0kqGLc8so&sensor=false">
</script>


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
        new OpenLayers.Control.Graticule({interval: [50, 30, 15, 7, 4, 2, 1, .5] }),
        new OpenLayers.Control.MousePosition({})
    ];

    window.onload = function () {

        map = new OpenLayers.Map('mapdiv', {controls: controls_array});

        var serverURL = "http://192.168.4.111/cgi-bin/mapserv";

        var layer_obj2 = new OpenLayers.Layer.WMS(
            "EX2f",
            serverURL,
            {
                layers: 'wt',
                map: '/mstu/htdocs/sec6/map.map',
                format: 'png'
            },
            {
                reproject: true,
                'numZoomLevels': 20,
                gutter: 15,
                buffer: 0
            }
        );


        var layer_obj3 = new OpenLayers.Layer.WMS(
            "EX3",
            serverURL,
            {
                layers: 'wetland2000',
                attribution: 'name2',
                map: '/mstu/htdocs/sec6/map.map',
                format: 'png'
            },
            {
                reproject: true,
                'numZoomLevels': 20,
                gutter: 15,
                buffer: 0
            }
        );


        // map.addLayers([layer_obj, layer_obj2, google_streets]);
        map.addLayers([layer_obj2, layer_obj3]);

        map.addControl(new OpenLayers.Control.ScaleLine());

        var center = new OpenLayers.LonLat(110, 35);
        map.setCenter(center, 4);
        // map.zoomToMaxExtent();
    }

</script>

<?php include('header2.php'); ?>


<h3>使用MapServer发布的服务</h3>

<p>使用MapServer发布的服务，复杂一点的例子。</p>

<div id="mapdiv"></div>

<?php include('footer.php'); ?>
