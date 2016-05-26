__author__ = 'bukun'

import sys


from torcms.model.mcatalog import MCatalog
from torcms.model.mappcatalog import MAppCatalog
from torcms.model.app_model import MApp
from torcms.model.app2catalog_model import MApp2Catalog

if __name__ == '__main__':
    mapp2cat = MApp2Catalog()
    mappcat = MAppCatalog()
    mapp = MApp()
    for rec in mappcat.query_all():
        uid= rec.uid
        # uuvv = mapp.query_extinfo_by_cat(uid)
        uuvv = mapp2cat.query_by_catid(rec.uid)
        print(uid, uuvv.count())
        mappcat.update_count(uid, uuvv.count())