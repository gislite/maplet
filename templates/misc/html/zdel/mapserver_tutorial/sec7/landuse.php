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
        new OpenLayers.Control.Graticule({interval: [10, 5, 3, 2], targetSize: 500}),
        new OpenLayers.Control.MousePosition({})
    ];
    window.onload = function () {
        var options = { // scales: [50000000, 30000000, 10000000, 5000000, ],
            // resolutions: [1.40625,0.703125,0.3515625,0.17578125,0.087890625,0.0439453125],
            // minScale: 500,
            //  projection: "EPSG:4326",
            //                    maxResolution: "auto",
            //         maxExtent: new OpenLayers.Bounds( 114, 38, 136, 54),
            //             maxResolution: 0.04296875,
            //maxScale: 1000000,
            // minResolution: "auto",
            // minExtent: new OpenLayers.Bounds(-1, -1, 1, 1),
            //  minResolution: 0.0001,
            // numZoomLevels: 7,
            units: "degrees",
            controls: controls_array
        };
        map = new OpenLayers.Map('mapdiv', options);
        var layer_obj2 = new OpenLayers.Layer.WMS("landuse", "/cgi-bin/tilecache.cgi?",
            {layers: 'lu',
                format: 'image/png'
            },
            {
                reproject: false,
                maxExtent: new OpenLayers.Bounds(114, 38, 136, 54),
                // maxResolution: 0.04296875,
                resolutions: [0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125]
//	resolutions:[0.02,0.01,0.005,0.0025,0.00125]
            }
        );

        var layer_dem = new OpenLayers.Layer.WMS("dem", "/cgi-bin/tilecache.cgi?",
            {layers: 'dem',
                format: 'image/png'
            },
            {
                reproject: false,
                maxExtent: new OpenLayers.Bounds(114, 38, 136, 54),
                // maxResolution: 0.04296875,
                resolutions: [0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125]
//	resolutions:[0.02,0.01,0.005,0.0025,0.00125]
            }
        );
        map.addLayers([layer_obj2, layer_dem]);
        map.addControl(new OpenLayers.Control.ScaleLine());
        var center = new OpenLayers.LonLat(125, 46);
        map.setCenter(center, 6);
        // map.zoomToMaxExtent();
    }
</script>
<?php include('../sec6/header2.php'); ?>

<h2>东北地区2010年土地利用在线地图（参考）</h2>

<div id="mapdiv"></div>

<div>科研成果WebGIS演示系统--中国科学院东北地理与农业生态研究所</div>

<p/>

<hr/>

<p/>

<div style="text-align:center">科研成果WebGIS演示系统--中国科学院东北地理与农业生态研究所</div>

<?php include('../sec6/footer.php'); ?>
