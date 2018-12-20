# -*- coding:utf-8 -*-
from extor.handlers.list_handler import ExtListHandler
from torcms.handlers.post_handler import PostHandler
urls = [
    ("/list/(.*)", ExtListHandler, dict()),
    ("/books/(.*)", PostHandler, dict(kind='k',filter_view = True))

]
