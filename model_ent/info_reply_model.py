# -*- coding:utf-8 -*-

from .core_tab import TabApp2Reply
from .post2reply_model import MPost2Reply


class MInfor2Reply(MPost2Reply):
    def __init__(self):
        self.tab = TabApp2Reply
        try:
            TabApp2Reply.create_table()
        except:
            pass
