# -*- coding:utf-8 -*-

from playhouse.postgres_ext import PostgresqlExtDatabase
foo_dic = {
        '01': '21',
        '03': '23',
        '05': '25',
        '07': '27',
        '09': '29',
        '11': '31',
        '13': '33',

        }

menu_arr = [['首页', '/'],
            ['文档', '/category/geography'],
            ['云算', '/calc/find'],
            ['专题', '/spec/'],
            ]

page_num = 2

site_name = 'TorCMS网站'
site_url = 'http://127.0.0.1:8088'

wcs_svr = ''
# 使用DataBase的不同形式，以应对Peewe针对数据库的不同语法
# 1 for SQLite
# 2 for MySQL
# 3 for PostgreSQL
dbtype = 3

cookie_secret = '12345'
redis_kw = 'lsadfkj'

smtp_cfg = {
    'host': "smtp.ym.163.com",
    'user': "user_name@yunsuan.org",
    'pass': "password_here",
    'postfix': 'yunsuan.org',
}

# Used in HTML render files.
cfg = {
    'app_url_name': 'map',
    'DEBUG': True,
    'PORT': '8088',
    'site_type': 2,
}


dbconnect = PostgresqlExtDatabase(
    'maplet',
    user='maplet',
    password='131322',
    host='127.0.0.1',
    autocommit=True,
    autorollback=True
)

router_post = {'1': 'post',
               '2': 'map',
               }
