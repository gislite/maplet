# -*- coding:utf-8 -*-

import time

from torcms.core import tools
from .core_tab import TabApp, TabCollect


class MCollect(object):
    def __init__(self):
        self.tab = TabCollect
        self.tab_app = TabApp
        try:
            TabCollect.create_table()
        except:
            pass

    def query_recent(self, user_id, num = 10):
        return self.tab.select().where(self.tab.user == user_id).join(self.tab_app).order_by(self.tab.timestamp.desc()).limit(num)

    def query_most(self, num):
        return self.tab.select().order_by(self.tab.count.desc()).limit(num)

    def get_by_signature(self, user_id, app_id):
        try:
            return self.tab.get((self.tab.user==user_id) & (self.tab.app== app_id))
        except:
            return False

    def add_or_update(self, user_id, app_id):

        if self.get_by_signature(user_id, app_id):
            tt = self.get_by_signature(user_id, app_id)
            entry = self.tab.update(
                timestamp = int(time.time())
            ).where(self.tab.uid == tt.uid)
            entry.execute()
        else:
            entry = self.tab.create(
                uid = tools.get_uuid(),
                user = user_id,
                app  = app_id,
                timestamp = int(time.time()),
            )

