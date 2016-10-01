# -*- coding:utf-8 -*-

import peewee

import config
from torcms.core import tools
from .core_tab import CabCatalog, CabPost, CabPost2Catalog
from .supertable_model import MSuperTable


class MPost2Catalog(MSuperTable):
    def __init__(self):
        self.tab_post2catalog = CabPost2Catalog
        self.tab_catalog = CabCatalog
        self.tab_post = CabPost
        self.tab = CabPost2Catalog
        try:
            CabPost2Catalog.create_table()
        except:
            pass

    def remove_relation(self, post_id, tag_id):
        entry = self.tab_post2catalog.delete().where(
            (self.tab_post2catalog.post == post_id) & (self.tab_post2catalog.catalog == tag_id))
        entry.execute()

    def query_by_catid(self, catid):
        return self.tab_post2catalog.select().where(self.tab_post2catalog.catalog == catid)

    def __get_by_info(self, post_id, catalog_id):
        recs = self.tab_post2catalog.select().where(
            (self.tab_post2catalog.post == post_id) & (self.tab_post2catalog.catalog == catalog_id))
        if recs.count() > 1:
            for rec in recs:
                self.delete(rec.uid)
            return False
        elif recs.count() == 1:
            return self.tab_post2catalog.get(
                (self.tab_post2catalog.post == post_id) & (self.tab_post2catalog.catalog == catalog_id))
        else:
            return False

    def query_count(self):
        recs = self.tab_post2catalog.select(self.tab_post2catalog.catalog,
                                            peewee.fn.COUNT(self.tab_post2catalog.catalog).alias('num')).group_by(
            self.tab_post2catalog.catalog)
        return (recs)

    def add_record(self, post_id, catalog_id, order=0):
        tt = self.__get_by_info(post_id, catalog_id)
        if tt:
            entry = self.tab_post2catalog.update(
                order=order,
            ).where(self.tab_post2catalog.uid == tt.uid)
            entry.execute()
        else:

            try:
                self.tab_post2catalog.create(
                    uid=tools.get_uuid(),
                    post=post_id,
                    catalog=catalog_id,
                    order=order,
                )
                return True
            except:
                return False

    def count_of_certain_category(self, cat_id):
        return self.tab_post2catalog.select().where(self.tab_post2catalog.catalog == cat_id).count()


    def query_pager_by_slug(self, slug, current_page_num=1):
        recs =  self.tab_post.select().join(self.tab_post2catalog).join(self.tab_catalog).where(
            self.tab_catalog.slug == slug).order_by(
            self.tab_post.time_update.desc()).paginate(current_page_num, config.page_num)
        return  recs

    def query_by_entity_uid(self, idd):
        return self.tab_post2catalog.select().join(self.tab_catalog).where(self.tab_post2catalog.post == idd).order_by(
            self.tab_post2catalog.order)

    def query_by_id(self, idd):
        return self.query_by_entity_uid(idd)

    def query_entity_category_relation(self, post_id):
        return self.tab_post2catalog.select().where(self.tab_post2catalog.post == post_id).order_by(self.tab_post2catalog.order)

    def get_entry_catalog(self, app_uid):
        uu = self.query_entity_category_relation(app_uid)
        if uu.count() > 0:
            return uu.get()
        else:
            return False
