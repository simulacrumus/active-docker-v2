import logging
import re
from time_format import * 
from datetime import datetime, timedelta
from utility import get_month_number

def scrape_activity_title(soup):
    title = ''
    try:
        title = soup.find("th").text
        if title.__contains__('*'):
            title = title.split('*')[0].strip()
    except(Exception) as e:
        try:
            title = soup.find("td").text.strip()
            if title.__contains__('*'):
                title = title.split('*')[0].strip()
        except(Exception) as e:
            logging.warning('Could not scrape activity title')
            logging.warning(e)
    try:
        title = re.compile(r'[\u202f\u00a0\u00ae]').sub('', str(title)).strip()
        title = re.compile(r'[\u2013]').sub('-', str(title)).strip()
        title = re.compile(r'[\u2019]').sub('\'', str(title)).strip()
        title = re.compile(r'[\n\r\t]').sub("", str(title))
    except(Exception) as e:
        logging.warning(e)
    return title

def scrape_category_time_period(soup):
    period = ''
    try:
        time_period_string = soup.find("caption").text
        period = re.compile(r'[\u2013]').sub('-', str(time_period_string)).strip()
        if period.__contains__('starting'):
            period = period.split('starting')[-1].strip()
        elif period.__contains__('-'):
            period = period.split('-')[-1].strip()
    except(Exception) as e:
        logging.warning('Could not scrape category time period')
        logging.warning(e)
        logging.warning('Original period string: {}'.format(time_period_string))
    return period

def scrape_activity_hours(soup):
    regex = re.compile(r'[\n\r\t]')
    hours = []
    index = 0
    hours_str = soup.find_all("td")
    for hour_str in hours_str:
        try:
            hour_str = regex.sub("", str(hour_str.text.strip()))
            times = parse_schedule_string(hour_str)
            hours.append(times)
            index = index+1
        except(Exception)as e:
            logging.warning('Could not scrape hours')
            logging.warning(e)
            logging.warning('Original hour string: {}'.format(hour_str))
    return hours

def scrape_facility_schedules(soup):
    all_schedules = []
    activities = soup.find_all("table")
    for activity in activities:
        period_string = scrape_category_time_period(soup=activity)
        period = create_date_periods(period_string)
        schedules = activity.find("tbody").find_all("tr")
        for schedule in schedules:
            title = scrape_activity_title(soup=schedule)
            if title:
                logging.info('Scraping hours for {}'.format(title))
                hours = scrape_activity_hours(soup=schedule)
                all_schedules.append({"title":title,"period":period,"schedules":hours})
    return all_schedules

def create_date_periods(period_string):
    try:
        period_string = period_string.lower()
        period_start_string = ''
        period_end_string = ''
        if 'until' in period_string:
            period_start_string = datetime.now().strftime("%B %d, %Y")
            period_end_string = period_string.split("until")[-1].strip()
            logging.info('Period string has until: {}'.format(period_string))
        elif 'starting' in period_string and 'to' not in period_string and 'until' not in period_string:
            logging.info('Period string has only start time: {}'.format(period_string))
            period_start_string = period_string.split("starting")[-1].strip()
            period_end_string = (datetime.now() + timedelta(days=7)).strftime("%B %d, %Y")
        else:
            logging.info('Period string: {}'.format(period_string))
            period_start_string = period_string.split("to")[0].strip()
            period_end_string = period_string.split("to")[1].strip()

        period_start = create_date_from_string(period_start_string)
        period_end = create_date_from_string(period_end_string).replace(hour=23, minute=59, second=59)
        
        # No need to create activities that are in past
        if period_start < datetime.today():
            period_start = datetime.today()

        return {"period_start": period_start , "period_end": period_end}
    
    except Exception as e:
        logging.warning('Could not create date periods from {}'.format(period_string))
        logging.info('Using the current week')
        period_start = datetime.today()
        period_end = period_start + timedelta(days=7)
        return {"period_start": period_start , "period_end": period_end}

def create_date_from_string(string):
    month_string = ''.join(re.findall(r'[a-zA-Z]+', string))
    month = get_month_number(month_string)
    year = datetime.today().year
    if "," in string:
        year = int(string.split(',')[-1].strip())
    day = int(''.join(re.findall(r'\d+', string.split(',')[0])))
    return datetime(year, month, day)