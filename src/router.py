# -*- coding:utf-8 -*-

from torcms.claslite.handlers.index_handler import PycateIndexHandler
from torcms.claslite.handlers.label_handler import InfoLabelHandler
from torcms.claslite.handlers.maintain_handler import MaintainPycateCategoryHandler

import torcms.claslite.handlers.widget_handler
import torcms.claslite.handlers.info_handler
import torcms.claslite.handlers.edit_handler
import torcms.claslite.handlers.delete_handler
import torcms.claslite.handlers.add_handler
import torcms.claslite.handlers.list_handler
import torcms.claslite.handlers.publish_handler


urls = [
    ("/info/(.*)", torcms.claslite.handlers.info_handler.InfoHandler, dict(hinfo={})),
    ("/edit/(.*)", torcms.claslite.handlers.edit_handler.EditHandler, dict(hinfo={})),
    ("/delete/(.*)", torcms.claslite.handlers.delete_handler.DeleteHandler, dict(hinfo={})),
    ("/maintain/claslitecategory/(.*)", MaintainPycateCategoryHandler, dict()),
    ("/add/(.*)", torcms.claslite.handlers.add_handler.AddHandler, dict(hinfo={})),
    ("/list/(.*)", torcms.claslite.handlers.list_handler.ListHandler, dict(hinfo={})),
    ("/widget/(.*)", torcms.claslite.handlers.widget_handler.WidgetHandler, dict(hinfo={})),
    ("/publish/(.*)", torcms.claslite.handlers.publish_handler.PublishHandler, dict(hinfo={})),
    ('/info_tag/(.*)', InfoLabelHandler, dict(hinfo={})),
    ("/", PycateIndexHandler, dict()),
]
