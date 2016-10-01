# -*- coding:utf-8 -*-

import config
from torcms.core import tools
from .core_tab import CabLabel
from .core_tab import CabPost
from .core_tab import CabPost2Label
from .supertable_model import MSuperTable


class MLabel(MSuperTable):
    def __init__(self):
        self.tab = CabLabel
        try:
            CabLabel.create_table()
        except:
            pass

    def get_id_by_name(self, tag_name):
        uu = self.tab.select().where(self.tab.name == tag_name)
        if uu.count() == 1:
            return uu.get().uid
        elif uu.count() > 1:
            for x in uu:
                self.delete(x.uid)
        else:
            return self.create_tag(tag_name)

    def create_tag(self, tag_name):

        cur_count = self.tab.select().where(self.tab.name == tag_name).count()
        if cur_count > 0:
            return False


        uid = tools.get_uu8d()
        while self.tab.select().where(self.tab.uid == uid).count() > 0:
            uid = tools.get_uu8d()

        entry = self.tab.create(
            uid=uid,
            name=tag_name,
            count=0
        )
        return entry.uid


class MPost2Label(MSuperTable):
    def __init__(self):
        self.tab = CabPost2Label
        self.tab_label = CabLabel
        self.tab_post = CabPost
        self.mtag = MLabel()
        try:
            CabPost2Label.create_table()
        except:
            pass
    def remove_relation(self, post_id, tag_id):
        entry = self.tab.delete().where((self.tab.app == post_id) & (self.tab.tag == tag_id))
        entry.execute()

    def generate_catalog_list(self, signature):
        tag_infos = self.get_by_id(signature)
        out_str = ''
        for tag_info in tag_infos:
            tmp_str = '<li><a href="/tag/{0}" >{1}</a></li>'.format(tag_info.tag, tag_info.catalog_name)
            out_str += tmp_str
        return out_str



    def get_by_id(self, idd):
        return self.tab.select().join(self.tab_label).where(self.tab.app == idd)

    def get_by_info(self, post_id, catalog_id):
        tmp_recs = self.tab.select().where((self.tab.app == post_id) & (self.tab.tag == catalog_id))

        if tmp_recs.count() > 1:
            ''' 如果多于1个，则全部删除
            '''
            for tmp_rec in tmp_recs:
                self.delete(tmp_rec.uid)
            return False

        elif tmp_recs.count() == 1:
            return tmp_recs.get()
        else:
            return False

    def add_record(self, post_id, tag_name, order=1):
        tag_id = self.mtag.get_id_by_name(tag_name)
        tt = self.get_by_info(post_id, tag_id)
        if tt:
            entry = self.tab.update(
                order=order,
            ).where(self.tab.uid == tt.uid)
            entry.execute()
        else:
            entry = self.tab.create(
                uid=tools.get_uuid(),
                app=post_id,
                tag=tag_id,
                order=order,
            )
            return entry.uid


    def total_number(self, slug):
        return self.tab_post.select().join(self.tab).where(self.tab.tag == slug).count()

    def query_pager_by_slug(self, slug, current_page_num=1):
        return self.tab_post.select().join(self.tab).where(self.tab.tag == slug).paginate(current_page_num,
                                                                                          config.page_num)
