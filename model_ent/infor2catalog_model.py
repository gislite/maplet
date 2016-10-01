# -*- coding:utf-8 -*-


from .core_tab import TabApp, TabCatalog, TabApp2Catalog
from .post2catalog_model import MPost2Catalog


class MInfor2Catalog(MPost2Catalog):
    def __init__(self):
        self.tab_post2catalog = TabApp2Catalog
        self.tab_catalog = TabCatalog
        self.tab_post = TabApp
        try:
            TabApp2Catalog.create_table()
        except:
            pass

    def query_all(self, limit_num = 50, by_uid = 'False'):
        return  self.tab_post2catalog.select()