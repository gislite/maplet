reply_zan = (sig, reply_id, id_num) ->
  id_num = id_num.toString()
  zans = $("#text_zan").val()
  AjaxUrl = "/" + sig + "/toreply/zan/" + reply_id
  $.getJSON AjaxUrl, (Json) ->
    # window.location.href = '/user/login';
    $("#text_zan_" + id_num).html Json.text_zan  unless Json.text_zan is 0

reply_del = (sig, reply_id, id_num) ->
  id_num = id_num.toString()
  AjaxUrl = "/" + sig + "/toreply/delete_reply/" + reply_id
  $.getJSON AjaxUrl, (Json) ->
    $("#del_zan_" + id_num).html ""

reply_it = (sig, view_id) ->
  txt = $("#cnt_md").val()
  return  if txt.length < 30
  $.post "/" + sig + "/toreply/add/" + view_id,
    cnt_md: txt
  , (result) ->
    msg_json = $.parseJSON(result)
    $("#pinglun").load "/reply/get/" + msg_json.pinglun

  $("#cnt_md").val ""
  $("#cnt_md").attr "disabled", true
  $("#btn_submit_reply").attr "disabled", true