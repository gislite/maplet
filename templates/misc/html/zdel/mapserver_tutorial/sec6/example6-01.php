

<?php include('config.php');?>

<?php include('header1.php');?>

    <script type='text/javascript'>
        var map;
        window.onload = function () {

            map = new OpenLayers.Map('map_element', {});
            var wms = new OpenLayers.Layer.WMS(
                    'OpenLayers WMS',
                    'http://vmap0.tiles.osgeo.org/wms/vmap0',
                    {layers: 'basic'},
                    {}
            );

            map.addLayer(wms);
            if (!map.getCenter()) {
                map.zoomToMaxExtent();
            }
        }
    </script>

<?php include('header2.php');?>

<div id='map_element' style='width: 400px; height: 300px;'>

</div>

<div>

    <h3 id="_1">工具简介</h3>
    <p>OpenLayers是一个用于开发WebGIS客户端的JavaScript包。OpenLayers 支持的地图来源包括Google Maps、Yahoo、 Map、微软Virtual Earth
        等，用户还可以用简单的图片地图作为背景图，与其他的图层在OpenLayers 中进行叠加，在这一方面OpenLayers提供了非常多的选择。除此之外，OpenLayers实现访问地理空间数据的方法都符合行业标准。OpenLayers
        支持Open GIS 协会制定的WMS（Web Mapping Service）和WFS（Web Feature Service）等网络服务规范，可以通过远程服务的方式，将以OGC
        服务形式发布的地图数据加载到基于浏览器的OpenLayers 客户端中进行显示。OpenLayers采用面向对象方式开发，并使用来自Prototype.js和Rico中的一些组件。</p>
    <p>OpenLayers 是一个专为Web GIS 客户端开发提供的JavaScript
        类库包，用于实现标准格式发布的地图数据访问。从OpenLayers2.2版本以后，OpenLayers已经将所用到的Prototype.js组件整合到了自身当中，并不断在Prototype.js的基础上完善面向对象的开发，Rico用到地方不多，只是在OpenLayers.Popup.AnchoredBubble类中圆角化DIV。OpenLayers2.4版本以后提供了矢量画图功能，方便动态地展现“点、线和面”这样的地理数据。</p>
    <p>在操作方面，OpenLayers 除了可以在浏览器中帮助开发者实现地图浏览的基本效果，比如放大（Zoom In）、缩小（Zoom
        Out）、平移（Pan）等常用操作之外，还可以进行选取面、选取线、要素选择、图层叠加等不同的操作，甚至可以对已有的OpenLayers 操作和数据支持类型进行扩充，为其赋予更多的功能。例如，它可以为OpenLayers
        添加网络处理服务WPS 的操作接口，从而利用已有的空间分析处理服务来对加载的地理空间数据进行计算。同时，在OpenLayers提供的类库当中，它还使用了类库Prototype.js 和Rico 中的部分组件，为地图浏览操作客户端增加Ajax
        效果。</p>
    <h3 id="_2">资源下载</h3>
    <p>下载网址： http://openlayers.org
        下载并解压后文件结构如下：</p>
    <h3 id="_3">入门示例</h3>
    <p>做示例很简单，把示例所需的两个文件夹img, theme和文件OpenLayers.js拷贝到同一目录下，如同置于code目录下。
        在code目录下新建文件index.html，其内容如下：</p>


</div>

     <pre>



     </pre>

<div>保存完成后用浏览器打开index.html，就可以看到示例效果，如下图：</div>


代码解析

要使用该库，其基本步骤大体为：

1. 引用脚本库及相应资源

2. 定义一个用以显示地图信息的页面元素，一般为div元素

3. 定义一个map脚本对象，使用OpenLayers对其初始化，上例中初始化参数分别是地图容器的id以及所需参数信息，其中参数信息是以JSON形式传递的

4.创建一个layer脚本对象并初始化

5.将layer添加到map中，这里可以添加任意多个，此处用一个作示例

6.给你的地图一个域信息

至于各对象参数详细信息，大家查看下载到的apidoc就行了，不再赘述。


</body>
</html>
