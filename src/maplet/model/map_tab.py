import peewee
from playhouse.postgres_ext import JSONField

from torcms.applite.model.ext_tab import TabApp
from torcms.core.base_model import BaseModel
from torcms.torlite.model.core_tab import CabMember


class TabJson(BaseModel):
    uid  = peewee.CharField(null=False, index=True, unique=True, primary_key=True, max_length=4, help_text='', )
    title = peewee.CharField(null = False, default = '')
    user = peewee.ForeignKeyField(CabMember, related_name='json_user_rel')
    json = JSONField()
    time_create = peewee.IntegerField(null = False, default = 0)
    time_update = peewee.IntegerField(null = False, default = 0)
    public = peewee.IntegerField(null = False, default = 0)

class TabApp2Json(BaseModel):
    uid = peewee.CharField(null = False, index=True, unique=True, primary_key=True, max_length=36, help_text='')
    app = peewee.ForeignKeyField(TabApp,  related_name='app2json_app_rel')
    json = peewee.ForeignKeyField(TabJson, related_name = 'app2json_json_rel')

class TabLayout(BaseModel):
    uid  = peewee.CharField(null=False, index=True, unique=True, primary_key=True, max_length=8, help_text='', )
    title = peewee.CharField(null = False, default = '')
    app = peewee.ForeignKeyField(TabApp,  related_name='layout_app_rel')
    user = peewee.ForeignKeyField(CabMember, related_name='layout_user_rel')
    json = peewee.CharField(null = True, default = '', max_length = 4)
    lon = peewee.FloatField(null = False, default = 105)
    lat = peewee.FloatField(null = False, default = 36)
    zoom = peewee.IntegerField(null = False, default = 3)
    marker = peewee.IntegerField(null = False, default = 0)
    time_create = peewee.IntegerField(null = False, default = 0)
    time_update = peewee.IntegerField(null = False, default = 0)
    public = peewee.IntegerField(null = False, default = 0)

