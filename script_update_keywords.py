# -*- coding:utf-8 -*-
__author__ = 'bukun'

from torcms.torlite.model.mpost import MPost


def get_dic():
    out_arr = []
    with open('./keywords_dic.txt') as fi:
        uu = fi.readlines()
        for u in uu:
            u = u.strip()
            if len(u) > 0:
                tt = u.split()
                out_arr.append(tt)
    return (out_arr)

def do_for_x(rec):
    kw_dic = get_dic()
    out_dic = {}
    for kw in kw_dic:
        count = rec.title.count(kw[0])
        count2 = rec.cnt_md.count(kw[0])
        if count > 0:
            out_dic[kw[0]] = count * .3 + count2 * .2 + int(kw[1]) * .5
    out_dic2 = sorted(out_dic.items(), key = lambda asd:asd[1], reverse=True)
    return (out_dic2[:8])

if __name__ == '__main__':
    mpost = MPost()
    uu = mpost.query_keywords_empty()
    if uu.count() > 0: 
        for x in uu:
            tt = (do_for_x(x))
            vv = [x[0] for x in tt]
            if len(vv) > 0:
                print(','.join(vv))
                mpost.update_keywords(x.uid, ','.join(vv))
            else:
                mpost.update_keywords(x.uid, '开放地理空间实验室')

