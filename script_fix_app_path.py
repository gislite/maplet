# -*- coding:utf-8 -*-
import os
import sys



pwd = os.getcwd()
(qian, hou) = os.path.split(pwd)
sys.path.append(qian)

from torcms.applite.model.app_model import MApp

mequ = MApp()


xx = range(0, 16)
yy = [hex(x) for x in xx]
yy = [y[-1] for y in yy]

def javascript2database(sig):

    js_dic = {
        'sig': sig,
        'title': '11sadf',
        'desc': '',
        'type': 1,
        'cnt_md': 'MarkDown Content.',
        'cnt_html': 'HTML Content.',
    }
    mequ.addata_init(js_dic)

def test_valid(wfile):
    '''
    Test the file in App HTML File.
    :param wfile:
    :return:
    '''
    if len(wfile) == 9:
        pass
    else:
        # print('Er')
        return False

    if wfile.endswith('.html') :
        pass
    else:
        return False

    for x in wfile[:4]:
        if x in yy:
            pass
        else:
            return False

    return True

if __name__ == '__main__':
    for sig in ['0101', '0102', '0103', '0104', '0105', '0106']:
        javascript2database(sig)
