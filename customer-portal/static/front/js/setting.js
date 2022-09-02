var SettingHandler = function (){}

SettingHandler.prototype.listenAvatarUploadEvent = function (){
  $("#avatar-input").on("change", function (){
    var image = this.files[0];
    var formData = new FormData();
    formData.append("image", image);
    zlajax.post({
      url: "/avatar/upload",
      data: formData,
      // 如果使用jQuery上传文件，那么还需要指定以下两个参数
      processData: false,
      contentType: false,
      success: function (result){
        if(result['code'] == 200){
          // result = {"code": 200, "data": {"avatar": "/xxx"}}
          var avatar = result['data']['avatar'];
          var avatar_url = "/media/avatar/" + avatar;
          $("#avatar-img").attr("src", avatar_url);
        }
      }
    })
  });
}

SettingHandler.prototype.listenSubmitEvent = function (){
  $("#setting_submit_btn").on("click", function (event){
    event.preventDefault();
    var address1 = $("#setting_address1").val();
    var address2 = $("#setting_address2").val();
    if(!address1 && !address2){
      alert("提交成功！");
      return;
    }
    if(address1 && (address1.length > 50 || address1.length < 1)){
      alert("地址长度必须在1-50字之间！");
      return;
    }
    zlajax.post({
      url: "/post_setting",
      data: {
        "address1": address1,
        "address2": address2,
      },
      success: function (result){
        if(result['code'] == 200){
          alert("提交成功！");
        }else{
          alert(result['message'])
        }
      }
    })
  });
}

SettingHandler.prototype.listenSubmitEvent = function (){
  $("#setting_logout_btn").on("click", function (event){
    event.preventDefault();
    zlajax.post({
      url: "/logout",
      data: {
        // "address1": address1,
        // "address2": address2,
      },
      success: function (result){
        if(result['code'] == 200){
          alert("提交成功！");
        }else{
          alert(result['message'])
        }
      }
    })
  });
}

SettingHandler.prototype.run = function (){
  this.listenAvatarUploadEvent();
  this.listenSubmitEvent();
}

$(function (){
  var handler = new SettingHandler();
  handler.run();
})