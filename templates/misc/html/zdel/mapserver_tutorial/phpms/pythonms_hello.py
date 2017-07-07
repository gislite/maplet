#!/usr/bin/python
import mapscript
import random


map_path="/mstu/htdocs/bk/"
map_file="hello.map"


image_name="pythonms_hello" \
	+ str(random.randrange(999999)).zfill(6) \
	+ ".png"


map = mapscript.mapObj(map_path + map_file)
img = map.draw()
img.save("/var/www/ms_tmp/" + image_name)




print "Content-type: text/html"
print
print "<html>"
print "<header><title>Python Mapscript Hello World</title></header>"
print "<body>"
print """
<form name="hello" action="pythonms_hello.py" method="POST">
<input type="image" name="img" src="/tmp/%s">
</form>
""" % image_name
print "</body>"
print "</html>"
