jBox: Java Client Extention for MapServer

Overview: The jBox and jBox PNG are simple java applets to add "rubberband" box
zoom and query capabilities to MapServer applications. They use Netscape
LiveConnect for applet/javascript communication. Netscape Navigator 3.0+
and MSIE 4.0+ are known to support it.

jBox is used with GIF and JPEG images. jBoxPNG supports PNG images by way 
of sixlegs.

See: http://mapserver.gis.umn.edu/doc40/jbox-howto.html for documentation.

Files:
	For GIF and/or JPEG images:
			jBox.class
			evalThread.class
		<OR>
			jBox.jar
	For PNG images:
			jBoxPNG.class
			evalThread.class
			com/sixlegs/image/png/*.class
		<OR>
			jBoxPNG.jar
			png.jar
	Source:
		jBox.java
		jBoxPNG.java
		evalThread.java