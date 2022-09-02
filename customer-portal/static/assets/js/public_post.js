// var PublicPostHandler = function (){
//   var csrf_token = $("meta[name='csrf-token']").attr("content");
//   var editor = new window.wangEditor("#editor");
//   editor.config.uploadImgServer = "/post/image/upload";
//   editor.config.uploadFileName = "image";
//   // 1. 放到请求体中
//   // 2. 放到请求头中X-CSRFToken
//   // 再和cookie中的csrf_token进行对比
//   editor.config.uploadImgHeaders = {
//     "X-CSRFToken": csrf_token
//   }
//   editor.config.uploadImgMaxSize = 1024*1024*5;
//   editor.create();
//   this.editor = editor;
// }

// PublicPostHandler.prototype.listenSubmitEvent = function (){
$(function (){
  var that = this;
  $("#submit-btn_addpost").on("click", function (event){
    event.preventDefault();
    var title = $("textarea[name='title']").val();
    var board_id = $("select[name='board_id']").val();
    var content = $("textarea[name='content']").val();
    let blog_pic_input= document.getElementById("blog_pic_file")
    alert(blog_pic_input.val())
    if(pic_address!=""){
    zlajax.post({
      url: "/post/public",
      data: {title,board_id,content,pic_address},
      success: function (result){
        if(result['code'] == 200){
          let data = result['data'];
          let post_id = data['id'];
          window.location = "/blog"
        }else{
          alert(result['message']);
        }
      }
    });
 }else{
      zlajax.post({
      url: "/post/public",
      data: {title,board_id,content,pic_address},
      success: function (result){
        if(result['code'] == 200){
          let data = result['data'];
          let post_id = data['id'];
          window.location = "/blog"
        }else{
          alert(result['message']);
        }
      }
    });
    }
 });
    })

// PublicPostHandler.prototype.run = function(){
//   this.listenSubmitEvent();
// }
//
//
// $(function(){
//   var handler = new PublicPostHandler();
//   handler.run();
// });