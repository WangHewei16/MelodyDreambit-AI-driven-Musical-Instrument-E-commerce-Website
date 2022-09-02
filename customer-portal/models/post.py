import shortuuid

from exts import db
from datetime import datetime


class NationModel(db.Model):
    __tablename__ = "nation"
    id = db.Column(db.String(72), primary_key=True, nullable=False)
    name = db.Column(db.String(72), nullable=False)


class PostModel(db.Model):
    __tablename__ = "post"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    title = db.Column(db.String(144), nullable=False)
    content = db.Column(db.Text, nullable=False)
    user_id = db.Column(db.String(72), db.ForeignKey("user.id"))
    commodity_id = db.Column(db.String(72), nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)
    author = db.relationship("UserModel", backref=db.backref("posts"))


class PostCommentModel(db.Model):
    __tablename__ = "post_comment"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    content = db.Column(db.Text, nullable=False)
    post_id = db.Column(db.VARCHAR(72), db.ForeignKey("post.id"))
    user_id = db.Column(db.VARCHAR(72), db.ForeignKey("user.id"), nullable=False)
    like_amount = db.Column(db.Integer, default=0)
    gmt_create = db.Column(db.DateTime, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, default=datetime.now)
    post = db.relationship("PostModel", backref=db.backref('comments'))
    author = db.relationship("UserModel", backref='comments')


class PostCommentLikeModel(db.Model):
    __tablename__ = 'post_comment_like'
    id = db.Column(db.VARCHAR(72), primary_key=True, default=shortuuid.uuid)
    post_comment_id = db.Column(db.VARCHAR(72), db.ForeignKey("post_comment.id"))
    user_id = db.Column(db.VARCHAR(72), nullable=False)
    gmt_create = db.Column(db.DateTime, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, default=datetime.now)


class PostPictureModel(db.Model):
    __tablename__ = "post_picture"
    id = db.Column(db.String(72), primary_key=True, nullable=False)
    image_oss = db.Column(db.String(200))
    post_id = db.Column(db.VARCHAR(72), db.ForeignKey("post.id"))
    post = db.relationship("PostModel", backref=db.backref("post_picture"))


class StatisticsModel(db.Model):
    __tablename__ = "statistics_daily"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    date_calculated = db.Column(db.String(20), nullable=False)
    register_num = db.Column(db.Integer, nullable=False)
    commodity_visit_num = db.Column(db.Integer, nullable=False)
    commodity_buy_num = db.Column(db.Integer, nullable=False)
    post_num = db.Column(db.Integer, nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)


class AddressModel(db.Model):
    __tablename__ = "address"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    user_id = db.Column(db.String(72), nullable=False)
    nation_id = db.Column(db.String(72),db.ForeignKey("nation.id"), nullable=False)
    address1 = db.Column(db.String(144), nullable=False)
    address2 = db.Column(db.String(144), nullable=False)
    first_name = db.Column(db.String(72), nullable=False)
    last_name = db.Column(db.String(72), nullable=False)
    phone_number = db.Column(db.String(72), nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    deleted = db.Column(db.Integer, nullable=False)

class BannerModel(db.Model):
    __tablename__ = "banner"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    name = db.Column(db.String(72), nullable=False)
    image_url = db.Column(db.String(72), nullable=False)
    link_url = db.Column(db.String(72), nullable=False)
    priority = db.Column(db.Integer)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)


class OrderModel(db.Model):
    __tablename__ = "orders"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    address_id = db.Column(db.String(72), db.ForeignKey("address.id"), nullable=False)
    user_id = db.Column(db.String(72), db.ForeignKey("user.id"), nullable=False)
    # address1 = db.Column(db.String(144), nullable=False)
    # address2 = db.Column(db.String(144), nullable=False)
    # first_name = db.Column(db.String(72), nullable=False)
    # last_name = db.Column(db.String(72), nullable=False)
    method = db.Column(db.Integer, nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)
    status = db.Column(db.Integer, nullable=False,default=0)
    flowstatus = db.Column(db.Integer, nullable=False,default=0)
    priority = db.Column(db.Integer, nullable=False, default=0)
    address = db.relationship("AddressModel", backref=db.backref("orders"))
    user = db.relationship("UserModel", backref=db.backref("orders"))


class CartModel(db.Model):
    __tablename__ = "cart"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    user_id = db.Column(db.String(72), nullable=False)
    commodity_id = db.Column(db.String(72), db.ForeignKey("commodity.id"), nullable=False)
    amount = db.Column(db.Integer, nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)
    status = db.Column(db.Integer, nullable=False)
    order_id = db.Column(db.String(72), nullable=False)
    commodity = db.relationship("CommodityModel", backref=db.backref("cart"))


class CommodityModel(db.Model):
    __tablename__ = "commodity"
    id = db.Column(db.String(72), primary_key=True, nullable=False)
    name = db.Column(db.String(72), nullable=False)
    price = db.Column(db.Integer, nullable=False)
    image_oss = db.Column(db.String(200))
    discount= db.Column(db.Integer, nullable=False)
    amount = db.Column(db.Integer, nullable=False)
    type = db.Column(db.Integer, nullable=False)
    collect_amount = db.Column(db.Integer, nullable=False)
    visit_amount = db.Column(db.Integer, nullable=False)
    buy_amount = db.Column(db.Integer, nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)


class CommodityCommentModel(db.Model):
    __tablename__ = "commodity_comment"
    id = db.Column(db.Integer, primary_key=True, nullable=False, default=shortuuid.uuid)
    content = db.Column(db.Text, nullable=False)
    commodity_id = db.Column(db.String(72),db.ForeignKey("commodity.id"), nullable=False)
    user_id = db.Column(db.String(72),db.ForeignKey("user.id"), nullable=False)
    like_amount = db.Column(db.Integer, nullable=False,default=0)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime,nullable=False, default=datetime.now)
    commodity = db.relationship("CommodityModel", backref='commodity_comments')
    author = db.relationship("UserModel", backref='commodity_comments')


class CommodityCommentLikeModel(db.Model):
    __tablename__ = "commodity_comment_like"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    user_id = db.Column(db.String(72), nullable=False)
    comment_id = db.Column(db.String(72),db.ForeignKey("commodity_comment.id"), nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)



class CommodityIntroductionModel(db.Model):
    __tablename__ = "commodity_introduction"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    intro = db.Column(db.Text)


class CommodityLike(db.Model):
    __tablename__ = "commodity_like"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    user_id = db.Column(db.String(72), nullable=False)
    commodity_id = db.Column(db.String(72), nullable=False)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)
    star = db.Column(db.Float, nullable=False)


class SearchKeyword:
    blog_keyword = ""
    keyword = ""
    type_list = []
    start_price = 0
    end_price = 10000
    sort_method = ""
    page_num = 1
    #(page_num-1) * (12)  (page_num-1) * (12) + 12

    # sql = "SELECT * FROM commodity WHERE price>%d AND price<%d" % (start_price, end_price)
    sql = "SELECT * FROM commodity"

    def change_sql(self):
        self.sql += " WHERE price>%d AND price<%d" % (self.start_price, self.end_price)
        if self.keyword != "" and self.keyword is not None:
            self.sql += " AND name LIKE " + "'%" + self.keyword + "%'"
        if len(self.type_list) != 0:
            self.sql += " AND"
            for type in self.type_list:
                if type == self.type_list[0]:
                    self.sql += " ("
                if type == self.type_list[-1]:
                    self.sql += " type=%d )" % type
                else:
                    self.sql += " type=%d OR" % type
        if self.sort_method != "":
            if self.sort_method == "Default sorting" or self.sort_method == "默认排序":
                self.sql += " ORDER BY gmt_create"
            else:
                self.sql += " ORDER BY price %s" % self.sort_method
        if self.page_num != 0:
            self.sql += " LIMIT %d, %d" % ((self.page_num-1) * 12, 12)
        return self.sql

    # sql = "SELECT * FROM commodity WHERE type IN (1,4,5) AND price>980 AND price<1000 ORDER BY price DESC"


class ChatModel(db.Model):
    __tablename__ = "chat"
    id = db.Column(db.String(72), primary_key=True, nullable=False, default=shortuuid.uuid)
    user_id = db.Column(db.String(72), nullable=False)
    content = db.Column(db.Text)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    staff_id = db.Column(db.String(72), nullable=False)
    status = db.Column(db.String(72), nullable=False)


class ChatCount:
    count = 0
    firstCount = False


class StaffModel(db.Model):
    __tablename__ = "staff"
    id = db.Column(db.String(72), primary_key=True, nullable=False)
    openid = db.Column(db.String(128))
    avatar = db.Column(db.String(255), nullable=False)
    nickname = db.Column(db.String(50))
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now())
    gmt_modify = db.Column(db.DateTime, nullable=False)
