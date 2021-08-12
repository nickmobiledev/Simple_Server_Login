from flask import Flask
from flask import request
import database

app = Flask(__name__)

@app.route('/create_account', methods=['POST')
def create_account():
	req = request.form
	return database.create_account(req['user_name'], req['user_email'], req['user_password'])
                                       
