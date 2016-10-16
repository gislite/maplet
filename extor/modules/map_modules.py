import random

import tornado.web
import torcms.model.info_model
import torcms.model.infor2catalog_model
from torcms.model.category_model import MCategory as  MInforCatalog
from torcms.model import usage_model
from torcms.model.info_relation_model import   *
# from config import app_url_name

from torcms.model.info_model import MInfor as MApp
from torcms.model.post_model import MPost
from torcms.model.infor2label_model import MInfor2Label as MApp2Label
from extor.model.json_model import MJson
from extor.model.layout_model import MLayout
import config

class app_json(tornado.web.UIModule):
    def render(self,  app_id,user_id):
        self.mjson = MJson()
        self.mlayout = MLayout()

        json_recs = self.mjson.query_by_app(app_id, user_id)

        kwd = {
            'pager': '',
            'signature': app_id,
            'tdesc': '',
            'site_url': config.site_url,

        }

        return self.render_string('map_theme/modules/app_json.html',
                                  json_recs = json_recs,
                                  app_id = app_id,
                                  kwd=kwd)

class app_layout(tornado.web.UIModule):
    def render(self, app_id, user_id):
        self.mlayout = MLayout()

        layout_recs = self.mlayout.query_by_app(app_id, user_id)

        print(layout_recs.count())

        kwd = {
            'pager': '',
            'tdesc': '',
            'site_url': config.site_url,

        }

        return self.render_string('map_theme/modules/app_layout.html',
                                  layout_recs = layout_recs,
                                  kwd=kwd)
