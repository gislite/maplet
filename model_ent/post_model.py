# -*- coding:utf-8 -*-

import datetime
import time

import peewee
import tornado.escape
import config
from torcms.core import tools
from .core_tab import CabPost
from .core_tab import CabPost2Catalog as CabPost2Tag
from .supertable_model import MSuperTable


class MPost(MSuperTable):
    def __init__(self):
        self.tab = CabPost
        try:
            CabPost.create_table()
        except:
            pass

    def update(self, uid, post_data, update_time=False):
        title = post_data['title'][0].strip()
        if len(title) < 2:
            return False
        cnt_html = tools.markdown2html(post_data['cnt_md'][0])
        try:
            if update_time:
                entry2 = CabPost.update(
                    time_update=time.time()
                ).where(CabPost.uid == uid)
                entry2.execute()
        except:
            pass

        try:
            entry = CabPost.update(
                title=title,
                cnt_html=cnt_html,
                user_name=post_data['user_name'],
                cnt_md=tornado.escape.xhtml_escape(post_data['cnt_md'][0]),
                logo=post_data['logo'][0],
                keywords=post_data['keywords'][0],
            ).where(CabPost.uid == uid)
            entry.execute()
        except:
            return False

    def insert_data(self, id_post, post_data):
        title = post_data['title'][0].strip()
        if len(title) < 2:
            return False

        if len(post_data['title'][0].strip()) == 0:
            return False

        cur_rec = self.get_by_id(id_post)
        if cur_rec:
            return (False)

        entry = CabPost.create(
            title=title,
            date=datetime.datetime.now(),
            cnt_md=tornado.escape.xhtml_escape(post_data['cnt_md'][0]),
            cnt_html=tools.markdown2html(post_data['cnt_md'][0]),
            uid=id_post,
            time_create=time.time(),
            user_name=post_data['user_name'],
            time_update=time.time(),
            view_count=1,
            logo=post_data['logo'][0],
            keywords=post_data['keywords'][0],
        )
        return (entry.uid)

    def query_cat_random(self, cat_id, num=6):
        if cat_id == '':
            return self.query_random(num)
        if config.dbtype == 1 or config.dbtype == 3:
            return CabPost.select().join(CabPost2Tag).where(CabPost2Tag.tag == cat_id).order_by(
                peewee.fn.Random()).limit(num)
        elif config.dbtype == 2:
            return CabPost.select().join(CabPost2Tag).where(CabPost2Tag.tag == cat_id).order_by(
                peewee.fn.Rand()).limit(num)

    def query_recent_edited(self, timstamp):
        return self.tab.select().where(CabPost.time_update > timstamp).order_by(CabPost.time_update.desc())

    def query_recent(self, num=8):
        return self.tab.select().order_by(CabPost.time_update.desc()).limit(num)

    def query_all(self):
        return self.tab.select().order_by(CabPost.time_update.desc())

    def get_num_by_cat(self, cat_str):
        return CabPost.select().where(CabPost.id_cats.contains(',{0},'.format(cat_str))).count()

    def query_keywords_empty(self):
        return CabPost.select().where(CabPost.keywords == '')

    def query_dated(self, num=8):
        return CabPost.select().order_by(CabPost.time_update.asc()).limit(num)

    def query_most_pic(self, num):
        return CabPost.select().where(CabPost.logo != "").order_by(CabPost.view_count.desc()).limit(num)

    def query_cat_recent(self, cat_id, num=8):
        return CabPost.select().join(CabPost2Tag).where(CabPost2Tag.tag == cat_id).order_by(
            CabPost.time_update.desc()).limit(num)

    def query_most(self, num=8):
        return CabPost.select().order_by(CabPost.view_count.desc()).limit(num)

    def query_cat_by_pager(self, cat_str, cureent):
        tt = CabPost.select().where(CabPost.id_cats.contains(str(cat_str))).order_by(
            CabPost.time_update.desc()).paginate(cureent, config.page_num)
        return tt


    def update_view_count_by_uid(self, uid):
        entry = CabPost.update(view_count=CabPost.view_count + 1).where(CabPost.uid == uid)
        try:
            entry.execute()
            return True
        except:
            return False

    def update_keywords(self, uid, inkeywords):
        entry = CabPost.update(keywords=inkeywords).where(CabPost.uid == uid)
        entry.execute()


    def get_next_record(self, in_uid):
        current_rec = self.get_by_id(in_uid)
        query = CabPost.select().where(CabPost.time_update < current_rec.time_update).order_by(
            CabPost.time_update.desc())
        if query.count() == 0:
            return None
        else:
            return query.get()

    def get_previous_record(self, in_uid):
        current_rec = self.get_by_id(in_uid)
        query = CabPost.select().where(CabPost.time_update > current_rec.time_update).order_by(CabPost.time_update)
        if query.count() == 0:
            return None
        else:
            return query.get()
