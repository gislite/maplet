# -*- coding:utf-8 -*-

from maplet.model.map_tab import *
from torcms.core import tools


class MJson(object):
    def __init__(self):
        try:
            TabJson.create_table()
        except:
            pass
        try:
            TabApp2Json.create_table()
        except:
            pass

    def get_by_id(self, uid):
        try:
            return TabJson.get(TabJson.uid == uid)
        except:
            return False

    def query_recent(self, user_id, num=10):
        return TabJson.select().where(TabJson.user == user_id).order_by(TabJson.time_update.desc()).limit(num)

    def query_by_app(self, app_id, user_id):
        # return TabMap2Json.select().join(TabJson).where ( (TabMap2Json.app.uid == app_id) & (TabJson.user == user_id) ).order_by(TabJson.time_update.desc())
        return TabJson.select().join(TabApp2Json).where((TabApp2Json.app == app_id) & (TabJson.user == user_id)).order_by(
            TabJson.time_update.desc())


    def delete_by_uid(self, uid):
        q = TabJson.delete().where( TabJson.uid == uid)
        try:
            q.execute()
        except:
            return False

    def add_json(self, json_uid, user_id, geojson):
        current_count = TabJson.select().where(TabJson.uid == json_uid).count()

        if current_count > 0:
            cur_record = self.get_by_id(json_uid)
            entry = TabJson.update(
                json=geojson,
                time_update=tools.timestamp(),
            ).where(TabJson.uid == cur_record.uid)
            entry.execute()

        else:
            entry = TabJson.create(
                uid=json_uid,
                title='',
                # app=app_id,
                user=user_id,
                json=geojson,
                time_create=tools.timestamp(),
                time_update=tools.timestamp(),
                public=1,
            )
    def add_or_update(self, json_uid, user_id, app_id, geojson):
        current_count = TabJson.select().where(TabJson.uid == json_uid).count()
        self.add_json(json_uid, user_id, geojson)

        if current_count:
            pass
        else:
            entry = TabApp2Json.create(
                uid = tools.get_uuid(),
                app = app_id,
                json = json_uid,
            )
