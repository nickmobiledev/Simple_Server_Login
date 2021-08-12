from peewee import *
import os

db = MySQLDatabase('nickmobiledev$xxxxxx', user='nickmobiledev', password='xxxxxx',
                         host='xxx', port=3306)

class User(Model):
	username = CharField(max_length=255, unique=True)
	email = CharField(max_length=255, unique=False)
	password = CharField(max_length=255, unique=False)
	class Meta:
		database = db


def create_account(user_name, user_email, user_password):
	try:
		User.create(username=user_name, email=user_email, password=user_password)
		return 'User Created'
	except IntegrityError:
		user_table = User.get(username=user_name)
		if user_table:
			return 'User Already Exists'
		else:
			return 'Server Error'


db.connect()
db.create_tables([User], safe=True)
