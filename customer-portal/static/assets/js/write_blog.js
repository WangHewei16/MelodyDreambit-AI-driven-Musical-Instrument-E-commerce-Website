// in the commodity detail page, click write blog jump to the page for blog writing
$(document).ready(function () {
    $(".write_blog_btn").on("click", function () {
        let commodity_id = this.getAttribute("product_id")
        zlajax.post({
            url: "/write_blog",
            data: {
                "commodity_id": commodity_id,
            },
            success: function (result) {
                if (result['code'] == 200) {
                    window.location = "/blog";
                } else {
                    warning();
                    window.location = "/login";
                }
            }, fail: function (result) {
                error();
            }
        })
    });
})