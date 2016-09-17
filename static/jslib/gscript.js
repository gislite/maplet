$.ready()
{
    $("#searchForm").validate({
                rules: {
                    searchheader: "required"
                },
                messages: {
                    searchheader: "<span class='red'>Please enter keywords</span>"

                }
            });

    $('#act_collect').click(function () {
        $.ajax({
            url: "/collect/" + post_uid,
            type: 'GET',
            cache: false,
            data: {},
            dataType: 'html',
            timeout: 1000,
            error: function () {
                alert('请登陆后进行收藏！')
            },
            success: function (result) {
                var uu = $.parseJSON(result);
                $('#text_collect').text('成功收藏');
                $('#text_collect').css('color', 'red');
            }
        });
    });

    function g_load_postcat(ii) {
        if ($('#pcat' + ii.toString()).val() == 0)
        {
            $('#gcat' + ii.toString()).empty();
        }
        else {
            $.ajax({
                url: '/category/j_subcat/' + $('#pcat' + ii.toString()).val(),
                type: 'GET',
                data: {},
                timeout: 1000,
                error: function () {
                    alert('重新加载');
                },
                success: function (result) {
                    var data = eval("(" + result + ")");
                    $('#gcat' + ii.toString()).empty();
                    $.each(data, function (tagidx, tagname) {
                        $("<option></option>")
                            .val(tagidx)
                            .text(tagname)
                            .appendTo($('#gcat' + ii.toString()));
                    });
                }
            });
        }
    }

    function g_load_infocat(ii) {
        if ($('#pcat' + ii.toString()).val() == 0)
        {
            $('#gcat' + ii.toString()).empty();
        }
        else {
            $.ajax({
                url: '/tag/j_subcat/' + $('#pcat' + ii.toString()).val(),
                type: 'GET',
                data: {},
                timeout: 1000,
                error: function () {
                    alert('重新加载');
                },
                success: function (result) {
                    var data = eval("(" + result + ")");
                    $('#gcat' + ii.toString()).empty();
                    $.each(data, function (tagidx, tagname) {
                        $("<option></option>")
                            .val(tagidx)
                            .text(tagname)
                            .appendTo($('#gcat' + ii.toString()));
                    });
                }
            });
        }
    }

    function reply_zan(sig, reply_id, id_num) {
        id_num = id_num.toString();
        zans = $('#text_zan').val();
        var AjaxUrl = "/" + sig + "/reply/zan/" + reply_id;
        $.getJSON(AjaxUrl, function (Json) {
            if (Json.text_zan == 0) {
            }
            else {
                $("#text_zan_" + id_num).html(Json.text_zan);
            }
        });
    }

    function reply_del(sig, reply_id, id_num) {
        id_num = id_num.toString();
        var AjaxUrl = "/" + sig + "/reply/delete/" + reply_id;
        $.getJSON(AjaxUrl, function (Json) {
            if (Json.del_zan == 1) {
                $("#del_zan_" + id_num).html('');
            }
            else {
                alert('删除失败！');
            }
        });
    }


    function reply_it(sig, view_id) {
        var txt = $("#cnt_md").val();
        if (txt.length < 10) {
            return;
        }
        $.post("/" + sig + "/reply/add/" + view_id, {cnt_md: txt}, function (result) {
            var msg_json = $.parseJSON(result);
            $("#pinglun").load('/reply/get/' + msg_json.pinglun);
        });
        $('#cnt_md').val('');
        $('#cnt_md').attr("disabled", true);
        $('#btn_submit_reply').attr('disabled', true);
    }

}
