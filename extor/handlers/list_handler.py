# -*- coding:utf-8 -*-

'''
Accessing via category.
'''

import json
from html2text import html2text

from torcms.core.base_handler import BaseHandler
from torcms.core import tools
from torcms.model.category_model import MCategory
from torcms.model.catalog_model import MCatalog
from torcms.model.post2catalog_model import MPost2Catalog
from torcms.handlers.list_handler import ListHandler
from config import CMS_CFG, router_post


class ExtListHandler(ListHandler):


    def list_catalog(self, cat_slug, **kwargs):
        '''
        listing the posts via category
        '''
        post_data = self.get_post_data()
        tag = post_data.get('tag', '')

        def get_pager_idx():
            '''
            Get the pager index.
            '''
            cur_p = kwargs.get('cur_p')
            the_num = int(cur_p) if cur_p else 1
            the_num = 1 if the_num < 1 else the_num
            return the_num

        current_page_num = get_pager_idx()
        cat_rec = MCategory.get_by_slug(cat_slug)
        if not cat_rec:
            return False

        num_of_cat = MPost2Catalog.count_of_certain_category(cat_rec.uid, tag=tag)

        page_num = int(num_of_cat / CMS_CFG['list_num']) + 1
        cat_name = cat_rec.name
        kwd = {'cat_name': cat_name,
               'cat_slug': cat_slug,
               'title': cat_name,
               'router': router_post[cat_rec.kind],
               'current_page': current_page_num,
               'kind': cat_rec.kind,
               'tag': tag}


        # Todo: review the following codes.


        if self.order:
            tmpl = 'list/catalog_list.html'
        else:
            tmpl = 'list/category_list_{0}.html'.format(cat_rec.kind)

        infos = MPost2Catalog.query_pager_by_slug(
            cat_slug,
            current_page_num,
            tag=tag,
            order=self.order
        )

        # ToDo: `gen_pager_purecss` should not use any more.
        self.render(tmpl,
                    catinfo=cat_rec,
                    infos=infos,
                    pager=tools.gen_pager_purecss(
                        '/list/{0}'.format(cat_slug),
                        page_num,
                        current_page_num),
                    userinfo=self.userinfo,
                    html2text=html2text,
                    cfg=CMS_CFG,
                    kwd=kwd,
                    router=router_post[cat_rec.kind])

