import datetime
import math

import oss2
from datetime import datetime
import datetime as dt
import json

import shortuuid
from flask import (
    Blueprint,
    request,
    render_template,
    current_app,
    make_response,
    session,
    redirect,
    g,
    jsonify,
    url_for, send_from_directory
)
import string
import random
import hashlib

from sqlalchemy import and_, or_
from werkzeug.security import generate_password_hash

from config import POST_PIC_UPLOAD_DIR
from exts import mail
from exts import cache
from utils import restful
from utils.captcha import Captcha
import time
import os
from io import BytesIO
from .forms import RegisterForm, LoginForm, UploadImageForm, EditProfileForm, PublicCommentForm, \
    PostForm, ChangePassForm, SettingForm, ProductCommentForm
from models.auth import Permission, UserModel
from exts import db
from .decorators import login_required
from hashlib import md5
from flask_avatars import Identicon
from flask_mail import Message
from models.post import PostModel, PostCommentModel, BannerModel, PostCommentLikeModel, CommodityModel, CartModel, \
    CommodityIntroductionModel, AddressModel, SearchKeyword, ChatModel, ChatCount, OrderModel, PostPictureModel, \
    StaffModel, NationModel, CommodityCommentModel, CommodityCommentLikeModel, CommodityLike

from flask_paginate import get_page_parameter, Pagination
from sqlalchemy.sql import func
from flask_jwt_extended import create_access_token

bp = Blueprint("front", __name__, url_prefix="/")
access_key_id = os.getenv('OSS_TEST_ACCESS_KEY_ID', 'LTAI5t5rXFCtUG3FiCARAd2d')
access_key_secret = os.getenv('OSS_TEST_ACCESS_KEY_SECRET', 'BxxKVg6ox20U0Hev4SnKn8FwYNRP7N')
bucket_name = os.getenv('OSS_TEST_BUCKET', 'qintong-edu')
endpoint = os.getenv('OSS_TEST_ENDPOINT', 'oss-cn-beijing.aliyuncs.com')
# aliyun.oss.file.endpoint=oss-cn-beijing.aliyuncs.com
# aliyun.oss.file.keyid=LTAI5t5rXFCtUG3FiCARAd2d
# aliyun.oss.file.keysecret=BxxKVg6ox20U0Hev4SnKn8FwYNRP7N
# aliyun.oss.file.bucketname=qintong-edu
# 确认参数
for param in (access_key_id, access_key_secret, bucket_name, endpoint):
    assert '<' not in param, '请设置参数：' + param
# 创建Bucket对象
bucket = oss2.Bucket(oss2.Auth(access_key_id, access_key_secret), endpoint, bucket_name)


# 钩子函数：before_request，在调用视图函数之前执行
@bp.before_request
def front_before_reuqest():
    if 'user_id' in session:
        user_id = session.get("user_id")
        user = UserModel.query.get(user_id)
        setattr(g, "user", user)


# 请求 => before_request => 视图函数（返回模板） => context_processor => 将context_processor返回的变量也添加到模板中

# 上下文处理器
@bp.context_processor
def front_context_processor():
    if hasattr(g, "user"):
        return {"user": g.user}
    else:
        return {}


def view_cart():
    user_id = session.get("user_id")
    count_index = 0
    comds = CommodityModel.query.filter(CommodityModel.discount == 1).all()
    for comd in comds:
        comd.price = int(comd.price * 0.8)

    if not user_id is None:
        carts_index = db.session.query(CartModel.id, CartModel.user_id, CartModel.amount, CommodityModel.id,
                                       CommodityModel.name,
                                       CommodityModel.image_oss, CommodityModel.price). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == user_id).all()
        sum_index = 0
        for i in carts_index:
            count_index = count_index + 1
            sum_index = sum_index + i[2] * i[6]
        return carts_index, sum_index, count_index
    else:
        return None


@bp.route('/', methods=['GET', 'POST'])
def index():
    total_query = CommodityModel.query.all()
    intro_query = CommodityIntroductionModel.query.all()
    intro_query_dict = {}
    for i in intro_query:
        if i.intro is not None:
            intro_query_dict[i.id] = i.intro.lstrip('<p>').rstrip('</p>')[0:100] + "..."
        else:
            intro_query_dict[i.id] = ''

    if request.method == "POST":
        keyword = request.form.get("keyword")
        SearchKeyword.keyword = keyword
        return redirect(url_for("front.shop", type_num=6))

    pipes_number_type = CommodityModel.query.filter_by(type=0).count()
    electronic_number_type = CommodityModel.query.filter_by(type=1).count()
    precussion_number_type = CommodityModel.query.filter_by(type=2).count()
    piano_number_type = CommodityModel.query.filter_by(type=3).count()
    guitar_number_type = CommodityModel.query.filter_by(type=4).count()
    other_number_type = CommodityModel.query.filter_by(type=5).count()
    commodity_buy_query_buymost = CommodityModel.query.order_by(CommodityModel.buy_amount.desc()).limit(10)
    buy_most = []
    # buy_most是一个列表，里面每个元素是一个字典，字典里存着商品的所有如上信息
    # [ {商品1的所有信息}, {商品2的所有信息} ....... ]
    for commodity in commodity_buy_query_buymost:
        if commodity.discount == 1:
            buy_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": int(commodity.price * 0.8),
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,

            })
        else:
            buy_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": commodity.price,
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,

            })
    buy = [[buy_most[0], buy_most[1]], [buy_most[2], buy_most[3]], [buy_most[4], buy_most[5]],
           [buy_most[6], buy_most[7]], [buy_most[8], buy_most[9]]]

    # 商品列表按照收藏数排序 首页展示
    commodity_collect_query_collectmost = CommodityModel.query.order_by(CommodityModel.collect_amount.desc()).limit(10)

    collect_most = []
    # collect_most是一个列表，里面每个元素是一个字典，字典里存着商品的所如下信息
    for commodity in commodity_collect_query_collectmost:
        if commodity.discount == 1:
            collect_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": int(commodity.price * 0.8),
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,

            })
        else:
            collect_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": commodity.price,
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,

            })
    collect = [[collect_most[0], collect_most[1]], [collect_most[2], collect_most[3]],
               [collect_most[4], collect_most[5]], [collect_most[6], collect_most[7]],
               [collect_most[8], collect_most[9]]]

    # 商品列表按照浏览量排序  首页展示
    commodity_visit_query_visitmost = CommodityModel.query.order_by(CommodityModel.visit_amount.desc()).limit(10)

    visit_most = []
    # visit_most是一个列表，里面每个元素是一个字典，字典里存着商品的所有如下信息
    for commodity in commodity_visit_query_visitmost:
        if commodity.discount == 1:
            visit_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": int(commodity.price * 0.8),
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,
            })
        else:
            visit_most.append({
                "id": commodity.id,
                "name": commodity.name,
                "price": commodity.price,
                "image": commodity.image_oss,
                "type": commodity.type,
                "visitAmount": commodity.visit_amount,
                "buyAmount": commodity.buy_amount,
                "collectAmount": commodity.collect_amount,
            })
    visit = [[visit_most[0], visit_most[1]], [visit_most[2], visit_most[3]], [visit_most[4], visit_most[5]],
             [visit_most[6], visit_most[7]], [visit_most[8], visit_most[9]]]

    context = {
        # "buy_most": commodity_collect_query_collectmost,
        # "collect_most": commodity_collect_query_collectmost,
        # "visit_most": commodity_visit_query_visitmost,
        "pipes_number_type": pipes_number_type,
        "electronic_number_type": electronic_number_type,
        "precussion_number_type": precussion_number_type,
        "piano_number_type": piano_number_type,
        "guitar_number_type": guitar_number_type,
        "other_number_type": other_number_type
    }
    type_dic = {0: "Pipes", 1: "Electronic",
                2: "Precussion",
                3: "Piano",
                4: "Guitar",
                5: "Other"}
    user_id = session.get("user_id")
    count_index = 0
    post_query = PostModel.query.order_by(PostModel.gmt_create.desc()).limit(4)
    discount_commodities = CommodityModel.query.filter_by(discount=1).all()

    date_dic = {}
    discount_price_dic = {}
    for discount_commodity in discount_commodities:
        discount_price_dic[discount_commodity.id] = discount_commodity.price
        discount_commodity.price = int(discount_commodity.price * 0.8)
        discount_date = discount_commodity.gmt_modify
        discount_date = discount_date + dt.timedelta(days=30)
        discount_date = discount_date.__format__('%Y/%m/%d')
        date_dic[discount_commodity.id] = discount_date

    if not user_id is None:
        carts_index = db.session.query(CartModel.id, CartModel.user_id, CartModel.amount, CommodityModel.id,
                                       CommodityModel.name,
                                       CommodityModel.image_oss, CommodityModel.price). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == user_id).all()

        sum_index = 0
        for i in carts_index:
            count_index = count_index + 1
            sum_index = sum_index + i[2] * i[6]

    if not user_id is None:
        return render_template("newFront/index.html", buy_most=buy,
                               collect_most=collect, visit_most=visit, carts=carts_index, sum=sum_index,
                               count=count_index, discount_commodities=discount_commodities, type_dic=type_dic,
                               date_dic=date_dic, discount_price_dic=discount_price_dic, post_query=post_query,
                               **context, total_query=total_query, intro_query_dict=intro_query_dict)
    else:
        return render_template("newFront/index.html", buy_most=buy,
                               collect_most=collect, visit_most=visit, count=count_index, type_dic=type_dic,
                               discount_commodities=discount_commodities, post_query=post_query,
                               date_dic=date_dic, discount_price_dic=discount_price_dic, **context,
                               total_query=total_query,
                               intro_query_dict=intro_query_dict)


@bp.get('/cms')
def cms():
    return render_template("cms/index.html")


@bp.get("/index")
def index_to_index():
    return redirect("/")


@bp.route("/about", methods=["GET", "POST"])
def about():
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/about.html", carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return render_template("newFront/about.html", count=0, sum=0)


@bp.route("/terms&conditions", methods=["GET", "POST"])
def terms():
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/terms_conditions.html", carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return render_template("newFront/terms_conditions.html", count=0, sum=0)


@bp.route("/blog_details/<post_id>", methods=['GET', 'POST'])
def blog_details(post_id):
    like_or_dislike = {}
    # create a list that judge whether the user has like the comment
    post_model = PostModel.query.get(post_id)
    comments = PostCommentModel.query.filter_by(post_id=post_id)
    user_id = session.get("user_id")
    for comment in comments:
        comment_id = comment.id
        like_in_db = PostCommentLikeModel.query.filter(and_(PostCommentLikeModel.user_id == user_id,
                                                            PostCommentLikeModel.post_comment_id == comment_id)).first()
        if like_in_db:
            like_or_dislike[comment_id] = True
        else:
            like_or_dislike[comment_id] = False
    comment_count = comments.count()

    context = {
        "comment_count": comment_count,
        "post": post_model,
        "like_or_dislike": like_or_dislike
    }
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/blog-details.html", **context, carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return render_template("newFront/blog-details.html", **context, count=0, sum=0)


@bp.route("/company", methods=['GET', 'POST'])
def company():
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/company_profile.html", carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return render_template("newFront/company_profile.html", count=0, sum=0)


@bp.route("/product-details/<commodity_id>", methods=['GET', 'POST'])
def product_details(commodity_id):
    # commodity_id = 1509369898466353153
    commodities = CommodityModel.query.get(commodity_id)

    commodities.visit_amount += 1  # 浏览次数 +1
    db.session.commit()
    like_or_dislike = {}
    # 根据id查询商品的详细介绍
    intro_query = CommodityIntroductionModel.query.filter_by(id=commodity_id).first()
    commodity_comments = CommodityCommentModel.query.filter_by(commodity_id=commodity_id).all()
    user_id = session.get("user_id")
    for comment in commodity_comments:
        comment_id = comment.id
        like_in_db = CommodityCommentLikeModel.query.filter(and_(CommodityCommentLikeModel.user_id == user_id,
                                                                 CommodityCommentLikeModel.comment_id == comment_id)).first()
        if like_in_db:
            like_or_dislike[comment_id] = True
        else:
            like_or_dislike[comment_id] = False
    star = 0.0
    commodity_like = CommodityLike.query.filter_by(commodity_id=commodity_id).all()
    if len(commodity_like) != 0:
        starSum = 0
        for i in commodity_like:
            starSum = starSum + i.star
        star = starSum / len(commodity_like)
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/product-details.html", carts=ans[0], sum=ans[1], count=ans[2],
                               product_detail=commodities, product_intro=intro_query,
                               commodity_comments=commodity_comments,
                               like_or_dislike=like_or_dislike, star=star)
    else:
        return render_template("newFront/product-details.html", count=0, product_detail=commodities,
                               product_intro=intro_query, commodity_comments=commodity_comments
                               , like_or_dislike=like_or_dislike, sum=0, star=star)


@bp.route("/add_cart", methods=['GET', 'POST'])
@login_required
def add_cart():
    commodity_id = request.form['commodity_id']
    commodity_amount = CommodityModel.query.filter_by(id=commodity_id).first().amount
    num = int(request.form['num_of_item'])
    user_id = session.get("user_id")
    if user_id == None:
        return jsonify({'returnvalue': 2})
    else:
        if commodity_amount < num:
            return jsonify({'returnvalue': 3})
        cart_query = CartModel.query.filter_by(commodity_id=commodity_id, user_id=user_id, status=1).filter(
            CartModel.order_id == 0).first()
        if not cart_query:
            cart_query = CartModel(commodity_id=commodity_id, amount=num, user_id=user_id, status=1, order_id=0)
            db.session.add(cart_query)
            db.session.commit()
        else:
            if commodity_amount < (cart_query.amount + num):
                return jsonify({'returnvalue': 4})
            cart_query.amount = cart_query.amount + num
            cart_query.gmt_modify = datetime.now()
            db.session.commit()

        comds = CommodityModel.query.filter(CommodityModel.discount == 1).all()
        for comd in comds:
            comd.price = int(comd.price * 0.8)

        cart_dict = {}
        cart_query = []
        countNum = 0
        sum = 0
        cart_dict["data"] = cart_query

        carts = db.session.query(CartModel.id, CartModel.user_id, \
                                 CartModel.amount, CommodityModel.id, CommodityModel.name, \
                                 CommodityModel.image_oss, CommodityModel.price). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == session.get("user_id")).all()
        for i in carts:
            sum = sum + i[6] * i[2]
            countNum = countNum + 1
            cart_query.append({
                "id": i[0],
                "user_id": i[1],
                "amount": i[2],
                "commodity_id": i[3],
                "commodity_name": i[4],
                "commodity_image_oss": i[5],
                "commodity_price": i[6]
            })
        sum_modify = "$" + str(sum)
        return jsonify({'returnvalue': 0, 'sum_modify': sum_modify,
                        'cart_query': cart_dict, 'cart_count': countNum})


# 新增：修改购物车中商品数量
@bp.route("/modify_cart", methods=['GET', 'POST'])
def modify_cart():
    chosenNum = request.form['chosenNum']
    chosenId = request.form['chosenId']
    sum_current = request.form['sum_current']
    chosen_amount_current = request.form['chosen_amount_current']
    sum_current = sum_current[1:]
    chosen_amount_current = chosen_amount_current[1:]
    cart = CartModel.query.filter_by(id=chosenId).filter(CartModel.order_id == 0).first()
    current_num = cart.amount
    if cart:
        if chosenNum == 0:
            db.session.delete(cart)
            db.session.commit()
        else:
            if int(chosenNum) > current_num:
                commodity_amount = CommodityModel.query.filter_by(id=cart.commodity_id).first().amount
                if commodity_amount < int(chosenNum):
                    return jsonify({"returnvalue": 2})
            good = CommodityModel.query.filter_by(id=cart.commodity_id).first()
            price = good.price
            cart.amount = int(chosenNum)
            chosen_amount_modify = int(price) * int(chosenNum)
            minus = chosen_amount_modify - int(chosen_amount_current)
            sum_new = minus + int(sum_current)
            subtt = "$" + str(chosen_amount_modify)
            sum_modify = "$" + str(sum_new)
            cart.gmt_modify = datetime.now()
            db.session.commit()
            cart_dict = {}
            cart_query = []
            cart_dict["data"] = cart_query
            carts = db.session.query(CartModel.id, CartModel.user_id, \
                                     CartModel.amount, CommodityModel.id, CommodityModel.name, \
                                     CommodityModel.image_oss, CommodityModel.price). \
                filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
                filter(CartModel.user_id == session.get("user_id")).all()
            for i in carts:
                cart_query.append({
                    "id": i[0],
                    "user_id": i[1],
                    "amount": i[2],
                    "commodity_id": i[3],
                    "commodity_name": i[4],
                    "commodity_image_oss": i[5],
                    "commodity_price": i[6]
                })
        return jsonify(
            {'text': 'You modify the good succeed', 'returnvalue': 0, 'subtt': subtt, 'sum_modify': sum_modify,
             'cart_query': cart_dict})
    else:
        return jsonify({'text': 'Something is error', 'returnvalue': 1})


# 修改订单地址（status=2）或将订单状态改为退款（status=1）
@bp.route("/edit_order", methods=['GET', 'POST'])
def edit_order():
    if request.form['type'] == "change address":
        order_id = request.form["order_id"]
        order = OrderModel.query.filter_by(id=order_id).first()
        order.status = 2
        db.session.commit()
        return jsonify({'text': 'Waiting for merchant confirmation', 'returnvalue': 0})
    elif request.form['type'] == "refund":
        order_id = request.form["order_id"]
        order = OrderModel.query.filter_by(id=order_id).first()
        order.status = 1
        db.session.commit()
        return jsonify({'text': 'Waiting for merchant confirmation', 'returnvalue': 0})
    elif request.form['type'] == "sign order":
        order_id = request.form["order_id"]
        order = OrderModel.query.filter_by(id=order_id).first()
        order.flowstatus = 3
        db.session.commit()
        return jsonify({'text': 'The order is finished!!', 'returnvalue': 0})
    else:
        return jsonify({'text': 'Something is error', 'returnvalue': 1})


@bp.route("/delete_cart", methods=['GET', 'POST'])
def delete_cart():
    chosenId = request.form['chosenId']
    sum_current = request.form['sum_current']
    chosen_amount_current = request.form['chosen_amount_current']
    sum_current = sum_current[1:]
    chosen_amount_current = chosen_amount_current[1:]
    cart = CartModel.query.filter_by(id=chosenId).first()
    if cart:
        db.session.delete(cart)
        db.session.commit()
        sum_new = int(sum_current) - int(chosen_amount_current)
        sum_modify = "$" + str(sum_new)
        cart_dict = {}
        cart_query = []
        countNum = 0
        cart_dict["data"] = cart_query
        carts = db.session.query(CartModel.id, CartModel.user_id, \
                                 CartModel.amount, CommodityModel.id, CommodityModel.name, \
                                 CommodityModel.image_oss, CommodityModel.price). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == session.get("user_id")).all()
        for i in carts:
            countNum = countNum + 1
            cart_query.append({
                "id": i[0],
                "user_id": i[1],
                "amount": i[2],
                "commodity_id": i[3],
                "commodity_name": i[4],
                "commodity_image_oss": i[5],
                "commodity_price": i[6]
            })
        return jsonify(
            {'text': 'You modify the good succeed', 'returnvalue': 0, 'sum_modify': sum_modify, 'cart_query': cart_dict,
             'cart_count': countNum})
    else:
        return jsonify({'text': 'Something is error', 'returnvalue': 1})


# 优化：判断是否登录
@bp.route("/cart", methods=['GET', 'POST'])
def cart_list():
    user_id = session.get("user_id")
    countNum = 0
    if not user_id is None:
        comds = CommodityModel.query.filter(CommodityModel.discount == 1).all()
        for comd in comds:
            comd.price = int(comd.price * 0.8)
        carts = db.session.query(CartModel.id, CartModel.user_id, \
                                 CartModel.amount, CommodityModel.id, CommodityModel.name, \
                                 CommodityModel.image_oss, CommodityModel.price, CommodityModel.discount,
                                 CommodityModel.amount). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == user_id).all()

        sum = 0
        for i in carts:
            countNum = countNum + 1
            sum = sum + i[2] * i[6]
    else:
        return redirect("/login")
    return render_template("newFront/cart.html", sum=sum, carts=carts, count=countNum)


@bp.route("/update_checkout", methods=['GET', 'POST'])
# @login_required
def update_checkout():
    user_id = session.get("user_id")
    address_id = request.form['address_id']
    method = request.form['method']
    address = AddressModel.query.filter_by(id=address_id).first()
    order_id = shortuuid.uuid()
    carts_items = db.session.query(CartModel).filter(CartModel.user_id == user_id).filter(CartModel.status == 1).all()

    priority = 0
    for item in carts_items:
        commodity_query = CommodityModel.query.filter_by(id=item.commodity_id).first()
        if item.amount >= commodity_query.amount:
            item.amount = commodity_query.amount
            commodity_query.amount = 0
            db.session.commit()
            cart_delete = CartModel.query.filter_by(commodity_id=commodity_query.id, status=1).all()
            for cart_delete_item in cart_delete:
                if cart_delete_item.id != item.id:
                    db.session.delete(cart_delete_item)
                    db.session.commit()
        else:
            if item.status == 1:#7776777
                commodity_query.amount = commodity_query.amount - item.amount#7776777
                #7776777
            else:#7776777
                return jsonify({'text': 'The order have been placed', 'returnvalue': 0})#7776777

        priority += item.commodity.price + item.commodity.buy_amount * 10 + item.commodity.collect_amount * 5
    order = OrderModel(id=order_id, address_id=address_id, user_id=user_id, method=method,
                       # first_name=address.first_name,
                       # last_name=address.last_name,
                       status=0, priority=priority)
    db.session.add(order)

    carts = db.session.query(CartModel).filter(CartModel.user_id == user_id).filter(CartModel.status == 1)
    carts.update({CartModel.status: 0, CartModel.order_id: order_id})
    db.session.commit()
    return jsonify({'text': 'Successful order', 'returnvalue': 1})


@bp.route("/checkout", methods=['GET', 'POST'])
@login_required
def checkout():
    user_id = session.get("user_id")
    countNum = 0
    address_query = []
    if not user_id is None:
        addresses = AddressModel.query.filter_by(user_id=user_id, deleted=0).order_by(
            AddressModel.gmt_create.desc()).all()
        for address in addresses:
            nation_name = NationModel.query.filter_by(id=address.nation_id).first().name
            address_query.append({
                "id": address.id,
                "first_name": address.first_name,
                "last_name": address.last_name,
                "nation_name": nation_name,
                "address1": address.address1,
                "address2": address.address2,
                "phone_number": address.phone_number
            })
        comds = CommodityModel.query.filter(CommodityModel.discount == 1).all()

        for comd in comds:
            comd.price = int(comd.price * 0.8)

        carts = db.session.query(CartModel.id, CartModel.user_id, \
                                 CartModel.amount, CommodityModel.id, CommodityModel.name, \
                                 CommodityModel.image_oss, CommodityModel.price). \
            filter(CartModel.status == 1).filter(CartModel.commodity_id == CommodityModel.id). \
            filter(CartModel.user_id == user_id).all()

        sum = 0
        for i in carts:
            countNum = countNum + 1
            sum = sum + i[2] * i[6]
    else:
        return redirect("/login")
    return render_template("newFront/checkout.html", sum=sum, carts=carts, count=countNum, addresses=address_query)


@bp.route("/blog", methods=['GET', 'POST'])
def blog():
    if request.method == 'POST':
        keyword = request.form.get("blog_search")
        if keyword:
            keyword_sql = "%" + keyword + "%"
            # post_query = PostModel.query.filter(
            #     or_(PostModel.title.like(keyword_sql), PostModel.content.like(keyword_sql)))
            post_query = PostModel.query.filter(PostModel.title.like(keyword_sql))
            page = request.args.get(get_page_parameter(), type=int, default=1)
            # 1：0-9
            # 2：10-19
            start = (page - 1) * current_app.config['PER_PAGE_COUNT']
            end = start + current_app.config['PER_PAGE_COUNT']
            posts = post_query.slice(start, end)
            pagination = Pagination(bs_version=3, page=page, prev_label="上一页")
            context = {
                "posts": posts,
                "pagination": pagination,
                "keyword": keyword,
            }
            return render_template("newFront/blog.html", **context)

    form = PostForm()
    post_query = PostModel.query.order_by(PostModel.gmt_create.desc())
    page = request.args.get(get_page_parameter(), type=int, default=1)
    # 1：0-9
    # 2：10-19
    start = (page - 1) * current_app.config['PER_PAGE_COUNT']
    end = start + current_app.config['PER_PAGE_COUNT']
    posts = post_query.slice(start, end)
    pagination = Pagination(bs_version=3, page=page, prev_label="上一页")
    context = {
        "posts": posts,
        "pagination": pagination,
        "keyword": SearchKeyword.blog_keyword,

    }
    print(form.post_body.data)
    if form.validate_on_submit():
        # those are the codes for post to store into required file
        img_dir = POST_PIC_UPLOAD_DIR
        post_id = shortuuid.uuid()
        # filename = post_id + "." + filename.rsplit('.')[-1]
        # img.save(os.path.join(img_dir, filename))
        # path = os.path.join(img_dir, filename)
        body = form.post_body.data

        title = form.post_title.data
        user_id = session.get("user_id")
        commodity_id = session.get("blog_commodity_id")

        post = PostModel(id=post_id, title=title, content=body, user_id=user_id, commodity_id=commodity_id)
        session.pop("blog_commodity_id", None)

        for img in form.post_pic.data:
            filename = img.filename
            img_id = shortuuid.uuid()
            filename = img_id + "." + filename.rsplit('.')[-1]
            img.save(os.path.join(img_dir, filename))
            path = os.path.join(img_dir, filename)
            auth = oss2.Auth('LTAI5t5rXFCtUG3FiCARAd2d', 'BxxKVg6ox20U0Hev4SnKn8FwYNRP7N')
            bucket = oss2.Bucket(auth, 'http://oss-cn-beijing.aliyuncs.com', 'qintong-edu')
            bucket.create_bucket(oss2.models.BUCKET_ACL_PUBLIC_READ)
            bucket.put_object_from_file(filename, path)
            image_oss = "https://qintong-edu.oss-cn-beijing.aliyuncs.com/{}".format(filename)
            picture_model = PostPictureModel(id=filename, image_oss=image_oss, post_id=post_id)
            db.session.add(picture_model)
            os.remove(os.path.join(img_dir, filename))

        db.session.add(post)
        db.session.commit()
        return redirect("/blog")
    ans = view_cart()
    if not ans is None:
        return render_template("newFront/blog.html", **context, carts=ans[0], sum=ans[1], count=ans[2], form=form)
    else:
        return render_template("newFront/blog.html", **context, count=0, form=form, sum=0)


@bp.route("/shop/<int:type_num>", methods=['GET', 'POST'])
def shop(type_num):
    if request.method == "POST":
        keyword = request.form.get("header_search")
        SearchKeyword.keyword = keyword
        return redirect(url_for("front.shop", type_num=6))

    if type_num == 6:
        keyword = SearchKeyword.keyword
        if keyword != "":
            keyword_sql = "%" + keyword + "%"

            total_query = CommodityModel.query.filter(CommodityModel.name.like(keyword_sql)).all()
            conditional_query = CommodityModel.query.filter(CommodityModel.name.like(keyword_sql)).limit(12).all()
            SearchKeyword.keyword = ""
            intro_query = CommodityIntroductionModel.query.all()
            intro_query_dict = {}
            for i in intro_query:
                if i.intro is not None:
                    intro_query_dict[i.id] = i.intro.lstrip('<p>').rstrip('</p>')[0:100] + "..."
                else:
                    intro_query_dict[i.id] = ''
            page_num = math.ceil(len(total_query) / current_app.config['C_PER_PAGE_COUNT'])

        else:
            total_query = CommodityModel.query.all()
            conditional_query = CommodityModel.query.limit(12).all()
            intro_query = CommodityIntroductionModel.query.all()
            intro_query_dict = {}
            for i in intro_query:
                if i.intro is not None:
                    intro_query_dict[i.id] = i.intro.lstrip('<p>').rstrip('</p>')[0:100] + "..."
                else:
                    intro_query_dict[i.id] = ''

            page_num = math.ceil(len(total_query) / current_app.config['C_PER_PAGE_COUNT'])

    else:
        total_query = CommodityModel.query.filter_by(type=type_num).all()
        conditional_query = CommodityModel.query.filter_by(type=type_num).limit(12).all()
        intro_query = CommodityIntroductionModel.query.all()
        intro_query_dict = {}
        for i in intro_query:
            if i.intro is not None:
                intro_query_dict[i.id] = i.intro.lstrip('<p>').rstrip('</p>')[0:100] + "..."
            else:
                intro_query_dict[i.id] = ''
        page_num = math.ceil(len(total_query) / current_app.config['C_PER_PAGE_COUNT'])

    pipes_number_type = CommodityModel.query.filter_by(type=0).count()
    electronic_number_type = CommodityModel.query.filter_by(type=1).count()
    precussion_number_type = CommodityModel.query.filter_by(type=2).count()
    piano_number_type = CommodityModel.query.filter_by(type=3).count()
    guitar_number_type = CommodityModel.query.filter_by(type=4).count()
    other_number_type = CommodityModel.query.filter_by(type=5).count()

    context = {
        "pipes_number_type": pipes_number_type,
        "electronic_number_type": electronic_number_type,
        "precussion_number_type": precussion_number_type,
        "piano_number_type": piano_number_type,
        "guitar_number_type": guitar_number_type,
        "other_number_type": other_number_type
    }

    ans = view_cart()
    if not ans is None:
        return render_template("newFront/shop.html", conditional_query=conditional_query, carts=ans[0], sum=ans[1],
                               count=ans[2], **context, intro_query_dict=intro_query_dict, page_num=page_num,
                               total_query=total_query)
    else:
        return render_template("newFront/shop.html", conditional_query=conditional_query, count=0, **context,
                               sum=0, intro_query_dict=intro_query_dict, page_num=page_num, total_query=total_query)


@bp.route("/add_chat", methods=['GET', 'POST'])
def add_chat():
    user_id = session.get("user_id")
    chat_staff = session.get("chat_staff")
    if not user_id is None:
        text_of_content = request.form['text of content']
        chat_item = ChatModel(user_id=user_id, status=0, content=text_of_content, staff_id=chat_staff)
        db.session.add(chat_item)
        db.session.commit()
        user_query = UserModel.query.filter_by(id=user_id).first()
        return jsonify({"server_code": 0, "avatar": user_query.avatar})
    else:
        return jsonify({"server_code": 1})


@bp.route("/view_chat_history", methods=['GET', 'POST'])
def view_chat_history():
    user_id = session.get("user_id")
    chat_staff = session.get("chat_staff")
    if not user_id is None:
        chat_query = ChatModel.query.filter_by(user_id=user_id).order_by(ChatModel.gmt_create).all()
        if chat_query is not None and len(chat_query) != 0:
            if chat_staff is None:
                session['chat_staff'] = chat_query[0].staff_id
        else:
            if chat_staff is None:
                staff_all = StaffModel.query.all()
                session['chat_staff'] = staff_all[random.randint(0, len(staff_all)-1)].id
        chat_query_dict = {}
        chat_query_list = []
        chat_query_dict['data'] = chat_query_list
        chat_query_temp = ChatModel.query.filter_by(user_id=user_id, status=1).all()
        user_query = UserModel.query.filter_by(id=user_id).first()
        staff_query = StaffModel.query.filter_by(id=session.get("chat_staff")).first()
        ChatCount.count = len(chat_query_temp)
        for chat in chat_query:
            gmt_create = chat.gmt_create.strftime("%Y-%m-%d %H:%M:%S")
            chat_query_list.append({
                "id": chat.id,
                "userid": chat.user_id,
                "content": chat.content,
                "gmtcreate": gmt_create,
                "staffid": chat.staff_id,
                "status": chat.status,
                "avatar": user_query.avatar,
                "staff-avatar": staff_query.avatar,
            })
        return jsonify({"chat_query_dict": chat_query_dict, "server_code": 0})
    else:
        return jsonify({"server_code": 1})


@bp.route("/update_count", methods=['GET', 'POST'])
def update_count():
    user_id = session.get("user_id")
    chat_staff = session.get("chat_staff")
    chats = ChatModel.query.filter_by(user_id=user_id, status=1).all()
    minus = len(chats) - ChatCount.count
    chats_new = ChatModel.query.filter_by(user_id=user_id, status=1).order_by(ChatModel.gmt_create.desc()).limit(
        minus).all()
    user_query = UserModel.query.filter_by(id=user_id).first()
    staff_query = StaffModel.query.filter_by(id=chat_staff).first()
    ChatCount.count = len(chats)
    chat_query_dict = {}
    chat_query_list = []
    chat_query_dict['data'] = chat_query_list
    for chat in chats_new:
        gmt_create = chat.gmt_create.strftime("%Y-%m-%d %H:%M:%S")
        chat_query_list.append({
            "id": chat.id,
            "userid": chat.user_id,
            "content": chat.content,
            "gmtcreate": gmt_create,
            "staffid": chat.staff_id,
            "status": chat.status,
            "avatar": user_query.avatar,
            "staff-avatar": staff_query.avatar,
        })
    chat_query_list.reverse()
    return jsonify({"server_code": 0, "chat_count": minus, "chat_query_dict": chat_query_dict})


@bp.route('/add_img', methods=['GET', 'POST'])
def add_img():
    user_id = session.get("user_id")
    chat_staff = session.get("chat_staff")
    if not user_id is None:
        img = request.files['img']
        fn_list = img.filename.split(".")
        fn = datetime.now().strftime("%Y%m%d%H%M%S") + "_" + hashlib.md5(fn_list[0].encode('utf8')).hexdigest() + "." + \
             fn_list[1]
        dateNow = datetime.now().strftime("%Y/%m/%d")
        filename = dateNow + "/" + fn
        text_of_context = "<img src=\"https://qintong-edu.oss-cn-beijing.aliyuncs.com/" + filename + "\" alt=\"img\">"
        filePath = os.path.join(current_app.config['CHAT_IMAGE_SAVE_PATH'], fn)
        img.save(filePath)
        bucket.put_object_from_file(filename, filePath)
        chat_item = ChatModel(user_id=user_id, status=0, content=text_of_context, staff_id=chat_staff)
        db.session.add(chat_item)
        db.session.commit()
        user_query = UserModel.query.filter_by(id=user_id).first()
        return jsonify({'server_code': 0, "avatar": user_query.avatar})
    else:
        return jsonify({'server_code': 1})


@bp.route("/sort_method", methods=['GET', 'POST'])
def sort_method():
    method = request.form["method"]
    product_categories = request.form["categories"]
    price_range = request.form["amount"]
    page_num = request.form["page"]

    sql_generator = SearchKeyword()

    sql_generator.page_num = int(page_num)

    if method:
        sql_generator.sort_method = method
    if price_range != "":
        sql_generator.start_price = int(price_range.split()[0].strip('$'))
        sql_generator.end_price = int(price_range.split()[2].strip('$'))

    category_list = []  # [0, 3, 5]
    if product_categories:
        for i in range(len(product_categories)):
            category_list.append(int(product_categories[i]))
        if category_list != sql_generator.type_list:
            sql_generator.type_list = category_list
    else:
        category_list = [0, 1, 2, 3, 4, 5]
        sql_generator.type_list = category_list

    # print(sql_generator.change_sql())
    conditional_query = db.session.execute(sql_generator.change_sql())

    sql_generator.page_num = 0
    sql_generator.sql = "SELECT * FROM commodity"
    total_query = db.session.execute(sql_generator.change_sql())
    total = total_query.rowcount

    conditional_order_query_dict = {}
    conditional_order_query_list = []
    conditional_order_query_dict["data"] = conditional_order_query_list
    for commodity in conditional_query:
        conditional_order_query_list.append({
            "id": commodity.id,
            "name": commodity.name,
            "price": commodity.price,
            "image": commodity.image_oss,
            "type": commodity.type,
            "buy_amount": commodity.buy_amount,
            "visit_amount": commodity.visit_amount,
            "amount": commodity.amount,
            "collect_amount": commodity.collect_amount,
        })

    return jsonify({"conditional_order_query_dict": conditional_order_query_dict, "total": total})


@bp.route("/changepassword", methods=['GET', 'POST'])
def changepassword():
    if request.method == 'GET':
        ans = view_cart()
        if not ans is None:
            return render_template("newFront/change_pass.html", carts=ans[0], sum=ans[1], count=ans[2])
        else:
            return render_template("newFront/change_pass.html", count=0, sum=0)
    else:
        form = ChangePassForm(request.form)
        if form.validate():
            email = form.email.data
            m = hashlib.md5()  # 创建md5对象
            m.update(form.password.data.encode(encoding='utf-8'))
            password = m.hexdigest()
            user = UserModel.query.filter_by(email=email)[0]
            user.password = password
            db.session.commit()
            return restful.ok()
            # else:
            #     return jsonify({"code": "400", "codemessage": "邮箱未注册"})
        else:
            # form.errors中存放了所有的错误信息
            # {'graph_captcha': ['请输入正确长度的图形验证码！', '图形验证码错误！']}
            message = form.messages[0]
            return restful.params_error(message=message)


@bp.get("/email/captcha")
def email_captcha():
    email = request.args.get("email")
    if not email:
        return jsonify({"code": 400, "message": "Please enter the mailbox first"})
    source = list(string.digits)
    captcha = "".join(random.sample(source, 6))
    message = Message(subject="Registration verification code", recipients=[email],
                      body="Your registration verification code is:%s" % captcha)
    try:
        mail.send(message)
        cache.set(email, captcha)
        print(cache.get(email))
    except Exception as e:
        print("Mail sending failed")
        print(e)
        return jsonify({"code": 500, "message": "Mail sending failed!"})
    return jsonify({"code": 200, "message": "Mail sent successfully!"})


# @bp.get("/email/captcha")
# def email_captcha():
#     email = request.args.get("email")
#     if not email:
#         return jsonify({"code": 400, "message": "Please enter the mailbox first"})
#     source = list(string.digits)
#     captcha = "".join(random.sample(source, 6))
#     subject = "Registration verification code"
#     body = "Your registration verification code is:%s" % captcha
#     message = Message(subject="Registration verification code", recipients=[email],
#                       body="Your registration verification code is:%s" % captcha)
#     current_app.celery.send_task("send_mail",(email,subject,body))
#     return jsonify({"code": 200, "message": "Mail sent successfully!"})
#     服务器的网络好
#     配置信息正确
#     # /email/captcha?email=xx@qq.com
#     email = request.args.get("email")


#     if not email:
#         return jsonify({"code": 400, "message": "请先传入邮箱！"})
#     # 随机的六位数字
#     source = list(string.digits)
#     captcha = "".join(random.sample(source, 6))
#     message = Message(subject="【知了传课】注册验证码", recipients=[email], body="【知了传课】您的注册验证码为：%s"%captcha)
#     try:
#         # 发送邮件实际上一个网络请求
#         mail.send(message)
#     except Exception as e:
#         print("邮件发送失败！")
#         print(e)
#         return jsonify({"code": 500, "message": "邮件发送失败！"})
#     return jsonify({"code": 200, "message": "邮件发送成功！"})

# @bp.get("/email/captcha")
# def email_captcha():
#     # /email/captcha?email=xx@qq.com
#     email = request.args.get("email")
#     if not email:
#         return restful.params_error(message="请先传入邮箱！")
#     # 随机的六位数字
#     source = list(string.digits)
#     captcha = "".join(random.sample(source, 6))
#     subject = "【Musical Instrument Shop】注册验证码"
#     body = "【Musical Instrument Shop】您的注册验证码为：%s" % captcha
#     current_app.celery.send_task("send_mail", (email, subject, body))
#     cache.set(email, captcha)
#     print(cache.get(email))
#     return restful.ok(message="邮件发送成功！")


@bp.route("/graph/capthca")
def graph_captcha():
    captcha, image = Captcha.gene_graph_captcha()
    # 将验证码存放到缓存中
    # key, value
    # bytes
    key = md5((captcha + str(time.time())).encode('utf-8')).hexdigest()
    cache.set(key, captcha)
    # with open("captcha.png", "wb") as fp:
    #     image.save(fp, "png")
    out = BytesIO()
    image.save(out, "png")
    # 把out的文件指针指向最开的位置
    out.seek(0)
    resp = make_response(out.read())
    resp.content_type = "image/png"
    resp.set_cookie("_graph_captcha_key", key, max_age=3600)
    return resp


@bp.route("/login", methods=['GET', 'POST'])
def login():
    if request.method == 'GET':
        ans = view_cart()
        if not ans is None:
            return render_template("newFront/sign-in.html", carts=ans[0], sum=ans[1], count=ans[2])
        else:
            return render_template("newFront/sign-in.html", count=0, sum=0)
    else:
        form = LoginForm(request.form)
        if form.validate():
            email = form.email.data
            password = form.password.data
            user = UserModel.query.filter_by(email=email).first()
            remember = form.remember.data

            if not user:
                return restful.params_error("Email or password is incorrect \n邮箱或密码错误")
            if not user.check_password(password):
                # if not user.password == password:
                return restful.params_error("Email or password is incorrect \n邮箱或密码错误")
            # if not user.is_active:
            #     return restful.params_error("此用户不可用！")
            session['user_id'] = user.id
            # 如果是员工，才生成token
            token = ""
            permissions = []
            # if user.is_staff:
            #     token = create_access_token(identity=user.id)
            #     for attr in dir(Permission):
            #         if not attr.startswith("_"):
            #             permission = getattr(Permission, attr)
            #             if user.has_permission(permission):
            #                 permissions.append(attr.lower())
            """
            {"avatar":"677d1194c930361e88189b315e4de934.jpg","comments":[],"email":"hynever@qq.com","id":"fiuhqDhK6Wo6Rb9hHc9ffX","is_active":true,"is_staff":true,"join_time":"2021-11-25 15:35:40","posts":[],"signature":"欢饮刚来到知了传课学习Python","username":"zhiliaochuanke"}
            """
            if remember == 1:
                session.permanent = True
            user_dict = user.to_dict()
            user_dict['permissions'] = permissions
            return restful.ok(data={"token": token, "user": user_dict})
        else:
            return restful.params_error(message=form.messages[0])


@bp.route("/register", methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        ans = view_cart()
        if not ans is None:
            return render_template("newFront/sign-up.html", carts=ans[0], sum=ans[1], count=ans[2])
        else:
            return render_template("newFront/sign-up.html", count=0, sum=0)
    else:
        form = RegisterForm(request.form)
        if form.validate():
            email = form.email.data
            username = form.username.data
            m = hashlib.md5()  # 创建md5对象
            m.update(form.password.data.encode(encoding='utf-8'))
            password = m.hexdigest()
            # password = form.password.data.generate(text=md5(email.encode("utf-8")).hexdigest())
            identicon = Identicon()
            filenames = identicon.generate(text=md5(email.encode("utf-8")).hexdigest())
            avatar = filenames[2]
            user = UserModel(email=email, username=username, password=password, avatar=avatar)
            db.session.add(user)
            db.session.commit()
            return restful.ok()
        else:
            # form.errors中存放了所有的错误信息
            # {'graph_captcha': ['请输入正确长度的图形验证码！', '图形验证码错误！']}
            message = form.messages[0]
            return restful.params_error(message=message)


@bp.route("/logout")
def logout():
    session.clear()
    return redirect("/login")


# @bp.route("/setting")
# @login_required
# def setting():
#     email_hash = md5(g.user.email.encode("utf-8")).hexdigest()
#     return render_template("front/setting.html", email_hash=email_hash)


@bp.get("/order_detail/<order_id>")
@login_required
def order_detail(order_id):
    user_id = session.get("user_id")
    ans = view_cart()
    # comds = CommodityModel.query.filter(CommodityModel.discount == 1).all()
    # for comd in comds:
    #     comd.price *= 0.8
    items = CartModel.query.filter_by(order_id=order_id)
    checkout_sum = 0
    for item in items:
        checkout_sum += item.commodity.price * item.amount
    order = OrderModel.query.filter_by(id=order_id).first()
    if not ans is None:
        return render_template("newFront/order_detail.html", items=items, checkout_sum=checkout_sum, order=order,
                               carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return render_template("newFront/order_detail.html", items=items, checkout_sum=checkout_sum, order=order,
                               count=0, sum=0)


@bp.route("/order", methods=['GET', 'POST'])
@login_required
def order_list():
    nations = NationModel.query.all()
    nation_dic = {}
    status_dic = {0: "normal", 1: "refunding", 2: "changing", 3: "refunded"}
    flowstatus_dic = {0: "ordered", 1: "delivering", 2: "reached", 3: "signed", 4: "refunded", 5: "pickup"}
    for nation in nations:
        nation_dic[nation.id] = nation.name
    user_id = session.get("user_id")

    ans = view_cart()
    if not ans is None:
        user = UserModel.query.filter_by(id=user_id).first()
        orders = OrderModel.query.filter_by(user_id=user_id).order_by(OrderModel.gmt_modify.desc()).all()
        return render_template("newFront/order.html", user=user, orders=orders, carts=ans[0], sum=ans[1],
                               count=ans[2], status_dic=status_dic, flowstatus_dic=flowstatus_dic,
                               nation_dic=nation_dic)
    else:
        user = UserModel.query.filter_by(id=user_id).first()
        orders = OrderModel.query.filter_by(user_id=user_id).order_by(OrderModel.gmt_modify.desc()).all()
        return render_template("newFront/order.html", user=user, orders=orders, count=0, status_dic=status_dic,
                               sum=0, flowstatus_dic=flowstatus_dic, nation_dic=nation_dic)


@bp.post("/avatar/upload")
@login_required
def upload_avatar():
    form = UploadImageForm(request.files)
    user_id = session.get("user_id")
    if form.validate():
        image = form.image.data
        # 不要使用用户上传上来的文件名，否则容易被黑客攻击
        filename = image.filename
        # xxx.png,xx.jpeg
        _, ext = os.path.splitext(filename)
        filename = md5((g.user.email + str(time.time())).encode("utf-8")).hexdigest() + ext
        image_path = os.path.join(current_app.config['AVATARS_SAVE_PATH'], filename)
        image.save(image_path)
        fn = datetime.now().strftime("%Y/%m/%d") + "/" + filename
        bucket.put_object_from_file(fn, image_path)
        avatar_context = "https://qintong-edu.oss-cn-beijing.aliyuncs.com/" + fn
        user_query = UserModel.query.filter_by(id=user_id).first()
        user_query.avatar = avatar_context
        db.session.commit()
        return restful.ok(data={"avatar": filename, "location": image_path})
    else:
        message = form.messages[0]
        return restful.params_error(message=message)


# @bp.post("/profile/edit")
# @login_required
# def edit_profile():
#     form = EditProfileForm(request.form)
#     if form.validate():
#         signature = form.signature.data
#         g.user.signature = signature
#         db.session.commit()
#         return restful.ok()
#     else:
#         return restful.params_error(message=form.messages[0])


# @bp.post("/blog/setting_upload_pic")
# @login_required
# def upload_blog_picture():
#     post_id = request.form['blog_id']
#     name = shortuuid.uuid()
#     file = request.form['location']
#     auth = oss2.Auth('LTAI5t5rXFCtUG3FiCARAd2d', 'BxxKVg6ox20U0Hev4SnKn8FwYNRP7N')
#     bucket = oss2.Bucket(auth, 'http://oss-cn-beijing.aliyuncs.com', '<qintong-edu>')
#     bucket.create_bucket(oss2.models.BUCKET_ACL_PRIVATE)
#     bucket.put_object_from_file(name, file)
#     image_oss = "https://qintong-edu.oss-cn-beijing.aliyuncs.com/{}".format(name)
#     picture = PostPictureModel(id=name, image_oss=image_oss, post_id=post_id)
#     db.session.add(picture)
#     db.session.commit()
#     return jsonify({"code": 200, "message": "Blog picture upload successfully!"})


@bp.post("/post/image/upload")
@login_required
def upload_post_image():
    form = UploadImageForm(request.files)
    if form.validate():
        image = form.image.data
        # 不要使用用户上传上来的文件名，否则容易被黑客攻击
        filename = image.filename
        # xxx.png,xx.jpeg
        _, ext = os.path.splitext(filename)
        filename = md5((g.user.email + str(time.time())).encode("utf-8")).hexdigest() + ext
        image_path = os.path.join(current_app.config['POST_IMAGE_SAVE_PATH'], filename)
        image.save(image_path)
        # {"data","code", "message"}
        return jsonify({"errno": 0, "data": [{
            "url": url_for("media.get_post_image", filename=filename),
            "alt": filename,
            "href": ""
        }]})
    else:
        message = form.messages[0]
        return restful.params_error(message=message)


@bp.get("/post/detail/<post_id>")
def post_detail(post_id):
    post_model = PostModel.query.get(post_id)
    comment_count = PostCommentModel.query.filter_by(post_id=post_id).count()
    context = {
        "comment_count": comment_count,
        "post": post_model
    }
    return render_template("front/post_detail.html", **context)


@bp.post("/product_comment")
@login_required
def product_comment():
    form = ProductCommentForm(request.form)
    if form.validate():
        content = form.content.data
        commodity_id = form.commodity_id.data
        user_id = session.get("user_id")
        if not user_id:
            return restful.params_error(message="Not login")
        comment = CommodityCommentModel(content=content, commodity_id=commodity_id, user_id=user_id)
        db.session.add(comment)
        db.session.commit()
        return restful.ok()
    else:
        message = form.messages[0]
        return restful.params_error(message=message)


@bp.post("/comment")
@login_required
def public_comment():
    form = PublicCommentForm(request.form)
    if form.validate():
        content = form.content.data
        post_id = form.post_id.data
        try:
            post_model = PostModel.query.get(post_id)
        except Exception as e:
            return restful.params_error(message="the blog do not exist")
        comment = PostCommentModel(content=content, post_id=post_id, user_id=g.user.id)
        db.session.add(comment)
        db.session.commit()
        return restful.ok()
    else:
        message = form.messages[0]
        return restful.params_error(message=message)


@bp.route("/write_blog", methods=['GET', 'POST'])
@login_required
def write_blog():
    if g.user is None:
        return restful.params_error(message="Please login first")
    commodity_id = request.form['commodity_id']
    session["blog_commodity_id"] = commodity_id
    return restful.ok()


@bp.route("/add_comment_like", methods=['GET', 'POST'])
@login_required
def add_comment_like():
    user_id = request.form['user_id']
    comment_id = request.form['comment_id']
    # app.log("aaaa")
    like_in_db = PostCommentLikeModel.query.filter(and_(PostCommentLikeModel.user_id == user_id,
                                                        PostCommentLikeModel.post_comment_id == comment_id)).first()

    if not like_in_db:
        like = PostCommentLikeModel(user_id=user_id, post_comment_id=comment_id)
        db.session.add(like)
        comment_in_db = PostCommentModel.query.filter(PostCommentModel.id == comment_id).first()
        like_amount = PostCommentLikeModel.query.filter(
            PostCommentLikeModel.post_comment_id == comment_in_db.id).count()
        comment_in_db.like_amount = like_amount
        db.session.commit()
        return jsonify({'text': 'like', "like_amount": like_amount})
    else:
        db.session.delete(like_in_db)
        comment_in_db = PostCommentModel.query.filter(PostCommentModel.id == comment_id).first()
        like_amount = PostCommentLikeModel.query.filter(
            PostCommentLikeModel.post_comment_id == comment_in_db.id).count()
        comment_in_db.like_amount = like_amount
        db.session.commit()
        return jsonify({'text': 'cancel', "like_amount": like_amount})


@bp.route("/add_commodity_comment_like", methods=['GET', 'POST'])
@login_required
def add_commodity_comment_like():
    user_id = request.form['user_id']
    comment_id = request.form['comment_id']
    like_in_db = CommodityCommentLikeModel.query.filter(and_(CommodityCommentLikeModel.user_id == user_id,
                                                             CommodityCommentLikeModel.comment_id == comment_id)).first()

    if not like_in_db:
        like = CommodityCommentLikeModel(user_id=user_id, comment_id=comment_id)
        db.session.add(like)
        comment_in_db = CommodityCommentModel.query.filter(CommodityCommentModel.id == comment_id).first()
        like_amount = CommodityCommentLikeModel.query.filter(
            CommodityCommentLikeModel.comment_id == comment_in_db.id).count()
        comment_in_db.like_amount = like_amount
        db.session.commit()
        return jsonify({'text': 'like', "like_amount": like_amount})
    else:
        db.session.delete(like_in_db)
        comment_in_db = CommodityCommentModel.query.filter(CommodityCommentModel.id == comment_id).first()
        like_amount = CommodityCommentLikeModel.query.filter(
            CommodityCommentLikeModel.comment_id == comment_in_db.id).count()
        comment_in_db.like_amount = like_amount
        db.session.commit()
        return jsonify({'text': 'cancel', "like_amount": like_amount})


@bp.route("/avatar/<filename>")
def get_avatar(filename):
    user_id = session.get("user_id")
    user = UserModel.query.filter_by(id=user_id).first()
    imagelink = user.avatar
    # return send_from_directory(current_app.config['AVATARS_SAVE_PATH'], filename)
    return render_template("newFront/setting.html", imagelink)


@bp.route("/post/<filename>")
def get_post_image(filename):
    user_id = session.get("user_id")
    image_oss = "https://qintong-edu.oss-cn-beijing.aliyuncs.com/{}".format(filename)
    user = UserModel.query.filter_by(id=user_id)[0]
    user.avatar = image_oss
    db.session.commit()
    return send_from_directory(current_app.config['POST_IMAGE_SAVE_PATH'], filename)


@bp.route("/terms&conditions")
def get_terms_conditions():
    return render_template("newFront/terms_conditions.html")


# @bp.route("/setting")
# @login_required
# def setting():
#     user_id = session.get("user_id")
#     if user_id:
#         address = db.session.query(AddressModel.address1, AddressModel.address2). \
#             filter_by(user_id=user_id).first()
#         if address:
#             context = {
#                 # "nation_id": address[0],
#                 "address1": address[0],
#                 "address2": address[1],
#             }
#         return render_template("front/setting.html", **context)
#     else:
#         return redirect("/login")
#
@bp.route("/setting", methods=['GET', 'POST'])
@login_required
def setting():
    user_id = session.get("user_id")
    if user_id:
        address_query = []
        addresses = AddressModel.query.filter_by(user_id=user_id, deleted=0).order_by(
            AddressModel.gmt_create.desc()).all()
        for address in addresses:
            nation_name = NationModel.query.filter_by(id=address.nation_id).first().name
            address_query.append({
                "id": address.id,
                "first_name": address.first_name,
                "last_name": address.last_name,
                "nation_name": nation_name,
                "address1": address.address1,
                "address2": address.address2,
                "phone_number": address.phone_number
            })
        nations = NationModel.query.order_by(NationModel.name).all()
        user_query = UserModel.query.filter_by(id=user_id).first()
        imagelink = user_query.avatar
        ans = view_cart()
        return render_template("newFront/setting.html", addresses=address_query, nations=nations
                               , imagelink=imagelink, carts=ans[0], sum=ans[1], count=ans[2])
    else:
        return redirect("/login")


# @bp.route("/post_setting")
# @login_required
# def post_setting():
#     form = SettingForm(request.form)
#     if form.validate():
#         nation_id = form.nation.data
#         address1 = form.address1.data
#         address2 = form.address2.data
#         # address1 = request.form['address1']
#         # address2 = request.form['address2']
#         addre = AddressModel(address1=address1, address2=address2)
#         db.session.add(addre)
#         db.session.commit()
#         return restful.ok()
#     else:
#         message = form.messages[0]
#         return restful.params_error(message=message)


@bp.route("/add_setting", methods=['GET', 'POST'])
@login_required
def add_setting():
    user_id = session.get("user_id")
    country = request.form['country']
    address1 = request.form['address1']
    address2 = request.form['address2']
    first_name = request.form['first_name']
    last_name = request.form['last_name']
    phone = request.form['phone']
    nation_name = NationModel.query.filter_by(id=country).first().name
    post_model = AddressModel(nation_id=country, address1=address1, address2=address2, user_id=user_id,
                              first_name=first_name,
                              last_name=last_name, phone_number=phone, deleted=0)
    db.session.add(post_model)
    db.session.commit()
    return jsonify(
        {"code": 200, "message": "Setting update success", "nation_name": nation_name, "address-id": post_model.id})


@bp.route("/star_record", methods=['GET', 'POST'])
def star_record():
    user_id = session.get("user_id")
    if not user_id is None:
        star = request.form['score']
        product_id = request.form['id']
        star_query = CommodityLike.query.filter_by(user_id=user_id, commodity_id=product_id).first()
        if star_query is not None:
            star_query.star = star
            star_query.gmt_modify = datetime.now()
            db.session.commit()
        else:
            star_new = CommodityLike(user_id=user_id, commodity_id=product_id, star=star)
            db.session.add(star_new)
            db.session.commit()
        return jsonify({"server_code": 0})
    else:
        return jsonify({"server_code": 1})


@bp.route("/delete_address", methods=['POST', 'GET'])
def delete_address():
    address_id = request.form["address_id"]
    address_query = AddressModel.query.filter_by(id=address_id).first()
    if address_query is not None:
        address_query.deleted = 1
        db.session.commit()
        return jsonify({'server_code': 0})
    else:
        return jsonify({'server_code': 1})


@bp.route("/get_star_view", methods=['GET', 'POST'])
def get_star_view():
    product_id_before = request.form['product_id']
    product_id = ''.join(filter(str.isdigit, product_id_before))
    star = 0.0
    commodity_like = CommodityLike.query.filter_by(commodity_id=product_id).all()
    if len(commodity_like) != 0:
        starSum = 0
        for i in commodity_like:
            starSum = starSum + i.star
        star = starSum / len(commodity_like)
    return jsonify({"server_code": 200, "star": star})


@bp.route("/search", methods=['GET', 'POST'])
def search():
    if request.method == "POST":
        keyword = request.form.get("header_search")
        SearchKeyword.keyword = keyword
        return redirect(url_for("front.shop", type_num=6))