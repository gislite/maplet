
from torcms.handlers.post_handler import MPost
from torcms.model.post2catalog_model import MPost2Catalog
from torcms.model.evaluation_model import MEvaluation

from torcms.model.usage_model import MUsage

from torcms.model.json_model import MPost2Gson

all_apps = MPost.query_all(kind = 'm', limit=100000)
for app in all_apps:
    print('=' * 20)
    print(app.uid)

    if len(app.uid) == 4:
        MPost.update_field(app.uid, post_id='m' + app.uid )
    for post2tag in MPost2Catalog.query_by_post(app.uid):
        if len(post2tag.post_id) == 4 :
            MPost2Catalog.update_field(post2tag.uid, post_id='m' + post2tag.post_id)

    for post2tag in MUsage.query_by_post(app.uid):
        if len(post2tag.post_id) == 4 :
            MUsage.update_field(post2tag.uid, post_id='m' + post2tag.post_id)

    for post2tag in MPost2Gson.query_by_post(app.uid):
        if len(post2tag.post_id) == 4 :
            MPost2Gson.update_field(post2tag.uid, post_id='m' + post2tag.post_id)