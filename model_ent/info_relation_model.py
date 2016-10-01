# -*- coding:utf-8 -*-

from .core_tab import TabApp, TabAppRelation, RabPost2App, RabApp2Post, CabPost
from .relation_model import MRelation


class MInforRel(MRelation):
    def __init__(self):
        self.tab_relation = TabAppRelation
        self.tab_post = TabApp
        try:
            TabAppRelation.create_table()
        except:
            pass


class MRelPost2Infor(MRelation):
    def __init__(self):
        MRelation.__init__(self)
        self.tab_relation = RabPost2App
        self.tab_post = TabApp
        try:
            self.tab_relation.create_table()
        except:
            pass


class MRelInfor2Post(MRelation):
    def __init__(self):
        MRelation.__init__(self)
        self.tab_relation = RabApp2Post
        self.tab_post = CabPost
        try:
            self.tab_relation.create_table()
        except:
            pass
