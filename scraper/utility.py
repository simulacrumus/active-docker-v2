from datetime import datetime
from datetime import timedelta
import json
import re
import requests
from bs4 import BeautifulSoup

def get_json_data(file):
    with open(file) as json_file:
        return json.load(json_file)

def write_json_data(data, filename):
    with open(filename, 'w') as outfile:
        json.dump(data, outfile)

def string_to_num(str:str):
    return int(filter(str.isdigit, str))

def remove_duplicates_from_list(list:list):
    return [*set(list)]

def get_next_weekday(date, weekday):
    days_ahead = weekday - date.weekday()
    if days_ahead < 0:
        days_ahead = days_ahead + 7
    return date + timedelta(days=days_ahead)

def get_soup_for_url(url:str):
    session = requests.Session()
    response = session.get(url=url)
    return BeautifulSoup(response.content, 'html.parser')