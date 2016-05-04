# -*- coding:utf-8 -*-
__author__ = 'bukun'

import os
import sys
from torcms.applite.model.app_model import MApp

if __name__ == '__main__':
    inws = 'templates/jshtml'
    mapp = MApp()
    recs = mapp.get_all()
    for rec in recs:
        # print(rec.uid)
        sig_count  = 0
        infile = ''
        for wroot, wdirs, wfiles in os.walk(inws):
            for wfile in wfiles:

                if wfile == '{0}.html'.format(rec.uid):
                    infile = os.path.join(wroot, wfile)
                    # print(infile)
                    if wfile.startswith(rec.uid):
                        sig_count  = sig_count + 1
        if sig_count >= 3:
            pass
        elif sig_count == 0:
            pass
        else:
            uu, vv = os.path.split(infile)
            outfile = os.path.join(uu, rec.uid + '_' + rec.title.replace('/', ''))
            with open(outfile, 'w') as fo:
                print(outfile)
                fo.write(rec.title)


