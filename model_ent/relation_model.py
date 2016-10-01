# -*- coding:utf-8 -*-

from torcms.core import tools
from .core_tab import CabPost as g_Post
from .core_tab import  CabRelation


class MRelation():
    def __init__(self):
        self.tab_relation = CabRelation
        self.tab_post = g_Post
        try:
            CabRelation.create_table()
        except:
            pass

    def add_relation(self, app_f, app_t, weight = 1):
        cur = self.tab_relation.select().where((self.tab_relation.app_f == app_f) & (self.tab_relation.app_t == app_t))
        if cur.count() > 1:
            for x in cur:
                self.delete(x.uid)

        if cur.count() == 0:
            uid = tools.get_uuid()
            entry = self.tab_relation.create(
                uid=uid,
                app_f=app_f,
                app_t=app_t,
                count=1,
            )
            return entry.uid
        elif cur.count() == 1:
            self.update_relation(app_f, app_t, weight)
        else:
            return False


    def delete(self, uid_base, uid_rel):
        entry = self.tab_relation.delete().where(
            (self.tab_relation.app_f == uid_base) & (self.tab_relation.app_t == uid_rel))
        entry.execute()

    def update_relation(self, app_f, app_t, weight = 1):
        try:
            uu = self.tab_relation.get((self.tab_relation.app_f == app_f) & (self.tab_relation.app_t == app_t))
        except:
            return False
        entry = self.tab_relation.update(
            count=uu.count + weight
        ).where((self.tab_relation.app_f == app_f) & (self.tab_relation.app_t == app_t))
        entry.execute()

    def get_app_relations(self, app_id, num=20):
        '''
        The the related infors.
        '''
        x = self.tab_relation.select().join(self.tab_post).where(self.tab_relation.app_f == app_id).order_by(
            self.tab_relation.count.desc()).limit(num)
        return x
