Maplet
=======

Source codes for http://www.maphub.org

Introduction
------------------------

The CMS was desinged as the container for GIS showing as the very beginning.
It has the basic map showing, map overlaying, GIS data editing funtion now.
And, the CMS could used to publish different kind the information,
such as basic HTML page, JavaScript app, maps, multimedias.


Building the environment
---------------------------------

::

    python3 -m venv ~/vpy_maplet
    source ~/vpy_maplet/bin/activate
    pip3 install -r doc/requirements.txt
    git clone https://github.com/bukun/torcms_f2elib.git static/f2elib
    git clone https://github.com/bukun/torcms_modules_bootstrap.git templates/modules

Setup
-----------------------

Database

::

    \set dbname maplet
    CREATE USER :dbname WITH PASSWORD '131322' ;
    CREATE DATABASE :dbname OWNER :dbname ;
    GRANT ALL PRIVILEGES ON DATABASE :dbname to :dbname ;
    \c :dbname ;
    create extension hstore;
    \q

Backup the database: postgres user

::

    pg_dump -h localhost -U maplet maplet   > pg_maplet-`date +%F`.sql

Import the database backup file: postgres user

::

    psql -d maplet -U maplet -f pg_maplet-xx.sql
