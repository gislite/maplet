# -*- coding:utf-8 -*-

import time
from datetime import datetime

import config
from config import cfg
import peewee
from torcms.core import tools
from .supertable_model import MSuperTable
from .core_tab import TabApp, TabApp2Catalog, TabApp2Label, TabUsage, TabAppRelation, TabApp2Reply, CabReply


class MInforBase(MSuperTable):
    def __init__(self):
        self.tab_app = TabApp
        self.tab_app2catalog = TabApp2Catalog
        self.tab_relation = TabAppRelation
        self.tab_app2label = TabApp2Label
        self.tab_usage = TabUsage
        self.tab_app2reply = TabApp2Reply
        self.cab_reply = CabReply
        try:
            TabApp.create_table()
        except:
            pass

    def get_all(self):
        return (self.tab_app.select().where(self.tab_app.valid == 1).order_by(self.tab_app.time_update.desc()))

    def update_jsonb(self, uid, extinfo):
        cur_extinfo = self.get_by_uid(uid).extinfo
        for key in extinfo:
            cur_extinfo[key] = extinfo[key]
        entry = self.tab_app.update(
            extinfo=cur_extinfo,
        ).where(self.tab_app.uid == uid)
        entry.execute()
        return (uid)

    def delete(self, del_id):
        u1 = self.tab_app2catalog.delete().where(self.tab_app2catalog.post == del_id)
        u1.execute()
        u2 = self.tab_relation.delete().where(self.tab_relation.app_f == del_id)
        u2.execute()
        u3 = self.tab_relation.delete().where(self.tab_relation.app_t == del_id)
        u3.execute()
        u4 = self.tab_app2label.delete().where(self.tab_app2label.app == del_id)
        u4.execute()
        u5 = self.tab_usage.delete().where(self.tab_usage.signature == del_id)
        u5.execute()

        reply_arr = []
        for reply in self.tab_app2reply.select().where(self.tab_app2reply.post_id == del_id):
            reply_arr.append(reply.reply_id.uid)

        u6 = self.tab_app2reply.delete().where(self.tab_app2reply.post_id == del_id)
        u6.execute()

        for replyid in reply_arr:
            self.cab_reply.delete().where(self.cab_reply.uid == replyid).execute()

        uu = self.tab_app.delete().where(self.tab_app.uid == del_id)
        uu.execute()
        return True

    def modify_meta(self, app_id, data_dic):
        '''
        手工修改的。
        :param uid:
        :param data_dic:
        :return:
        '''
        pass

    def modify_init(self, uid, data_dic):
        '''
        命令行更新的
        :param uid:
        :param data_dic:
        :return:
        '''
        entry = self.tab_app.update(
            time_update=int(time.time()),
            # html_path=data_dic['html_path'],
            date=datetime.now(),
            keywords=data_dic['keywords'],
            type=data_dic['type'],
        ).where(self.tab_app.uid == uid)
        entry.execute()
        return (uid)

    def get_view_count(self, sig):
        try:
            return self.tab_app.get(uid=sig).view_count
        except:
            return False

    def view_count_increase(self, uid):
        infor = self.get_by_uid(uid)
        entry = self.tab_app.update(
            view_count=infor.view_count + 1,
        ).where(self.tab_app.uid == uid)
        entry.execute()

    def get_run_count(self, sig):
        try:
            return self.tab_app.get(uid=sig).run_count
        except:
            return False

    def run_count_increase(self, uid):

        entry = self.tab_app.update(
            run_count=self.get_run_count(uid) + 1,
        ).where(self.tab_app.uid == uid)
        entry.execute()

    def query_random(self, num=8):
        fn = peewee.fn
        if config.dbtype == 1 or config.dbtype == 3:
            return self.tab_app.select().where(self.tab_app.valid == 1).order_by(fn.Random()).limit(num)
        elif config.dbtype == 2:
            return self.tab_app.select().where(self.tab_app.valid == 1).order_by(fn.Rand()).limit(num)

    def query_cat_random(self, catid, num=8):
        fn = peewee.fn
        if config.dbtype == 1 or config.dbtype == 3:
            return self.tab_app.select().join(self.tab_app2catalog).where((self.tab_app.valid == 1) & (self.tab_app2catalog.catalog == catid)).order_by(fn.Random()).limit(num)
        elif config.dbtype == 2:
            return self.tab_app.select().where(self.tab_app.valid == 1).order_by(fn.Rand()).limit(num)

    def query_most(self, num=8):
        return self.tab_app.select().where(self.tab_app.valid == 1).order_by(self.tab_app.view_count.desc()).limit(num)

    def query_most_by_cat(self, num=8, catid=None):
        if catid:
            return self.tab_app.select().join(self.tab_app2catalog).where((self.tab_app.valid == 1) &
                                                                      (
                                                                          self.tab_app2catalog.catalog == catid)).order_by(
            self.tab_app.view_count.desc()).limit(num)
        else:
            return False

    def query_least_by_cat(self, num=8, cat_str=1):
        return self.tab_app.select().join(self.tab_app2catalog).where((self.tab_app.valid == 1) &
                                                                      (
                                                                          self.tab_app2catalog.catalog == cat_str)).order_by(
            self.tab_app.view_count).limit(num)

    def get_by_keyword(self, par2):
        return self.tab_app.select().where((self.tab_app.valid == 1)
                                           & (self.tab_app.title.contains(par2))).order_by(
            self.tab_app.time_update.desc()).limit(20)

    def query_recent(self, num=8):
        return self.tab_app.select().where(self.tab_app.valid == 1).order_by(self.tab_app.time_update.desc()).limit(num)

    def get_by_uid(self, sig):
        try:
            return self.tab_app.get(uid=sig)
        except:
            return False


class MInfor(MInforBase):
    def __init__(self):
        self.tab = TabApp
        self.tab_app = TabApp
        self.tab_app2catalog = TabApp2Catalog
        self.tab_relation = TabAppRelation
        self.tab_app2label = TabApp2Label
        self.tab_usage = TabUsage
        self.tab_app2reply = TabApp2Reply
        self.cab_reply = CabReply
        try:
            TabApp.create_table()
        except:
            pass

    def modify_meta(self, uid, data_dic, extinfo={}):
        '''
        手工修改的。
        :param uid:
        :param data_dic:
        :return:
        '''
        title = data_dic['title'][0].strip()
        if len(title) < 2:
            return False

        cur_info = self.get_by_uid(uid)
        if cur_info:
            cur_extinfo = cur_info.extinfo
            # Update the extinfo, Not replace
            for key in extinfo:
                cur_extinfo[key] = extinfo[key]
            entry = self.tab_app.update(
                title=title,
                keywords=','.join([x.strip() for x in data_dic['keywords'][0].strip().strip(',').split(',')]),
                time_update=int(time.time()),
                date=datetime.now(),
                cnt_md=data_dic['cnt_md'][0],
                logo=data_dic['logo'][0],
                cnt_html=tools.markdown2html(data_dic['cnt_md'][0]),
                extinfo=cur_extinfo,
                valid = data_dic['valid'],
            ).where(self.tab_app.uid == uid)
            entry.execute()
        else:

            entry = self.add_meta(uid, data_dic, extinfo)
            return entry
        return (uid)

    def query_extinfo_by_cat(self, cat_id):
        return self.tab_app.select().where(
            (self.tab_app.valid == 1) & (self.tab_app.extinfo['def_cat_uid'] == cat_id)).order_by(
            self.tab_app.time_update.desc())

    def query_by_tagname(self, tag_name):
        return self.tab_app.select().where(
            (self.tab_app.valid == 1) & (self.tab_app.extinfo['def_tag_arr'].contains(tag_name))).order_by(
            self.tab_app.time_update.desc())

    def get_label_fenye(self, tag_slug, page_num):
        all_list = self.query_by_tagname(tag_slug)

        # 当前分页的记录
        current_list = all_list[(page_num - 1) * cfg['info_per_page']: (page_num) * cfg['info_per_page']]
        return (all_list)

    def add_meta(self, uid, data_dic, extinfo={}):
        title = data_dic['title'][0].strip()
        if len(title) < 2 or len(title) == 0:
            return False
        entry = self.tab_app.create(
            uid=uid,
            title=title,
            keywords=','.join([x.strip() for x in data_dic['keywords'][0].split(',')]),
            create_time=int(time.time()),
            date=datetime.now(),
            cnt_md=data_dic['cnt_md'][0],
            logo=data_dic['logo'][0],
            cnt_html=tools.markdown2html(data_dic['cnt_md'][0]),
            extinfo=extinfo,
            user_name=data_dic['user_name'],
            valid=data_dic['valid'],
        )
        return (entry.uid)

    def get_list(self, condition):
        db_data = self.tab_app.select().where(
            (self.tab_app.valid == 1) & (self.tab_app.extinfo.contains(condition))).order_by(
            self.tab_app.time_update.desc())
        return (db_data)

    def get_num_condition(self, con):

        return self.get_list(con).count()

    def modify_init(self, uid, data_dic):
        '''
        命令行更新的
        :param uid:
        :param data_dic:
        :return:
        '''
        entry = self.tab_app.update(
            time_update=int(time.time()),
            # html_path=data_dic['html_path'],
            date=datetime.now(),
            type=data_dic['type'],
        ).where(self.tab_app.uid == uid)
        entry.execute()
        return (uid)

    def addata_init(self, data_dic, ext_dic={}):
        title = data_dic['title'][0].strip()
        if len(title) < 2:
            return False

        if self.get_by_uid(data_dic['sig']):
            uu = self.get_by_uid(data_dic['sig'])
            if data_dic['title'] == uu.title and data_dic['type'] == uu.type:
                pass
            else:
                self.modify_init(data_dic['sig'], data_dic)
        else:
            time_stamp = int(time.time())

            entry = self.tab_app.create(
                uid=data_dic['sig'],
                title=data_dic['title'],
                create_time=time_stamp,
                time_update=time_stamp,
                cnt_md=data_dic['cnt_md'],
                cnt_html=data_dic['cnt_html'],
                date=datetime.now(),
                keywords=data_dic['keywords'],
                extinfo=ext_dic
            )

    def get_list_fenye(self, tag_slug, page_num):

        all_list = self.get_list(tag_slug)
        current_list = all_list[(page_num - 1) * cfg['info_per_page']: (page_num) * cfg['info_per_page']]
        return (current_list)

    def get_cat_recs_count(self, catid):
        '''
        获取某一分类下的数目
        '''
        condition = {'catid': [catid]}

        db_data = self.tab_app.select().where((self.tab_app.valid == 1) & (self.tab_app.extinfo.contains(condition)))
        return db_data.count()
