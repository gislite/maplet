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
        new OpenLayers.Control.Graticule({interval:[50,30,15,7,4,2,1,.5] }),
        new OpenLayers.Control.MousePosition({})
    ];

    window.onload = function () {

        map = new OpenLayers.Map('mapdiv');


        var google_streets = new OpenLayers.Layer.Google(
            "Google Streets",
            {}
        );


        map.addLayers([google_streets]);

        map.addControl(new OpenLayers.Control.PanZoomBar());
        map.addControl(new OpenLayers.Control.MousePosition());
        map.addControl(new OpenLayers.Control.LayerSwitcher());

        map.addControl(new OpenLayers.Control.Scale());
        var center = new OpenLayers.LonLat(-90, 45);
        map.setCenter(center, 6);
    }

</script>



<?php include('header2.php'); ?>

<h2>使用Google数据</h2>

<p>添加Google数据</p>

<div id="mapdiv"></div>



<?php include('footer.php'); ?>