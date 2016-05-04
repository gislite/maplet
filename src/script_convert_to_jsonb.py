# -*- coding:utf-8 -*-
import os
import sys



from torcms.applite.model.app_model import MApp

mequ = MApp()

all_recs = mequ.get_all()


def javascript2database():

    for rec in all_recs:
        print(rec.uid)
        dic = {'ext_lon': rec.lon,
               'ext_lat': rec.lat,
               'ext_zoom_current': rec.zoom_current,
               'ext_zoom_max': rec.zoom_max,
               'ext_zoom_min': rec.zoom_min,}
        mequ.update_jsonb(rec.uid, dic)



if __name__ == '__main__':
    javascript2database()
