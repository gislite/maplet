# -*- coding: utf-8 -*-

'''
for database schema migration.

Memo for Usage:
    migrate.migrate(torcms_migrator.rename_table('e_layout', 'mablayout'))
    migrate.migrate(torcms_migrator.drop_column('tabtag', 'role_mask'))
'''
from playhouse import migrate
from playhouse.postgres_ext import BinaryJSONField
import config


def run_migrate(*args):
    '''
    running some migration.
    :return:
    '''

    print('Begin migrate ...')

    torcms_migrator = migrate.PostgresqlMigrator(config.DB_CON)

    version_field = migrate.IntegerField(null = False,  default=1)

    try:
        migrate.migrate(torcms_migrator.add_column('mabgson', 'version', version_field))
    except:
        pass


    print('Migration finished.')

if __name__ == '__main__':
    run_migrate('aa')
