# -*- coding:utf-8 -*-

import peewee
from playhouse.postgres_ext import BinaryJSONField

from torcms.core.base_model import BaseModel



class CabPost_Wetland(BaseModel):
    uid = peewee.CharField(null=False, index=False, unique=True, primary_key=True, default='00000',
                           max_length=5, help_text='', )
    title = peewee.CharField(null=False, help_text='Title')
    keywords = peewee.CharField(null=False, help_text='Keywords')
    date = peewee.DateTimeField()
    time_create = peewee.IntegerField()
    user_name = peewee.CharField(null=False, max_length=36, help_text='UserName', )
    time_update = peewee.IntegerField()
    view_count = peewee.IntegerField()
    logo = peewee.CharField()
    cnt_md = peewee.TextField()
    cnt_html = peewee.TextField()


class TabApp_maplet(BaseModel):
    uid = peewee.CharField(max_length=4, null=False, unique=True, help_text='', primary_key=True)
    title = peewee.CharField(null=False, help_text='标题', )
    keywords = peewee.CharField(null=True, default='')
    desc = peewee.CharField(null=True, default='')
    industry = peewee.CharField(default='')
    date = peewee.DateTimeField(null=False, help_text='显示出来的日期时间')
    run_count = peewee.IntegerField(null=False, default=0, help_text='运行次数')
    view_count = peewee.IntegerField(null=False, default=0, help_text='查看次数')
    run_time = peewee.IntegerField(null = False, default = 0, help_text='上次运行时间')
    update_time = peewee.IntegerField(null=False, default=0, help_text='更新时间')
    create_time = peewee.IntegerField(null=False, default=0, help_text='创建时间')
    type = peewee.IntegerField(null=False, default=1)
    html_path = peewee.CharField(default='')
    cnt_md = peewee.TextField(null = True)
    cnt_html = peewee.TextField(null = True)
    lon = peewee.FloatField()
    lat = peewee.FloatField()
    zoom_current = peewee.IntegerField()
    zoom_max = peewee.IntegerField()
    zoom_min = peewee.IntegerField()
    time_update = peewee.IntegerField(null = False, default = 0)



class TabApp_wetland(BaseModel):
    uid = peewee.CharField(max_length=4, null=False, unique=True, help_text='', primary_key=True)
    title = peewee.CharField(null=False, help_text='标题', )
    keywords = peewee.CharField(null=True, default='')
    desc = peewee.CharField(null=True, default='')
    industry = peewee.CharField(default='')
    date = peewee.DateTimeField(null=False, help_text='显示出来的日期时间')
    run_count = peewee.IntegerField(null=False, default=0, help_text='运行次数')
    view_count = peewee.IntegerField(null=False, default=0, help_text='查看次数')
    run_time = peewee.IntegerField(null = False, default = 0, help_text='上次运行时间')
    update_time = peewee.IntegerField(null=False, default=0, help_text='更新时间')
    create_time = peewee.IntegerField(null=False, default=0, help_text='创建时间')
    type = peewee.IntegerField(null=False, default=1)
    html_path = peewee.CharField(default='')
    cnt_md = peewee.TextField(null = True)
    cnt_html = peewee.TextField(null = True)
    # lon = peewee.FloatField()
    # lat = peewee.FloatField()
    # zoom_current = peewee.IntegerField()
    # zoom_max = peewee.IntegerField()
    # zoom_min = peewee.IntegerField()
    time_update = peewee.IntegerField(null = False, default = 0)
    extinfo = BinaryJSONField()


class TabApp_mapfig(BaseModel):
    uid = peewee.CharField(max_length=4, null=False, unique=True, help_text='', primary_key=True)
    title = peewee.CharField(null=False, help_text='标题', )
    keywords = peewee.CharField(null=True, default='')
    desc = peewee.CharField(null=True, default='')
    industry = peewee.CharField(default='')
    date = peewee.DateTimeField(null=False, help_text='显示出来的日期时间')
    # run_count = peewee.IntegerField(null=False, default=0, help_text='运行次数')
    # view_count = peewee.IntegerField(null=False, default=0, help_text='查看次数')
    # run_time = peewee.IntegerField(null = False, default = 0, help_text='上次运行时间')
    # update_time = peewee.IntegerField(null=False, default=0, help_text='更新时间')
    # create_time = peewee.IntegerField(null=False, default=0, help_text='创建时间')
    # type = peewee.IntegerField(null=False, default=1)
    html_path = peewee.CharField(default='')
    cnt_md = peewee.TextField(null = True)
    cnt_html = peewee.TextField(null = True)
    yaml = peewee.TextField()
    # json = BinaryJSONField()
    # lon = peewee.FloatField()
    # lat = peewee.FloatField()
    # zoom_current = peewee.IntegerField()
    # zoom_max = peewee.IntegerField()
    # zoom_min = peewee.IntegerField()
    time_update = peewee.IntegerField(null = False, default = 0)
    # extinfo = BinaryJSONField()

class TabApp_yunsuan(BaseModel):
    uid = peewee.CharField(max_length=4, null=False, unique=True, help_text='', primary_key=True)
    title = peewee.CharField(null=False, help_text='标题', )
    keywords = peewee.CharField(null=True, default='')
    desc = peewee.CharField(null=True, default='')
    industry = peewee.CharField(default='')
    date = peewee.DateTimeField(null=False, help_text='显示出来的日期时间')
    run_count = peewee.IntegerField(null=False, default=0, help_text='运行次数')
    view_count = peewee.IntegerField(null=False, default=0, help_text='查看次数')
    run_time = peewee.IntegerField(null = False, default = 0, help_text='上次运行时间')
    update_time = peewee.IntegerField(null=False, default=0, help_text='更新时间')
    create_time = peewee.IntegerField(null=False, default=0, help_text='创建时间')
    type = peewee.IntegerField(null=False, default=1)
    html_path = peewee.CharField(default='')
    cnt_md = peewee.TextField(null = True)
    cnt_html = peewee.TextField(null = True)
    # lon = peewee.FloatField()
    # lat = peewee.FloatField()
    # zoom_current = peewee.IntegerField()
    # zoom_max = peewee.IntegerField()
    # zoom_min = peewee.IntegerField()
    time_update = peewee.IntegerField(null = False, default = 0)

class MWetland():
    def query_all(self):
        return TabApp_wetland.select()

class MMaplet():
    def query_all(self):
        return TabApp_maplet.select()

class MYunsuan():
    def query_all(self):
        return TabApp_yunsuan.select()
class MPost_Wetland():
    def query_all(self):
        return CabPost_Wetland.select()


class MMapfig():
    def __init__(self):
        pass
        # TabApp_mapfig.create_table()
    def query_all(self):
        return TabApp_mapfig.select()
