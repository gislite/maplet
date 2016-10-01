# -*- coding:utf-8 -*-
from .core_tab import CabLink
from torcms.model.supertable_model import MSuperTable


class MLink(MSuperTable):
    def __init__(self):
        self.tab = CabLink
        try:
            self.tab.create_table()
        except:
            pass

    def update(self, uid, post_data):
        entry = CabLink.update(
            name=post_data['name'][0],
            link=post_data['link'][0],
            order=post_data['order'][0],
            logo=post_data['logo'][0] if 'logo' in post_data else '',
        ).where(CabLink.uid == uid)
        entry.execute()

    def insert_data(self, id_link, post_data):
        uu = self.get_by_id(id_link)
        if uu:
            return (False)
        entry = self.tab.create(
            name=post_data['name'][0],
            link=post_data['link'][0],
            order=post_data['order'][0],
            logo=post_data['logo'][0] if 'logo' in post_data else '',
            uid=id_link,
        )
        return (id_link)

    def query_link(self, num):
        return self.tab.select().limit(num).order_by(self.tab.order)
