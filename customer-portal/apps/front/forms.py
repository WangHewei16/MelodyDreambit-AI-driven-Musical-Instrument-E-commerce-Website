# pip install flask-wtf
# flask-wtf基于wtforms
from flask_wtf import FlaskForm
from wtforms import Form, ValidationError, TextAreaField, SubmitField, MultipleFileField, BooleanField, RadioField
from wtforms import Form, ValidationError, TextAreaField, SubmitField, PasswordField
from wtforms.fields import StringField, IntegerField, FileField
from wtforms.validators import Email, Length, EqualTo, InputRequired, DataRequired
from flask_wtf.file import FileAllowed, FileSize
from wtf_tinymce.forms.fields import TinyMceField

from models.auth import UserModel
from exts import cache

from flask import request


class BaseForm(Form):
    @property
    def messages(self):
        message_list = []
        if self.errors:
            for errors in self.errors.values():
                message_list.extend(errors)
        return message_list


class RegisterForm(BaseForm):
    # 如果使用了Email这个validator，那么就必须要安装email_validator
    # pip install email_validator
    email = StringField(validators=[Email(message="Please enter a correct email" + '\n' + "请输入正确的邮箱")])
    email_captcha = StringField(validators=[Length(6, 6, message="Please enter a email with correct format" + '\n' + "请输入正确格式的邮箱")])
    username = StringField(validators=[Length(3, 20, message="Please enter a username with correct length" + '\n' + "请输入正确长度的用户名")])
    password = PasswordField(validators=[Length(6, 20, message="Please enter a password with correct length" + '\n' + "请输入正确长度的密码")])
    repeat_password = PasswordField(validators=[EqualTo("password", message="Two passwords are inconsistent" + '\n' + "两次密码不一致")])
    graph_captcha = StringField(validators=[Length(4, 4, message="Please enter a graphic verification code with correct length" + '\n' + "请输入正确长度的图形验证码")])
    # agree = RadioField()

    def validate_email(self, field):
        email = field.data
        user = UserModel.query.filter_by(email=email).first()
        if user:
            raise ValidationError(message="Email has been registered" + '\n' + "邮箱已经被注册")

    def validate_email_captcha(self, field):
        email_captcha = field.data
        email = self.email.data
        cache_captcha = cache.get(email)
        if not cache_captcha or email_captcha != cache_captcha:
            raise ValidationError(message="Email verification code is incorrect" + '\n' + "邮箱验证码错误")

    def validate_graph_captcha(self, field):
        key = request.cookies.get("_graph_captcha_key")
        cache_captcha = cache.get(key)
        graph_captcha = field.data
        if not cache_captcha or cache_captcha.lower() != graph_captcha.lower():
            raise ValidationError(message="Graphic verification code is incorrect" + '\n' + "图形验证码错误")

    # def validate_agree(self, field):
    #     agree = field.data
    #     if not agree:
    #         raise ValidationError(message="请同意用户条款与条件!")

class LoginForm(BaseForm):
    email = StringField(validators=[Email(message="Please enter a correct email" + '\n' + "请输入正确的邮箱")])
    password = StringField(validators=[Length(6, 20, message="Please enter a password with correct length" + '\n' + "请输入正确长度的密码")])
    remember = IntegerField()


class ChangePassForm(BaseForm):
    email = StringField(validators=[Email(message="Please enter a correct email" + '\n' + "请输入正确的邮箱")])
    email_captcha = StringField(validators=[Length(6, 6, message="Please enter a correct email with correct format" + '\n' + "请输入正确格式的邮箱验证码")])
    password = StringField(validators=[Length(6, 20, message="Please enter a password with correct length" + '\n' + "请输入正确长度的密码")])
    repeat_password = StringField(validators=[EqualTo("password", message="Two passwords are inconsistent" + '\n' + "两次密码不一致")])
    graph_captcha = StringField(validators=[Length(4, 4, message="Please enter a graphic verification code with correct length" + '\n' +"请输入正确长度的图形验证码")])

    def validate_email(self, field):
        email = field.data
        user = UserModel.query.filter_by(email=email).first()
        if not user:
            raise ValidationError(message="Email is not registered" + '\n' + "邮箱未被注册")

    def validate_email_captcha(self, field):
        email_captcha = field.data
        email = self.email.data
        cache_captcha = cache.get(email)
        if not cache_captcha or email_captcha != cache_captcha:
            raise ValidationError(message="Email verification code is incorrect" + '\n' + "邮箱验证码错误")

    def validate_graph_captcha(self, field):
        key = request.cookies.get("_graph_captcha_key")
        cache_captcha = cache.get(key)
        graph_captcha = field.data
        if not cache_captcha or cache_captcha.lower() != graph_captcha.lower():
            raise ValidationError(message="Graphic verification code is incorrect" + '\n' + "图形验证码错误")


class UploadImageForm(BaseForm):
    image = FileField(validators=[FileAllowed(['jpg', 'jpeg', 'png'], message="Picture format does not meet the requirements"+ '\n' + "图片格式不符合要求"),
                                  FileSize(max_size=1024 * 1024 * 5, message="Maximum size of the picture cannot exceed 5M" + '\n' + "图片最大不能超过5M")])


class EditProfileForm(BaseForm):
    signature = StringField(validators=[Length(min=1, max=50, message="个性签名长度在1-50字之间")])


class PostForm(FlaskForm):
    post_title = StringField("Title", validators=[DataRequired(),Length(min=1, max=30, message="Title should not longer than 200 characters"+"\n"+
                                                                        "标题长度不应该长于30个字符")])
    post_body = TinyMceField(
        'My WTF TinyMCE Field label',
        tinymce_options={'toolbar': 'bold italic | link | code'}
    )
    # post_body = TextAreaField('Body', validators=[DataRequired()])
    post_pic = MultipleFileField('Your post\'s picture'+"\n"+"你的博客的图片", validators=[DataRequired()])
    submit = SubmitField('Post a Blog'+"\n"+"发布")


class PublicCommentForm(BaseForm):
    content = StringField(validators=[InputRequired(message="Please enter some content"+ '\n' + "请输入一些内容")])
    post_id = StringField(validators=[InputRequired(message="Please enter blog id"+ '\n' + "请选择一个买家秀")])


class ProductCommentForm(BaseForm):
    content = StringField(validators=[InputRequired(message="Please enter some content" + '\n' + "请输入一些内容")])
    commodity_id = StringField(validators=[InputRequired(message="Please enter blog id" + '\n' + "请选择一个买家秀")])


class SettingForm(BaseForm):
    nation = StringField("Nation", validators=[DataRequired()])
    address1 = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
    address2 = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
    image_oss = FileField("Image", validators=[DataRequired()])


class CheckoutForm(BaseForm):
    nation = IntegerField("Nation",validators=[DataRequired()])
    address1 = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
    address2 = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
    first_name = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
    last_name = StringField(validators=[Length(1, 50, message="Please enter the range of 1-50 words" + '\n' + "请输入1-50个字之间范围内的")])
