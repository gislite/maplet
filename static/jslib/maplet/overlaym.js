(function () {
    $(document).ready(function () {
        var app_arr, app_url, baseMaps, ii, jj, lyrs, map, mycars, osm, overlayMaps;
        lyrs = new L.LayerGroup;
        mycars = new Array;
        app_url = $("#app_ctrl").val();
        app_arr = app_url.split("/");
        jj = 0;
        while (jj < app_arr.length) {
            mycars[jj] = L.tileLayer.wms("http://wcs.osgeo.cn:8088/service?", {
                layers: "maplet_" + app_arr[jj].substring(1),
                format: "image/png",
                transparent: true,
                attribution: "Maplet"
            });
            mycars[jj].addTo(lyrs);
            jj++
        }

        var osm = L.tileLayer.chinaProvider('TianDiTu.Normal.Annotion', {
            maxZoom: 18,
            minZoom: 1
        });

        var osm1 = L.tileLayer.chinaProvider('TianDiTu.Normal.Map', {
            maxZoom: 18,
            minZoom: 1
        });

        var the_basemap = L.layerGroup([osm1, osm]);

        map = L.map("map", {
            center: [vlat, vlon],
            zoom: vzoom_current,
            maxZoom: vzoom_max,
            minZoom: vzoom_min,
            layers: [lyrs]
        });


        the_basemap.addTo(map);
        baseMaps = {osm: the_basemap};
        overlayMaps = {};
        ii = 0;
        while (ii < app_arr.length) {
            overlayMaps[app_arr[ii]] = mycars[ii];
            ii++
        }
        return L.control.layers(baseMaps, overlayMaps).addTo(map)
    })
}).call(this);
