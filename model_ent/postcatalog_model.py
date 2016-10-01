# -*- coding:utf-8 -*-


import config
from .core_tab import CabCatalog
from .supertable_model import MSuperTable

class MPostCatalog(MSuperTable):
    def __init__(self):
        self.tab = CabCatalog
        try:
            CabCatalog.create_table()
        except:
            pass

    def get_qian2(self, qian2):
        '''
        用于首页。根据前两位，找到所有的大类与小类。
        并为方便使用，使用数组的形式返回。
        :param qian2: 分类id的前两位
        :return: 数组，包含了找到的分类
        '''
        return self.tab.select().where(self.tab.uid.startswith(qian2)).order_by(self.tab.order)
    def query_pcat(self):
        return  self.tab.select().where(self.tab.uid.endswith('00')).order_by(self.tab.order)
    def query_uid_starts_with(self, qian2):
        return self.tab.select().where(self.tab.uid.startswith(qian2)).group_by(self.tab.uid).order_by(self.tab.order)

    def query_all(self, by_count=False, by_order=True):
        if by_count:
            recs = self.tab.select().order_by(self.tab.count.desc())
        elif by_order:
            recs = self.tab.select().order_by(self.tab.order)
        else:
            recs = self.tab.select().order_by(self.tab.uid)
        return (recs)

    def query_field_count(self, limit_num):
        return self.tab.select().order_by(self.tab.count.desc()).limit(limit_num)

    def get_by_slug(self, slug):
        uu = self.tab.select().where(self.tab.slug == slug)
        if uu.count() == 1:
            return uu.get()
        elif uu.count() > 1:
            return False
        else:
            return False

    def update_count(self, cat_id, num):
        entry = self.tab.update(
            count=num,
        ).where(self.tab.uid == cat_id)
        entry.execute()

    def initial_db(self, post_data):
        entry = self.tab.create(
            name=post_data['name'],
            id_cat=post_data['id_cat'],
            slug=post_data['slug'],
            order=post_data['order'],
        )
        return (entry)

    def update(self, uid, post_data):

        entry = self.tab.update(
            name=post_data['name'][0],
            slug=post_data['slug'][0],
            order=post_data['order'][0],

        ).where(self.tab.uid == uid)
        entry.execute()

    def insert_data(self, id_post, post_data):

        uu = self.get_by_id(id_post)
        if uu:
            self.update(id_post, post_data)
        else:
            entry = self.tab.create(
                name=post_data['name'][0],
                slug=post_data['slug'][0],
                order=post_data['order'][0],
                uid=id_post,
            )
            return (entry.uid)