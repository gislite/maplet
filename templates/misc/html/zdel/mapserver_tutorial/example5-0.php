<?php
/**
 * Created by PhpStorm.
 * User: bk
 * Date: 14-3-5
 * Time: 下午6:38
 */
?>

<?php

// echo(phpinfo());
// dl('php_mapscript.so');

$map_path = "/mstu/htdocs/";

$map = ms_newMapObj($map_path . "example5-1.map");
$map->setSize(400, 300);
// $map->colorObj->setRGB(0,255,0);
$image = $map->draw();

// $image->width = 800;
//$image->height = 800;

$image_url = $image->saveWebImage();

?>


<?php
include('config.php');
?>

<?php
include('header.php');
?>


<IMG SRC=<?php echo $image_url; ?>>

<?php
include('footer.php');
?>
