
from flask_sqlalchemy import SQLAlchemy
from flask_mail import Mail
from flask_caching import Cache
from flask_wtf import CSRFProtect
from flask_avatars import Avatars
from flask_jwt_extended import JWTManager
from flask_cors import CORS
# babel 模组用于翻译
from flask_babel import Babel, gettext as _

babel = Babel()
db = SQLAlchemy()
mail = Mail()
cache = Cache()
# csrf = CSRFProtect()
avatars = Avatars()
jwt = JWTManager()
cors = CORS()
