# -*- coding:utf-8 -*-

from maplet.model.map_tab import *
from torcms.core import tools


class MLayout(object):
    def __init__(self):
        try:
            TabLayout.create_table()
        except:
            pass

    def get_by_id(self, uid):
        try:
            return TabLayout.get(TabLayout.uid == uid)
        except:
            return False

    def delete_by_uid(self, uid):
        q = TabLayout.delete().where( TabLayout.uid == uid)
        try:
            q.execute()
        except:
            return False
        
    def query_recent(self, user_id, num=10):
        return TabLayout.select().where(TabLayout.user == user_id).order_by(TabLayout.order).limit(num)

    def query_by_app(self, app_id, user_id):
        return TabLayout.select().where((TabLayout.app == app_id) & (TabLayout.user == user_id)).order_by(
            TabLayout.time_update.desc())

    def add_or_update(self, post_data):
        print(post_data)
        entry = TabLayout.create(
            uid=tools.get_uu8d(),
            title='',
            app=post_data['map'][0],
            user=post_data['user'],
            json=post_data['geojson'][0] if 'geojson' in post_data else '',
            lon=float(post_data['lon'][0]),
            lat=float(post_data['lat'][0]),
            zoom=int(post_data['zoom'][0]),
            marker=int(post_data['marker'][0]) if 'marker' in post_data else 0,
            time_create=tools.timestamp(),
            time_update=tools.timestamp(),
            public=1,
        )
        print(entry)
