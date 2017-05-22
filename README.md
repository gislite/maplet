## Maplet


Source codes for http://www.maplet.org

## Database


    \set dbname maplet
    CREATE USER :dbname WITH PASSWORD '131322' ;
    CREATE DATABASE :dbname OWNER :dbname ;
    GRANT ALL PRIVILEGES ON DATABASE :dbname to :dbname ;
    \c :dbname ;
    create extension hstore;
    \q