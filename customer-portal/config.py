import os.path
from datetime import timedelta

SECRET_KEY = "SpaceX"

# 项目根路径
BASE_DIR = os.path.dirname(__file__)



DB_USERNAME = 'root'
DB_PASSWORD = 'i4gotitABC'
DB_HOST = '124.222.119.139'
DB_PORT = '3306'
DB_NAME = 'instrument'
POST_PIC_UPLOAD_DIR = os.path.join(BASE_DIR, 'static/assets/img')

# session.permanent = True的情况下的过期时间
PERMANENT_SESSION_LIFETIME = timedelta(days=7)

DB_URI = 'mysql+pymysql://%s:%s@%s:%s/%s?charset=utf8mb4' % (DB_USERNAME, DB_PASSWORD, DB_HOST, DB_PORT, DB_NAME)

SQLALCHEMY_DATABASE_URI = DB_URI
SQLALCHEMY_TRACK_MODIFICATIONS = False

# LANGUAGE = ['zh_CN', 'en']
# BABEL_DEFAULT_LOCALE = 'zh_CN'
# BABEL_DEFAULT_TIMEZONE='UTC'
# BABEL_TRANSLATION_DIRECTORIES='**/translations'


# QQ邮箱配置
# MAIL_SERVER = "smtp.qq.com"
# MAIL_USE_TLS = True
# MAIL_PORT = 587
# MAIL_USERNAME = "1724916415@qq.com"
# MAIL_PASSWORD = "mstlbhsdboamgcci"
# MAIL_DEFAULT_SENDER = "1724916415@qq.com"

# QQ邮箱配置
MAIL_SERVER = "smtp.qq.com"
MAIL_PORT = 587
MAIL_USE_TLS = True
MAIL_USERNAME = "737272215@qq.com"
MAIL_PASSWORD = "lusshabpakmwbgad"
MAIL_DEFAULT_SENDER = "737272215@qq.com"

# mstlbhsdboamgcci

# Celery的redis配置
# CELERY_BROKER_URL = 'redis://127.0.0.1:6379/0'
# CELERY_RESULT_BACKEND = 'redis://127.0.0.1:6379/0'
CELERY_BROKER_URL = 'redis://:i4gotitABC@8.130.13.122:6379/0'
CELERY_RESULT_BACKEND = 'redis://:i4gotitABC@8.130.13.122:6379/0'


# Flask-Caching的配置
CACHE_TYPE = "RedisCache"
CACHE_DEFAULT_TIMEOUT = 300
# CACHE_REDIS_HOST = "127.0.0.1"
CACHE_REDIS_HOST = "8.130.13.122"
CACHE_REDIS_PORT = 6379
CACHE_REDIS_PASSWORD = "i4gotitABC"

# 头像配置
AVATARS_SAVE_PATH = os.path.join(BASE_DIR, "media", "avatars")

# 帖子图片存放路径
POST_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "post")


BANNER_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "banner")


CHAT_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "chat")


# 每页展示帖子的数量
PER_PAGE_COUNT = 10

# 每页展示商品的数量
C_PER_PAGE_COUNT = 12


# # 设置JWT过期时间
# JWT_ACCESS_TOKEN_EXPIRES = timedelta(days=100)
