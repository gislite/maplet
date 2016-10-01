# -*- coding:utf-8 -*-


from torcms.core import tools
from .core_tab import CabPageHist
from .supertable_model import MSuperTable


class MPageHist(MSuperTable):
    def __init__(self):
        self.tab = CabPageHist
        try:
            CabPageHist.create_table()
        except:
            pass

    def insert_data(self, raw_data):
        uid = tools.get_uuid()

        CabPageHist.create(
                uid=uid,
                title=raw_data.title,
                # date=raw_data.date,
                page_slug=raw_data.slug,
                # time_create=raw_data.time_create,
                user_name=raw_data.id_user,
                cnt_md=raw_data.cnt_md,
                time_update=raw_data.time_update,

            )
        return True
