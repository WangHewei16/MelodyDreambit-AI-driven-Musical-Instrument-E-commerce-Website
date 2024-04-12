from flask import Flask, request, jsonify, make_response
# from flask_babel import Babel
from flask_babel import Babel, gettext as _, refresh
from sqlalchemy import extract

import config
from exts import db, mail, cache, avatars, jwt, cors, babel
from flask_migrate import Migrate
from models import auth
from apps.front import front_bp
from apps.media import media_bp
from apps.cmsapi import cmsapi_bp
from bbs_celery import make_celery
import commands

from wtf_tinymce import wtf_tinymce


app = Flask(__name__)
app.config.from_object(config)
wtf_tinymce.init_app(app)


babel.init_app(app)
db.init_app(app)
mail.init_app(app)
cache.init_app(app)
# csrf.init_app(app)
avatars.init_app(app)
wtf_tinymce.init_app(app)
jwt.init_app(app)
cors.init_app(app, resources={r"/cmsapi/*": {"origins": "*"}})

migrate = Migrate(app, db)

mycelery = make_celery(app)

app.register_blueprint(front_bp)
app.register_blueprint(media_bp)
app.register_blueprint(cmsapi_bp)

# app.config['BABEL_DEFAULT_LOCALE'] = 'zh_Hans_CN'
# babel = Babel(app)
# app.config['LANGUAGE'] = ['en', 'zh_CN']
# app.config['BABEL_DEFAULT_LOCALE'] = 'zh_CN'

# app.config['LANGUAGE'] = ['en', 'zh_CN']
app.config['BABEL_DEFAULT_LOCALE'] = 'en'
babel = Babel(app)

# app.cli.command("init_boards")(commands.init_boards)
# app.config['BABEL_DEFAULT_TIMEZONE'] = 'UTC'

# @babel.localeselector
# def get_locale():
#     return "zh_Hans_CN"
# add to you main app code
# @babel.localeselector
# def get_locale():
#     return request.accept_languages.best_match(app.config['LANGUAGES'].keys())

# @babel.localeselector
# def get_locale():
#     language = request.cookies.get('language')
#     if language:
#         return language
#     return request.accept_languages.best_match(['zh_CN', 'en'])
@babel.localeselector
def get_locale():
    cookie = request.cookies.get('locale')
    if cookie in ['zh_CN', 'en']:
        return cookie
    return request.accept_languages.best_match(app.config.get('BABEL_DEFAULT_LOCALE'))


@app.route("/set_locale")
def set_locale():
    lang = request.args.get("language")
    response = make_response(jsonify(message=lang))
    if lang == 'English':
        refresh()
        response.set_cookie('locale', 'en')
        return response

    if lang == '汉语':
        refresh()
        response.set_cookie('locale', 'zh_CN')
        return response

    return jsonify({"data": "success"})

# @app.route("/language", methods=['GET', 'POST'])
# def language():
#     lang = request.form.get("language")
#     if lang == '🇬🇧 ENGLISH':
#         response = make_response('success')
#         refresh()
#         response.set_cookie('language', 'en')
#         return response
#
#     if lang == '🇨🇳 CHINESE':
#         response = make_response('success')
#         refresh()
#         response.set_cookie('language', 'zh_CN')
#         return response
#
#     return jsonify({"data": "success"})
# @app.route("/language", methods=['GET', 'POST'])
# def language():
#     lang = request.values.get("language")
#     if lang == '🇬🇧 ENGLISH':
#         app.config.update(
#             BABEL_DEFAULT_LOCALE='en'
#         )
#         refresh()
#         print(app.config['BABEL_DEFAULT_LOCALE'])
#     if lang == '🇨🇳 CHINESE':
#         app.config.update(
#             BABEL_DEFAULT_LOCALE='zh_CN'
#         )
#         refresh()
#         print(app.config['BABEL_DEFAULT_LOCALE'])
#     return jsonify({"data": "success"})


if __name__ == '__main__':
    app.run()
