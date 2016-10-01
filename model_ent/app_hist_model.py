# -*- coding:utf-8 -*-


from torcms.core import tools
from .core_tab import TabAppHist
from .supertable_model import MSuperTable


class MAppHist(MSuperTable):
    def __init__(self):
        self.tab = TabAppHist
        try:
            TabAppHist.create_table()
        except:
            pass

    def insert_data(self, raw_data):
        uid = tools.get_uuid()
        TabAppHist.create(
                uid=uid,
                title=raw_data.title,
                keywords = raw_data.keywords,
                user_name = raw_data.user_name,
                logo = raw_data.logo,
                date = raw_data.date,
                run_count = raw_data.run_count,
                view_count = raw_data.view_count,
                run_time = raw_data.run_time,
                create_time = raw_data.create_time,
                time_update = raw_data.time_update,
                type = raw_data.type,
                html_path = raw_data.html_path,
                cnt_md = raw_data.cnt_md,
                app_id = raw_data.uid,
                valid = raw_data.valid,
                extinfo = raw_data.extinfo,
            )
        return True
