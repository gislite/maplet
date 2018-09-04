# -*- coding:utf-8 -*-

import tornado.web
import config


from torcms.model.category_model import MCategory
from torcms.model.post2catalog_model import MPost2Catalog
from extor.model.ext_category_model import MExtCategory




class CatalogPager(tornado.web.UIModule):
    '''
    pager of category
    '''

    def render(self, *args, **kwargs):
        cat_slug = args[0]
        current = int(args[1])
        # cat_slug 分类
        # current 当前页面

        cat_rec = MCategory.get_by_slug(cat_slug)
        num_of_cat = MPost2Catalog.count_of_certain_category(cat_rec.uid)

        tmp_page_num = int(num_of_cat / config.CMS_CFG['list_num'])

        page_num = (tmp_page_num if abs(tmp_page_num - num_of_cat / config.CMS_CFG['list_num']) < 0.1
                    else tmp_page_num + 1)

        kwd = {
            'page_home': False if current <= 1 else True,
            'page_end': False if current >= page_num else True,
            'page_pre': False if current <= 1 else True,
            'page_next': False if current >= page_num else True,
        }

        return self.render_string('modules_ext/post/catalog_pager.html',
                                  kwd=kwd,
                                  cat_slug=cat_slug,
                                  pager_num=page_num,
                                  page_current=current)


class Ext_category_menu(tornado.web.UIModule):
    '''
    Menu for category lists.
    '''

    def render(self, *args, **kwargs):
        kind = kwargs.get('kind', '1')
        catid = kwargs.get('catid', '')
        userinfo = kwargs.get('userinfo', None)
        recs = MExtCategory.query_by_kind(kind)

        return self.render_string('modules_ext/post/showcat_list.html',
                                  recs=recs,
                                  catid=catid,
                                  userinfo=userinfo,
                                  )


class Ext_category_list(tornado.web.UIModule):
    '''
    Menu for category lists.
    '''

    def render(self, *args, **kwargs):
        catid = kwargs.get('catid', '')
        second = kwargs.get('second', True)
        userinfo = kwargs.get('userinfo', None)

        cat_id = catid[:2]
        recs = MCategory.get_qian2(cat_id)
        if second == False:
            return self.render_string('modules_ext/post/showsubcat_list.html',
                                      recs=recs,
                                      catid=catid,
                                      userinfo=userinfo,
                                      )
        else:
            return self.render_string('modules_ext/post/showsubcat_list_second.html',
                                      recs=recs,
                                      catid=catid,
                                      userinfo=userinfo,
                                      )


class ExtPostCategoryOf(tornado.web.UIModule):
    '''
    The catalog of the post.
    '''

    def render(self, uid_with_str, slug=False, order=False, with_title=True, glyph=''):
        curinfo = MCategory.get_by_uid(uid_with_str)
        sub_cats = MCategory.query_sub_cat(uid_with_str)
        kwd = {
            'glyph': glyph
        }

        return self.render_string('modules_ext/info/catalog_of.html',
                                  pcatinfo=curinfo,
                                  sub_cats=sub_cats,
                                  recs=sub_cats,
                                  with_title=with_title,
                                  kwd=kwd)
