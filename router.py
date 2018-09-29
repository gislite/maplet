# -*- coding:utf-8 -*-
from extor.handlers.list_handler import ExtListHandler
urls = [
    ("/list/(.*)", ExtListHandler, dict())
]
