// $(document).ready(function(){
// 	$(".add-cart-btn").on("click", function () {
// 		let element = document.getElementById('num_of_item')
// 		let commodity_id = element.getAttribute("product_id")
// 		let num_of_item = $("#num_of_item").val()
// 		    zlajax.post({
//       url: "/add_cart",
//       data: {
//         "commodity_id": commodity_id,
//         "num_of_item": num_of_item,
//       },
//       success: function (result){
//         if(result['code'] == 200){
//           alert("success")
//         }else{
//           alert("fail");
//         }
//       }
//     })
//   });
//
// 	// $.post('/add_cart', {
//     //     'commodity_id':commodity_id,
// 	// 	'num_of_item': num_of_item
//     // }).done (function (response) {
//     //     let server_response = response['text']
//     //     alert(server_response)
//     // }).fail(function (){
// 	// 	alert("fail")
// 	// })
// 	// });
// });