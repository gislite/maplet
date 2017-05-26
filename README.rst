Maplet
=======

Source codes for http://www.maplet.org

Introduction
------------------------

The CMS was desinged as the container for GIS showing as the very beginning.
It has the basic map showing, map overlaying, GIS data editing funtion now.
And, the CMS could used to publish different kind the information,
such as basic HTML page, JavaScript app, maps, multimedias.

Setup
-----------------------
## Database


    \set dbname maplet
    CREATE USER :dbname WITH PASSWORD '131322' ;
    CREATE DATABASE :dbname OWNER :dbname ;
    GRANT ALL PRIVILEGES ON DATABASE :dbname to :dbname ;
    \c :dbname ;
    create extension hstore;
    \q