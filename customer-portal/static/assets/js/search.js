let maxpages = 0;
let p = 1;

$(document).ready(function(){

    $("#search").click(function (){
        sort_method(1);
        console.log(p)
        console.log(maxpages)
    });


});

const getCookie = (name) => document.cookie.match(`[;\s+]?${name}=([^;]*)`)?.pop();

function change_page(page){
    $("#curpage"+page).attr('class', 'active')
    let page_num = page + 1
    // console.log($(this)) //p = page_num sort_method(p)
    // sort_method(page_num)
    p = page_num
    sort_method(p)

}

function pre_page(){
    if(p !== 1){
        p--
        $("#curpage"+(p-1)).attr('class', 'active')
        sort_method(p)
    }
}

function next_page(){
    if(p !== maxpages){
        $("#curpage"+p).attr('class', 'active')
        p++
        sort_method(p)
    }

}

function sort_method(page_num) {
    p = page_num
    let method = $('#sorting').text();
    let amount = $("#amount").val();
    let categories = "";

    if ($("#cat-item-1").is(":checked") === true){
        categories += 0;
    }
    if ($("#cat-item-2").is(":checked") === true){
        categories += 1;
    }
    if ($("#cat-item-3").is(":checked") === true){
        categories += 2;
    }
    if ($("#cat-item-4").is(":checked") === true){
        categories += 3;
    }
    if ($("#cat-item-5").is(":checked") === true){
        categories += 4;
    }
    if ($("#cat-item-6").is(":checked") === true){
        categories += 5;
    }

    $.post(
    "/sort_method",
    {'method': method, 'amount': amount, 'categories': categories, 'page': page_num},
    function(data) {
        // data['conditional_order_query_dict']['data'] -->
        // [{image:xxx, name: xxx...}, {image:xxx, name: xxx...}]

        //清除原div内容
        $("#result_div").empty()
        $("#result_div2").empty()

        //添加搜索结果div内容
        for (let i = 0; i < data['conditional_order_query_dict']['data'].length; i++) {
            // console.log(data['conditional_query_dict']['data'][i]["image"])
            // console.log(data['conditional_query_dict']['data'][i]["price"])
            // console.log(data['conditional_query_dict']['data'][i]["name"])
            // console.log(data['conditional_query_dict']['data'][i]["type"])
            let root = $("#result_div")
            let div1 = $("<div>"); //创建td标签
            div1.attr('class', 'col-xxl-3 col-xl-4 col-lg-4 col-md-6'); //设置标签属性aa=‘td_aa’
            // td.html('哈哈哈哈'); //设置td标签文本值为'哈哈哈哈'
            root.append(div1); //将新创建的td标签插到id为tr_id1的标签中

            let div2 = $("<div>")
            div2.attr('class', 'product__item mb-20')
            div1.append(div2)

            let div3 = $("<div>")
            div3.attr('class', 'product__thumb w-img fix')
            div2.append(div3)

            let div3_a = $("<a>")
            div3_a.attr('href', '\\product-details\\' + data['conditional_order_query_dict']['data'][i]["id"])
            let div3_a_img = $("<img>")
            div3_a_img.attr('src', data['conditional_order_query_dict']['data'][i]["image"])
            div3_a_img.attr('alt', "image lost")
            div3_a.append(div3_a_img)
            div3.append(div3_a)


            let div4 = $("<div>")
            div4.attr('class', 'product__action transition-3')
            let div4_ul = $("<ul>")
            let div4_ul_li1 = $("<li>")
            let div4_ul_li1_a = $("<a>")
            div4_ul_li1_a.attr('class', 'add_cart')
            div4_ul_li1_a.attr('href', 'javascript:void(0)')
            div4_ul_li1_a.attr('id', data['conditional_order_query_dict']['data'][i]["id"])
            let div4_ul_li1_a_i = $("<i>")
            div4_ul_li1_a_i.attr('class', 'fa fa-shopping-cart')
            div4_ul_li1_a_i.attr('aria-hidden', 'true')

            div4_ul_li1_a.append(div4_ul_li1_a_i)
            div4_ul_li1.append(div4_ul_li1_a)


            let div4_ul_li3 = $("<li>")
            let div4_ul_li3_a = $("<a>")
            div4_ul_li3_a.attr('class', 'view-details')
            div4_ul_li3_a.attr('href', '\\javascript:void(0)')
            div4_ul_li3_a.attr('data-bs-toggle', 'modal')
            div4_ul_li3_a.attr('data-bs-target', '#productModalId'+data['conditional_order_query_dict']['data'][i]["id"])

            let div4_ul_li3_a_i = $("<i>")
            div4_ul_li3_a_i.attr('class', 'fa fa-eye')
            div4_ul_li3_a_i.attr('aria-hidden', 'true')

            div4_ul_li3_a.append(div4_ul_li3_a_i)
            div4_ul_li3.append(div4_ul_li3_a)

            div4_ul.append(div4_ul_li1)
            div4_ul.append(div4_ul_li3)

            div4.append(div4_ul)
            div3.append(div4)

            let div5 = $("<div>")
            // div5.attr('class', 'product__content')
            div5.addClass('product__content text-center')
            // let div6 = $("<div>")
            // div6.attr('class', 'product__tag product__tag-4')
            // let div6_span = $("<span>")
            // let div6_span_a = $("<a>")
            // div6_span_a.attr('href', '\\shop')
            // div6_span_a.html(data['conditional_query_dict']['data'][i]["type"])
            // div6_span.append(div6_span_a)
            // div6.append(div6_span)
            // div5.append(div6)

            let div5_h3 = $("<h3>")
            div5_h3.attr("class", 'product__title')
            let div5_h3_a = $("<a>")
            div5_h3_a.attr('href', '\\product-details\\' + data['conditional_order_query_dict']['data'][i]["id"])
            div5_h3_a.html(data['conditional_order_query_dict']['data'][i]["name"])
            div5_h3.append(div5_h3_a)
            div5.append(div5_h3)

            let div7 = $("<div>")
            // div7.attr('class', 'product__price product__price-4 mb-10')
            div7.attr('class', 'product__price')
            let div7_span = $("<span>")
            div7_span.attr('class', 'price')
            div7_span.html("$" + data['conditional_order_query_dict']['data'][i]["price"])
            div7.append(div7_span)
            div5.append(div7)


            let div8 = $("<div>")
            div8.attr('class', 'product__select-button')
            let div8_a = $("<a>")
            div8_a.attr('href', '\\product-details\\' + data['conditional_order_query_dict']['data'][i]["id"])
            div8_a.attr('class', 'select-btn w-100')
            if(getCookie("locale") === 'zh_CN'){
                div8_a.html("查看详情")
            }else{
                div8_a.html("See details")
            }

            div8.append(div8_a)
            div5.append(div8)

            div2.append(div5)

            let _root = $("#result_div2")
            let _div1 = $("<div>"); //创建td标签
            _div1.attr('class', 'single-product mb-30 wood-list-product-wrap'); //设置标签属性aa=‘td_aa’
            _root.append(_div1); //将新创建的td标签插到id为tr_id1的标签中

            let _div2 = $("<div>")
            _div2.attr('class', 'row align-items-xl-center')
            _div1.append(_div2)

            let _div3 = $("<div>")
            _div3.attr('class', 'col-xxl-4 col-xl-4 col-lg-4 col-md-4')

            let _div4 = $("<div>")
            _div4.attr('class', 'product-thumb mr-30 product-thumb-list w-img')

            let _img = $("<img>").attr('src', data['conditional_order_query_dict']['data'][i]["image"])
            let _img2 = $("<img>").attr('src', data['conditional_order_query_dict']['data'][i]["image"])
            _div4.append(_img)
            _div4.append(_img2)
            _div3.append(_div4)
            _div2.append(_div3)

            let _div5 = $("<div>")
            _div5.attr('class', 'col-xxl-8 col-xl-8 col-lg-8 col-md-8')
            _div2.append(_div5)

            let _div6 = $("<div>").attr('class', 'wood-product-content wood-product-list-content')
            _div5.append(_div6)

            let _div6_h4 = $("<h4>")
            _div6_h4.attr('class', 'pro-title pro-title-1')
            let _div6_h4_a = $('<a>').attr('href', '\\product-details\\' + data['conditional_order_query_dict']['data'][i]["id"])
            _div6_h4_a.html(data['conditional_order_query_dict']['data'][i]["name"])
            _div6_h4.append(_div6_h4_a)
            _div6.append(_div6_h4)

            let _div7 = $("<div>").attr('class', 'pro-price')
            _div7.attr('class', 'product__price')
            // let _div7_span = $("<span>").html(data['conditional_order_query_dict']['data'][i]["price"])
            _div6.append(_div7)
            // let _div7_span = $("<span>").html(data['conditional_order_query_dict']['data'][i]["price"])
            let _div7_span = $("<span>").html("$" + data['conditional_order_query_dict']['data'][i]["price"])
            _div7_span.attr('class', 'price')
            _div7.append(_div7_span)


            let _div8 = $("<div>")
            let _div8_a1 = $("<a>")
            let _div8_span1 = $("<span>")
            let _div8_a2 = $("<a>")
            let _div8_br = $("<br>")
            let _div8_a3 = $("<a>")
            let _div8_span2 = $("<span>")
            let _div8_a4 = $("<a>")
            if(getCookie("locale") === 'zh_CN') {
                _div8.attr("class", "product__modal-content-2")
                _div8_a1.html("购买数量 : " + data['conditional_order_query_dict']['data'][i]["buy_amount"])
                _div8_span1.attr("class", "rating-no ml-10 rating-left")
                _div8_a2.html("浏览数量 : " + data['conditional_order_query_dict']['data'][i]["visit_amount"])
                _div8_a3.html("收藏数量 : " + data['conditional_order_query_dict']['data'][i]["collect_amount"])
                _div8_span2.attr("class", "rating-no ml-10 rating-left")
                _div8_a4.html("库存 : " + data['conditional_order_query_dict']['data'][i]["amount"])
            }else{
                _div8.attr("class", "product__modal-content-2")
                _div8_a1.html("Buy amount : " + data['conditional_order_query_dict']['data'][i]["buy_amount"])
                _div8_span1.attr("class", "rating-no ml-10 rating-left")
                _div8_a2.html("Visit amount : " + data['conditional_order_query_dict']['data'][i]["visit_amount"])
                _div8_a3.html("Collect amount : " + data['conditional_order_query_dict']['data'][i]["collect_amount"])
                _div8_span2.attr("class", "rating-no ml-10 rating-left")
                _div8_a4.html("Inventory : " + data['conditional_order_query_dict']['data'][i]["amount"])
            }
            _div8.append(_div8_a1,_div8_span1,_div8_a2, _div8_br,_div8_a3,_div8_span2,_div8_a4)
            _div6.append(_div8)

            let _div9 = $("<div>").attr('class', 'wood-shop-product-actions')
            let _div9_a1 = $("<a>").attr('href', '\\product-details\\' + data['conditional_order_query_dict']['data'][i]["id"])
            _div9_a1.attr('class', 'wood-cart-btn')

            if(getCookie("locale") === 'zh_CN'){
                _div9_a1.html('查看详情')
            }else{
                _div9_a1.html('See Details')
            }

            let _div9_a2 = $("<a>").attr('href', '\\javascript:void(0)')
            _div9_a2.attr('class', 'wood-proudct-btn-boxed')
            let _div9_a2_i = $("<i>").attr('class', 'fa fa-eye')
            _div9_a2.append(_div9_a2_i)
            _div9_a2.attr('data-bs-toggle', 'modal')
            _div9_a2.attr('data-bs-target', '#productModalId'+data['conditional_order_query_dict']['data'][i]["id"])

            _div9.append(_div9_a1, _div9_a2)


            _div6.append(_div9)

        }

        //data['total']
        let page_div = $(".basic-pagination")
        page_div.empty()

        let pnav = $("<nav>")
        page_div.append(pnav)
        let pnav_ul = $("<ul>")
        pnav.append(pnav_ul)
        let last_li = $("<li>")
        pnav_ul.append(last_li)
        let last_a = $("<a>").attr('href', '#label mr-15')
        last_a.attr('onclick', 'pre_page()')
        last_li.append(last_a)
        let last_i = $("<i>").attr('class', 'far fa-angle-left')
        last_a.append(last_i)

        let page = Math.ceil(data['total']/12)
        // console.log(page)
        maxpages = page
        for(let i = 0; i < page; i++){
            let loopi = $("<li>")
            pnav_ul.append(loopi)
            // let loopi_a = $("<a>").onclick = function () {change_page(i)}
            let loopi_a = $("<a>").attr('onclick', "change_page("+i.toString()  +")")
            loopi_a.attr('href', '#label mr-15')
            loopi_a.attr('id', 'curpage' + i.toString())
            loopi_a.html(i+1)
            loopi.append(loopi_a)
        }

        let next_li = $("<li>")
        pnav_ul.append(next_li)
        let next_a = $("<a>").attr('href', '#label mr-15')
        next_a.attr('onclick', 'next_page()')
        next_li.append(next_a)
        let next_i = $("<i>").attr('class', 'far fa-angle-right')
        next_a.append(next_i)

        let total_div = $(".col-xxl-4.col-xl-6.col-md-6.col-sm-6")
        total_div.empty()
        let total_p = $("<p>").attr({'class': 'show-total-result text-sm-center', 'id': 'show-total-result'})
        if(data['conditional_order_query_dict']['data'].length === 0){
            if(getCookie("locale") === 'zh_CN'){
                total_p.html('无相关结果');
            }else{
                total_p.html('No Results');
            }

        }else{
            if(getCookie("locale") === 'zh_CN'){
                total_p.html('共 ' + data['total'] +' 条结果')
            }else{
                total_p.html('Total of ' + data['total'] +' Results')
            }
        }
        total_div.append(total_p)



    },
    "json"

);


}