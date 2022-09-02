import hashlib

from exts import db
import shortuuid
from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy_serializer import SerializerMixin

# 1&1 = 1， 1&0=0， 0&0=0
# 0b00000111

class Permission(object):
    # 255的二进制方式来表示 1111 1111
    ALL_PERMISSION = 0b11111111
    # 1. 访问者权限
    VISITOR =        0b00000001
    # 2. 管理帖子权限
    POST =         0b00000010
    # 3. 管理评论的权限
    COMMENT =      0b00000100
    # 4. 管理板块的权限
    BANNER =        0b00001000
    # 5. 管理前台用户的权限
    USER =      0b00010000
    # 6. 管理后台管理员的权限
    STAFF =        0b01000000


class RoleModel(db.Model, SerializerMixin):
    serialize_only = ("id", "name", "desc", "create_time")
    # __tablename__ = 'role'
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(50), nullable=False)
    desc = db.Column(db.String(200),nullable=True)
    create_time = db.Column(db.DateTime,default=datetime.now)
    permissions = db.Column(db.Integer,default=Permission.VISITOR)


class UserModel(db.Model, SerializerMixin):
    __tablename__ = "user"
    id = db.Column(db.String(72), primary_key=True, nullable=False,default=shortuuid.uuid)
    email = db.Column(db.String(50), nullable=False)
    username = db.Column(db.String(50), nullable=False)
    password = db.Column(db.String(72), nullable=False)
    avatar = db.Column(db.String(100))
    signature = db.Column(db.String(100))
    is_active = db.Column(db.Integer)
    gmt_create = db.Column(db.DateTime, nullable=False, default=datetime.now)
    gmt_modify = db.Column(db.DateTime, nullable=False, default=datetime.now)
    # is_active = db.Column(db.Boolean, default=True)
    # role_id = db.Column(db.Integer, db.ForeignKey("role.id"))
    # role = db.relationship("RoleModel", backref="users")

    def __init__(self, *args, **kwargs):
        if "password" in kwargs:
            self.password = kwargs.get('password')
            kwargs.pop("password")
        super(UserModel, self).__init__(*args, **kwargs)

    # @property
    # def password(self):
    #     return self._password
    #
    # @password.setter
    # def password(self, newpwd):
    #     self._password = generate_password_hash(newpwd)

    def check_password(self, rawpwd):
        m = hashlib.md5()  # 创建md5对象
        m.update(rawpwd.encode(encoding='utf-8'))
        rawpwd = m.hexdigest()
        return self.password == rawpwd

    def has_permission(self, permission):
        # 当前用户所拥有的权限&permission = permission
        # 0b011 & 0b001 = 0b001
        # 0b011 & 0b100 = 0b000
        return (self.role.permissions & permission) == permission
