<?php
/**
 * Created by JetBrains PhpStorm.
 * User: bk
 * Date: 13-11-2
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */

?>


<?php
//------------------------------------------
// Convert from image to map coordinates
function img2map($width, $height, $point, $ext)
{
    $minx = $ext->minx;
    $miny = $ext->miny;
    $maxx = $ext->maxx;
    $maxy = $ext->maxy;
    if ($point->x && $point->y) {
        $x = $point->x;
        $y = $point->y;
        $dpp_x = ($maxx - $minx) / $width;
        $dpp_y = ($maxy - $miny) / $height;
        $x = $minx + $dpp_x * $x;
        $y = $maxy - $dpp_y * $y;
    }
    $pt[0] = $x;
    $pt[1] = $y;
    return $pt;
}

//------------------------------------------
// Default values
$script_name = "example5-2.php";

// path defaults
$map_path = "/mstu/htdocs/";
$map_file = "example5-1.map";
$img_path = "/var/www/ms_tmp/";

// Navigation defaults
$zoomsize = 2;
$pan = "CHECKED";
$zoomout = "";
$zoomin = "";

// Displayed layer defaults
$urbanareas = "CHECKED";
$lakes = "CHECKED";
$states = "CHECKED";
$roads = "CHECKED";

// Default click point
$clickx = 320;
$clicky = 240;
$clkpoint = ms_newPointObj();
$old_extent = ms_newRectObj();

// Default extent
$extent = array(-180, 0, -60, 90);
$max_extent = ms_newRectObj();
$max_extent->setextent(-180, 0, -60, 90);

// Retrieve mapfile and create a map from it
$map = ms_newMapObj($map_path . $map_file);

// If we've been invoked by the form, use form variables
// else drop through and create first map
if (($_POST['img_x'] and $_POST['img_y']) or $_POST['refresh']) {
    echo("sakf");
    if ($_POST['refresh']) {
        // Refresh, fake the coordinates
        $clickx = 320;
        $clicky = 240;
    } else {
        // map click, use real coordinates
        $clickx = $_POST['img_x'];
        $clicky = $_POST['img_y'];
    }

// Set the mouse click location (we need it to zoom)
    $clkpoint->setXY($clickx, $clicky);

// Selected layers changed? set checkbox "CHECKED" status
    if ($_POST['layer']) {
        // any layers selected?
        $layers = join(" ", $_POST['layer']);
        // yes
    } else {
        $layers = "";
        // no
    }
    $this_layer = 0;
    if (preg_match("/urbanareas/", $layers)) {
        $urbanareas = "CHECKED";
        $this_layer = $map->getLayerByName('urbanareas');
        $this_layer->set('status', MS_ON);
    } else {
        $urbanareas = "";
        $this_layer = $map->getLayerByName('urbanareas');
        $this_layer->set('status', MS_OFF);
    }
    if (preg_match("/lakes/", $layers)) {
        $lakes = "CHECKED";
        $this_layer = $map->getLayerByName('lakes');
        $this_layer->set('status', MS_ON);
    } else {
        $lakes = "";
        $this_layer = $map->getLayerByName('lakes');
        $this_layer->set('status', MS_OFF);
    }
    if (preg_match("/states/", $layers)) {
        $states = "CHECKED";
        $this_layer = $map->getLayerByName('states');
        $this_layer->set('status', MS_ON);
    } else {
        $states = "";
        $this_layer = $map->getLayerByName('states');
        $this_layer->set('status', MS_OFF);
    }

// retrieve extent of displayed map
    if ($_POST['extent']) {
        $extent = split(" ", $_POST['extent']);
    }

// Set the map to the extent retrieved from the form
    $map->setExtent($extent[0], $extent[1], $extent[2], $extent[3]);

// Save this extent as a rectObj, we need it to zoom.
    $old_extent->setextent($extent[0], $extent[1], $extent[2], $extent[3]);

// Calculate the zoom factor to pass to zoomPoint method
//
//	zoomfactor = +/- N
//	if N > 0 zoom in - N < 0 zoom out - N = 0 pan
//
    $zoom_factor = $_POST['zoom'] * $_POST['zsize'];


// Set the zoom direction checkbox status
    if ($zoom_factor == 0) {
        $zoom_factor = 1;
        $pan = "CHECKED";
        $zoomout = "";
        $zoomin = "";
    } elseif ($zoom_factor < 0) {
        $pan = "";
        $zoomout = "CHECKED";
        $zoomin = "";
    } else {
        $pan = "";
        $zoomout = "";
        $zoomin = "CHECKED";
    }
    $zoomsize = abs($_POST['zsize']);

// Zoom in (or out) to clkpoint
    $map->zoomPoint($zoom_factor, $clkpoint, $map->width,
        $map->height, $old_extent, $max_extent);
}


// We've dropped thru because the script was invoked directly
// or we've finished panning, zooming and setting layers on or off
// Set unique image names for map, reference and legend
$map_id = sprintf("%0.6d", rand(0, 999999));
$image_name = "third" . $map_id . ".png";
$image_url = "/ms_tmp/" . $image_name;
$ref_name = "thirdref" . $map_id . ".png";
$ref_url = "/ms_tmp/" . $ref_name;
$leg_name = "thirdleg" . $map_id . ".png";
$leg_url = "/ms_tmp/" . $leg_name;
// Draw and save map image
$image = $map->draw();
$image->saveImage($img_path . $image_name);


// Draw and save reference image
//$ref = $map->drawReferenceMap();
//$ref->saveImage($img_path . $ref_name);

// Draw and save legend image
$leg = $map->drawLegend();
$leg->saveImage($img_path . $leg_name);


// Save the extent after panning and zooming in
// a form variable as a space delimited string
$new_extent = sprintf("%3.6f", $map->extent->minx) . " "
    . sprintf("%3.6f", $map->extent->miny) . " "
    . sprintf("%3.6f", $map->extent->maxx) . " "
    . sprintf("%3.6f", $map->extent->maxy);

// Format the scale of the image for display
// $scale = sprintf("%10d", $map->scale);

// Convert click cordinates to map coordinates & format for display
list($x, $y) = img2map($map->width, $map->height, $clkpoint, $old_extent);
$x_str = sprintf("%3.6f", $x);
$y_str = sprintf("%3.6f", $y);


// We're done, output the HTML form
?>
<html>
<head><title>MapScript Third Map</title></head>
<body bgcolor="#E6E6E6">
<form method=post action="<?php echo $script_name; ?>">
    <table width="100%" border="1">
        <tr>
            <td width="60%" rowspan="6">
                <input name="img" type="image" src="<?php echo $image_url; ?>"
                       width=640 height=480 border=2></td>
            <td width="40%" align="center" colspan="3">
                <img SRC="<?php echo $ref_url; ?>"
                     width=300 height=225 border=1></td>
        </tr>
        <tr>
            <td align="left" colspan="3"><font size="-1">
                    Map scale:&nbsp;&nbsp;&nbsp;1:<?php echo $scale; ?>
                </font></td>
        </tr>
        <tr>
            <td align="left" colspan="3"><font size="-1">
                    Click x,y:&nbsp;&nbsp;&nbsp;&nbsp;
                    <?php echo $x_str; ?>,<?php echo $y_str; ?></font></td>
        </tr>
        <tr>
            <td align="left" colspan="3"><font size="-1">
                    <input type="hidden" name="extent"
                           value="<?php echo $new_extent; ?>">Map Extent:&nbsp;
                    <?php echo $new_extent; ?></font></td>
        </tr>
        <tr>
            <td><b>
                    <center>Legend</center>
                </b></td>
            <td><b>
                    <center>Navigation</center>
                </b></td>
            <td><b>
                    <center>Layers</center>
                </b></td>
        </tr>
        <tr>
            <td rowspan="2"><img src="<?php echo $leg_url; ?>"></td>
            <td align="left"><font size="-1">
                    <input type=radio name="zoom" VALUE=0
                        <?php echo $pan; ?>> Pan<br>
                    <input type=radio name="zoom" VALUE=1
                        <?php echo $zoomin; ?>> Zoom In<br>
                    <input type=radio name="zoom"
                           VALUE=-1 <?php echo $zoomout; ?>> Zoom Out<br>
                    <input type=text name="zsize"
                           VALUE="<?php echo $zoomsize; ?>" SIZE=2> Size<br>
                    <input type=SUBMIT name="refresh" VALUE="Refresh"></td>
            <td align="top">
                <input type="checkbox" name="layer[]" value="urbanareas"
                    <?php echo $urbanareas; ?> >Urban Areas<br>
                <input type="checkbox" name="layer[]" value="lakes"
                    <?php echo $lakes; ?> >Lakes<br>
                <input type="checkbox" name="layer[]" value="states"
                    <?php echo $states; ?> >State Boundaries<br>
                <input type="checkbox" name="layer[]" value="roads"
                    <?php echo $roads; ?> >Roads<br></font></td>
        </tr>
    </table>
</form>
</body>
</html>