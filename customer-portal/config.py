import os.path
from datetime import timedelta

SECRET_KEY = "SpaceX"

BASE_DIR = os.path.dirname(__file__)
DB_USERNAME = 'root'
DB_PASSWORD = 'X'
DB_HOST = '124.222.119.139'
DB_PORT = '3306'
DB_NAME = 'instrument'
POST_PIC_UPLOAD_DIR = os.path.join(BASE_DIR, 'static/assets/img')
PERMANENT_SESSION_LIFETIME = timedelta(days=7)
DB_URI = 'mysql+pymysql://%s:%s@%s:%s/%s?charset=utf8mb4' % (DB_USERNAME, DB_PASSWORD, DB_HOST, DB_PORT, DB_NAME)
SQLALCHEMY_DATABASE_URI = DB_URI
SQLALCHEMY_TRACK_MODIFICATIONS = False
MAIL_SERVER = "smtp.qq.com"
MAIL_PORT = 587
MAIL_USE_TLS = True
MAIL_USERNAME = "X"
MAIL_PASSWORD = "X"
MAIL_DEFAULT_SENDER = "X"
CELERY_BROKER_URL = 'redis://:i4gotitABC@8.130.13.122:6379/0'
CELERY_RESULT_BACKEND = 'redis://:i4gotitABC@8.130.13.122:6379/0'
CACHE_TYPE = "RedisCache"
CACHE_DEFAULT_TIMEOUT = 300
CACHE_REDIS_HOST = "8.130.13.122"
CACHE_REDIS_PORT = 6379
CACHE_REDIS_PASSWORD = "i4gotitABC"
AVATARS_SAVE_PATH = os.path.join(BASE_DIR, "media", "avatars")
POST_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "post")
BANNER_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "banner")
CHAT_IMAGE_SAVE_PATH = os.path.join(BASE_DIR, "media", "chat")
PER_PAGE_COUNT = 10
C_PER_PAGE_COUNT = 12
