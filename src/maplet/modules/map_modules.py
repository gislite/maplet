import random

import tornado.web
import torcms.model.app_model
import torcms.model.app2catalog_model
from torcms.model.mappcatalog import MAppCatalog
from torcms.model import usage_model
from torcms.model.app_rel_model import *
from config import app_url_name

from torcms.model.app_model import MApp
from torcms.model.mpost import MPost
from torcms.model.app2label_model import MApp2Label
from maplet.model.json_model import MJson
from maplet.model.layout_model import MLayout
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
