//source: https://www.freesion.com/article/6551462336/
var csrftoken = $('meta[name=csrf-token]').attr('content')

$.ajaxSetup({
    beforeSend: function (xhr, settings) {
        if (!/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type)) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken)
        }
    }
})

$(document).ready(function () {
    $("#submit").addClass("btn btn-info");
    $(".comment_like").unbind('click').bind('click', function () {
        this_id = $(this).attr("id")
        $("#" + this_id).attr({"disabled": "disabled"});     //控制按钮为禁用
        var timeoutObj = setTimeout(function () {
            $("#" + this_id).removeAttr("disabled");//将按钮可用
            /* 清除已设置的setTimeout对象 */
            clearTimeout(timeoutObj)
        }, 500);

        var user_id = $(this).attr("data-user-id")
        var comment_id = $(this).attr("data-comment-id");
        $.post('/add_comment_like', {
            "user_id": user_id,
            "comment_id": comment_id
        }).done(function (response) {
            let server_response = response['text']
            if (server_response != undefined) {
                if (server_response == "cancel") {
                    document.getElementById("add_comment_like_span" + comment_id).innerHTML = "Like " + response['like_amount'];
                    document.getElementById("add_comment_like_span" + comment_id).style.backgroundColor = "blue";
                } else {
                    document.getElementById("add_comment_like_span" + comment_id).innerHTML = "Cancel " + response['like_amount'];
                    document.getElementById("add_comment_like_span" + comment_id).style.backgroundColor = "red";
                }
                // alert(server_response)
            } else {
                warningLogin();
                document.location("login")
            }
        })
    });
    $(".commodity_comment_like").unbind('click').bind('click', function () {
        this_id = $(this).attr("id")
        $("#" + this_id).attr({"disabled": "disabled"});     //控制按钮为禁用
        var timeoutObj = setTimeout(function () {
            $("#" + this_id).removeAttr("disabled");//将按钮可用
            /* 清除已设置的setTimeout对象 */
            clearTimeout(timeoutObj)
        }, 500);
        var user_id = $(this).attr("data-user-id")
        var comment_id = $(this).attr("data-comment-id");
        $.post('/add_commodity_comment_like', {
            "user_id": user_id,
            "comment_id": comment_id
        }).done(function (response) {
            let server_response = response['text']
            if (server_response != undefined) {
                if (server_response == "cancel") {
                    document.getElementById("add_comment_like_span" + comment_id).innerHTML = "Like " + response['like_amount'];
                    document.getElementById("add_comment_like_span" + comment_id).style.backgroundColor = "blue";
                } else {
                    document.getElementById("add_comment_like_span" + comment_id).innerHTML = "Cancel " + response['like_amount'];
                    document.getElementById("add_comment_like_span" + comment_id).style.backgroundColor = "red";
                }
            } else {
                warningLogin();
                document.location("login")
            }
        })
    });
});