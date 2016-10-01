# -*- coding:utf-8 -*-




from torcms.core import tools
from .core_tab import CabMember as g_Member
from .supertable_model import MSuperTable


class MUser(MSuperTable):
    def __init__(self):
        self.tab = g_Member
        try:
            g_Member.create_table()
        except:
            pass

    def get_by_uid(self, uid):
        try:
            return g_Member.get(g_Member.uid == uid)
        except:
            return False

    def get_by_name(self, uname):
        try:
            return g_Member.get(user_name=uname)
        except:
            return False
    def set_sendemail_time(self, uid):
        entry = g_Member.update(
            time_email = tools.timestamp(),
        ).where(g_Member.uid == uid)
        entry.execute()

    def get_by_email(self, useremail):
        try:
            return g_Member.get(user_email=useremail)
        except:
            return False

    def check_user(self, u_name, u_pass):
        tt = g_Member.select().where(g_Member.user_name == u_name).count()
        if tt == 0:
            return -1
        a = g_Member.get(user_name=u_name)
        if a.user_pass == tools.md5(u_pass):
            return 1
        return 0

    def update_pass(self, u_name, newpass):

        out_dic = {'success': False, 'code': '00'}


        entry = g_Member.update(user_pass=tools.md5(newpass)).where(g_Member.user_name == u_name)
        entry.execute()

        out_dic['success'] = True

        return out_dic


        #entry = CabMember.update(
        #    user_pass=tools.md5(newpass),
        #).where(CabMember.user_name == u_name)
        #try:
        #    entry.execute()
        #    return True
        #except:
        #    return False




    def query_nologin(self):
        time_now = tools.timestamp()
        # num * month * hours * minite * second
        return self.tab.select().where(((time_now - self.tab.time_login) > 3 * 30 * 24 * 60 * 60) & ((time_now - g_Member.time_email) > 4 * 30 * 24 * 60 * 60))

    def update_info(self, u_name, newemail):

        out_dic = {'success': False, 'code': '00'}
        if tools.check_email_valid(newemail):
            pass
        else:
            out_dic['code'] = '21'
            return out_dic


        entry = g_Member.update(user_email=newemail).where(g_Member.user_name == u_name)
        entry.execute()

        out_dic['success'] = True

        return out_dic




            # entry = CabMember.update(
            #    user_email=newemail,
            # ).where(CabMember.user_name == u_name)
            # try:
            #    entry.execute()
            #    return True
            # except:
            #    return False

    def update_time_reset_passwd(self, uname, timeit):
        entry = g_Member.update(
            time_reset_passwd=timeit,
        ).where(g_Member.user_name == uname)
        try:
            entry.execute()
            return True
        except:
            return False

    def update_privilege(self, u_name, newprivilege):
        entry = g_Member.update(
            privilege=newprivilege
        ).where(g_Member.user_name == u_name)
        try:
            entry.execute()
            return True
        except:
            return False
            # return entry

    def update_time_login(self, u_name):
        entry = g_Member.update(
            time_login=tools.timestamp()
        ).where(g_Member.user_name == u_name)
        entry.execute()

    def insert_data(self, post_data):
        out_dic = {'success': False, 'code': '00'}

        if tools.check_username_valid(post_data['user_name'][0]):
            pass
        else:
            out_dic['code'] = '11'
            return out_dic

        if tools.check_email_valid(post_data['user_email'][0]):
            pass
        else:
            out_dic['code'] = '21'
            return out_dic

        if 'privilege' in post_data:
            role = post_data['privilege'][0]
        else:
            role = '10000'



        g_Member.create(uid=tools.get_uuid(),
                        user_name=post_data['user_name'][0],
                        user_pass=tools.md5(post_data['user_pass'][0]),
                        user_email=post_data['user_email'][0],
                        privilege=role,
                        time_create=tools.timestamp(),
                        time_update=tools.timestamp(),
                        time_reset_passwd=tools.timestamp(),
                        time_login=tools.timestamp(),
                        time_email=tools.timestamp()
                        )

        out_dic['success'] = True
        return out_dic


    def get_by_keyword(self, par2):
        return g_Member.select().where(g_Member.user_name.contains(par2))

    def delete_by_user_name(self, user_name):
        try:
            del_count = g_Member.delete().where(g_Member.user_name == user_name)
            del_count.execute()
            return True
        except:
            return False

    def delete(self, del_id):
        try:
            del_count = g_Member.delete().where(g_Member.uid == del_id)
            del_count.execute()
            return True
        except:
            return False
