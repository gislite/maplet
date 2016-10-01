# -*- coding:utf-8 -*-

import time
from .core_tab import CabPic as  g_image
from .supertable_model import MSuperTable

class MEntity(MSuperTable):
    def __init__(self):
        self.tab = g_image
        try:
            g_image.create_table()
        except:
            pass

    def getall(self):
        return g_image.select()

    def get_id_by_impath(self, imgpath):
        uu = g_image.select().where(g_image.imgpath == imgpath)
        if uu.count() == 1:
            return uu.get().uid
        elif uu.count() > 1:
            return False
        else:
            return False

    def insert_data(self, signature, impath):
        entry = g_image.create(
            uid=signature,
            imgpath=impath,
            create_timestamp=time.time()
        )
        return entry
