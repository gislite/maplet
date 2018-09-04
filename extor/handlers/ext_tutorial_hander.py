import json

from torcms.core.base_handler import BaseHandler
from torcms.model.category_model import MCategory

class TutorialIndexHandler(BaseHandler):
    def initialize(self, **kwargs):
        super(TutorialIndexHandler, self).initialize()
        self.kind = kwargs.get('kind', 'k')

    def get(self, *args, **kwargs):
        url_str = args[0]
        url_arr = self.parse_url(url_str)

        if url_str == '' or url_str == 'list':
            self.list()

        else:
            self.show404()

    def list(self):
        '''
        The default page of POST.
        '''
        post_data = self.get_post_data()
        cat_slug = post_data.get('slug', '')
        cat_rec = MCategory.get_by_slug(cat_slug)
        if not cat_rec:
            return False
        kwd = {
            'uid': '',
            'cat_slug': cat_slug,
            'cat_name' : cat_rec.name,
            'page_slug':cat_slug + "_index"

        }
        self.render('post_{0}/post_list.html'.format(self.kind),
                    userinfo=self.userinfo,
                    catinfo=cat_rec,
                    kwd=kwd
                    )
