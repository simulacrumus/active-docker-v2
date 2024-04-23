import sqlalchemy
from os import environ as env

def create_db_engine():
    # connection_string = 'mysql+mysqlconnector://{MYSQL_USER}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DATABASE}'.format(
    #     MYSQL_USER = env['MYSQL_USER'],
    #     MYSQL_PASSWORD = env['MYSQL_PASSWORD'],
    #     MYSQL_HOST = env['MYSQL_HOST'],
    #     MYSQL_PORT = env['MYSQL_PORT'],
    #     MYSQL_DATABASE = env['MYSQL_DATABASE']
    # )

    connection_string = 'mysql+mysqlconnector://admin:24DbDy97wRt7ztL4bUzrZZd7y@142.93.157.9:3306/active'

    engine = sqlalchemy.create_engine(connection_string,echo=False, future=True)
    return engine