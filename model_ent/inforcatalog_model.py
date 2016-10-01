# -*- coding:utf-8 -*-

from .core_tab import TabCatalog
from .postcatalog_model import MPostCatalog

class MInforCatalog(MPostCatalog):
    def __init__(self):
        self.tab = TabCatalog
        try:
            TabCatalog.create_table()
        except:
            pass

