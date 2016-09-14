# -*- coding: UTF-8 -*-

import sys, os
import html2text
import tornado.escape

from time import sleep
import config
from whoosh.index import create_in, open_dir
from whoosh.fields import *
from whoosh.qparser import QueryParser

from jieba.analyse import ChineseAnalyzer

from torcms.model.post_model import MPost
from torcms.model.infor_model import MInfor as MApp

def do_for_app(writer, rand = True):
    mpost = MApp()
    if rand:
        recs = mpost.query_random(50)
    else:
        recs = mpost.query_recent(50)
    doc_type = '<span style="color:blue;" class="glyphicon glyphicon-map-marker">[{0}]</span>'.format('地图')
    print(recs.count())
    for rec in recs:
        # # sleep(0.1)
        text2 = rec.title +',' +  html2text.html2text(tornado.escape.xhtml_unescape(rec.cnt_html))
        # writer.update_document(path=u"/a",content="Replacement for the first document")
        writer.update_document(
            title=rec.title,
            type= doc_type,
            link='/map/{0}'.format(rec.uid),
            content=text2
        )



def do_for_post(writer, rand = True):
    mpost = MPost()
    if rand:
        recs = mpost.query_random(50)
    else:
        recs = mpost.query_recent(50)
    doc_type = '<span style="color:green;" class="glyphicon glyphicon-list-alt">[{0}]</span>'.format('文档')
    print(recs.count())
    for rec in recs:
        # sleep(0.1)
        text2 = rec.title +',' + html2text.html2text(tornado.escape.xhtml_unescape(rec.cnt_html))
        # writer.update_document(path=u"/a",content="Replacement for the first document")
        writer.update_document(
            title=rec.title,
            type= doc_type,
            link='/post/{0}.html'.format(rec.uid),
            content=text2
        )


def gen_whoosh_database( if_rand = True):

    analyzer = ChineseAnalyzer()
    schema = Schema(title=TEXT(stored=True, analyzer=analyzer),
                    type=TEXT(stored=True),
                    link=ID(unique=True, stored=True, ),
                    content=TEXT(stored=True, analyzer=analyzer))
    whoosh_db = 'database/whoosh'
    if not os.path.exists(whoosh_db):
        os.makedirs(whoosh_db)
        ix = create_in(whoosh_db, schema)
    else :
        ix = open_dir(whoosh_db)

    writer = ix.writer()
    do_for_app(writer, rand = if_rand)
    do_for_post(writer, rand = if_rand)
    print('-' * 10)
    writer.commit()

if __name__ == '__main__':
    if len(sys.argv) > 1:
        rand = False
    else:
        rand = True
    print(rand)
    gen_whoosh_database(if_rand = rand )
