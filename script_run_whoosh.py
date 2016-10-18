# -*- coding:utf-8 -*-

import os
import html2text
import tornado.escape
from whoosh.index import create_in
from whoosh.fields import *
from jieba.analyse import ChineseAnalyzer
from torcms.model.post_model import MPost
from torcms.model.info_model import MInfor

from torcms.script.script_whoosh import gen_whoosh_database


if __name__ == '__main__':
    if len(sys.argv) > 1:
        rand = False
    else:
        rand = True
    post_type = {
        'doc_type': '<span style="color:green;" class="glyphicon glyphicon-list-alt">[{0}]</span>'.format('文档'),
        'info_type': '<span style="color:blue;" class="glyphicon glyphicon-map-marker">[{0}]</span>'.format('地图'),
    }
    gen_whoosh_database(if_rand = rand , post_type = post_type)
