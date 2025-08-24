from datetime import datetime
from datetime import timedelta
import json
import re
import requests
from bs4 import BeautifulSoup
import calendar
from playwright.async_api import async_playwright

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

async def get_soup_for_url(url:str):
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()
        
        # Add realistic headers to avoid bot detection
        await page.set_extra_http_headers({
            'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
            'Accept-Language': 'en-US,en;q=0.5',
            'Accept-Encoding': 'gzip, deflate',
            'Connection': 'keep-alive',
        })
        
        await page.goto(url, wait_until='networkidle')
        await page.wait_for_timeout(2000)  # Wait 2 seconds to avoid rapid requests
        content = await page.content()
        await browser.close()
        return BeautifulSoup(content, 'html.parser')

def get_month_number(month_name):
    return datetime.strptime(month_name, '%B').month

# def contains_month_name(string):
#     month_names = list(calendar.month_name)[1:]
#     pattern = re.compile(r'\b(?:' + '|'.join(month_names) + r')\b', re.IGNORECASE)
#     return bool(pattern.search(string))