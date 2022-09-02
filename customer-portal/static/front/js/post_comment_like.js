//source: https://www.freesion.com/article/6551462336/
var csrftoken = $('meta[name=csrf-token]').attr('content')

$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type)) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken)
        }
    }
})

$(document).ready(function(){
    $("#submit").addClass("btn btn-info");
    $(".comment_like").unbind('click').bind('click',function () {
        var user_id = $(this).attr("data-user-id")
        var comment_id =  $(this).attr("data-comment-id");
        $.post('/add_comment_like', {
             "user_id": user_id,
            "comment_id":comment_id
    }).done (function (response) {
        let server_response = response['text']
            if(server_response != undefined){
                alert(server_response)
                location.reload();
            }else {
                alert("please login first")
            }

        })
    });

});