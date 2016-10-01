# -*- coding:utf-8 -*-


from torcms.core import tools
from .core_tab import CabPostHist as g_PostHist
from .supertable_model import MSuperTable


class MPostHist(MSuperTable):
    def __init__(self):
        self.tab = g_PostHist
        try:
            g_PostHist.create_table()
        except:
            pass

    def query_by_postid(self, postid):
        recs = self.tab.select().where(self.tab.post_id == postid)
        return recs
    def insert_data(self, raw_data):

        uid = tools.get_uuid()

        g_PostHist.create(
            uid=uid,
            title=raw_data.title,
            # date=raw_data.date,
            post_id=raw_data.uid,
            # time_create=raw_data.time_create,
            user_name=raw_data.user_name,
            cnt_md=raw_data.cnt_md,
            time_update=raw_data.time_update,
            # id_spec=raw_data.id_spec,
            logo=raw_data.logo,
        )

        return True
