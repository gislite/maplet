# -*- coding: UTF-8 -*-

import sys, os
import html2text
import tornado.escape


import config
from whoosh.index import create_in, open_dir
from whoosh.fields import *
from whoosh.qparser import QueryParser

from jieba.analyse import ChineseAnalyzer

# sys.path.append('/opt/torlite/yunsuan')

from torcms.torlite.model.mpost import MPost
from torcms.applite.model.app_model import MApp
# from applite.model.chart_model import MChart
# from ext_model import MApp
# from tormap.model.app_model import MMapApp

def do_for_post(writer):
    mpost = MPost()
    # 下面这个在 PostgreSQL 中出错
    # recs = mpost.query_all()
    recs = mpost.query_recent(2000)
    for rec in recs:
        text2 = html2text.html2text(tornado.escape.xhtml_unescape(rec.cnt_html))
        print(text2)
        writer.add_document(
            title=rec.title,
            type='<span style="color:green;" class="glyphicon glyphicon-list-alt">[文档]</span>',
            link='/post/{0}.html'.format(rec.uid),
            content=text2
        )
def do_for_app(writer):
    mapp = MApp()
    app_recs = mapp.query_recent(2000)
    for rec in app_recs:
        text2 = html2text.html2text(tornado.escape.xhtml_unescape(rec.cnt_html))
        print(text2)
        writer.add_document(
            title=rec.title,
            type='<span style="color:blue;" class="glyphicon glyphicon-map-marker">[地图]</span>',
            link='/map/{0}'.format(rec.uid),
            content=text2
        )

# def do_for_chart(writer):
#     mchart = MChart()
#     app_recs = mchart.query_all()
#     for rec in app_recs:
#         text2 = html2text.html2text(tornado.escape.xhtml_unescape(rec.cnt_html))
#         print(text2)
#         writer.add_document(
#             title=rec.title,
#             type='<span style="color:#336666;" class="glyphicon glyphicon-stats">[数据地图]</span>',
#             link='/chart/{0}'.format(rec.uid),
#             content=text2
#         )
def gen_whoosh_database():
    whoosh_db = 'database/whoosh'
    if not os.path.exists(whoosh_db):
        os.makedirs(whoosh_db)
    analyzer = ChineseAnalyzer()
    schema = Schema(title=TEXT(stored=True, analyzer=analyzer), type=TEXT(stored=True), link=ID(stored=True),
                    content=TEXT(stored=True, analyzer=analyzer))
    ix = create_in(whoosh_db, schema)
    writer = ix.writer()
    do_for_app(writer)
    # do_for_chart(writer)
    do_for_post(writer)
    writer.commit()

if __name__ == '__main__':
    gen_whoosh_database()
