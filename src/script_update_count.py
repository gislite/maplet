# -*- coding:utf-8 -*-
__author__ = 'bukun'

from torcms.applite.model.app2catalog_model import MApp2Catalog
from torcms.applite.model.mappcatalog import MAppCatalog
from torcms.torlite.model.mcatalog import MCatalog
from torcms.torlite.model.mpost2catalog import MPost2Catalog

if __name__ == '__main__':
    mapp2tag = MApp2Catalog()
    mpost2cat = MPost2Catalog()
    mcat = MCatalog()
    mappcat = MAppCatalog()
    for x in mapp2tag.query_count():
        print(x.catalog.uid, x.num)
        mappcat.update_app_catalog_num(x.catalog.uid, x.num)

    for x in mpost2cat.query_count():
        print(x.catalog.uid, x.num)
        mcat.update_post_catalog_num(x.catalog.uid, x.num)
