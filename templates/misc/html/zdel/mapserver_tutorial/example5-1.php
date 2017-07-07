<?php

$image_name = sprintf("phpms-hello%0.6d", rand(0, 999999)) . ".png";

$map = ms_newMapObj("/mstu/htdocs/example5-1.map");
// Create an image of the map and save it to disk
$image = $map->draw();
$image->saveImage("/var/www/ms_tmp/" . $image_name);
?>

<?php
include('config.php');
?>

<?php
include('header.php');
?>


<h3>开始使用PHP Mapscript</h3>

<div>下面开始介绍在PHP环境中使用MapScript进行地图开发。</div>

<div>首先要注意的是在MapScript环境下mapfile的写法：</div>

<div>You have to set STATUS DEFAULT. This is different in MapServer CGI and
    MapScript: with CGI a layer is shown by default only when STATUS is
    DEFAULT layers with STATUS ON are visible only when specified in the
    URL. In MapScript layers with STATUS ON are visible by default. I
    don't know why this is so; practically everyone (including myself) has
    stumbled over that one.
</div>

<div>There is an extensive list of postings on this matter: search for
    STATUS DEFAULT on the user's list. Of course, when you had known that
    you should look for STATUS DEFAULT, you would already have known the
    answer :-) . That's always the problem with searching a mailing list.
</div>

<hr/>

<form action="example5-1.php" method="POST">
    <input type="image" name="img" src="/ms_tmp/<?php echo $image_name; ?>">
    <input type="submit" value="Submit"/>
</form>


<?php
include('footer.php');
?>
