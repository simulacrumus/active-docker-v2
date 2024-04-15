import logging
import re
from time_format import * 

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

def scrape_activity_hours(soup):
    regex = re.compile(r'[\n\r\t]')
    days = ["monday","tuesday","wednesday","thursday","friday","saturday","sunday"]
    hours = []
    index = 0
    hours_str = soup.find_all("td")
    for hour_str in hours_str:
        try:
            hour_str = regex.sub("", str(hour_str.text.strip()))
            times = parse_schedule_string(hour_str)
            hours.append({"day":days[index],"times":times})
            index = index+1
        except(Exception)as e:
            logging.warning('Could not scrape hours')
            logging.warning(e)
            logging.warning('Original hour string: {}'.format(hour_str))
    return hours

def scrape_facility_schedules(soup):
    all_schedules = []
    activities = soup.find_all("tbody")
    for activity in activities:
        schedules = activity.find_all("tr")
        for schedule in schedules:
            title = scrape_activity_title(soup=schedule)
            if title:
                logging.info('Scraping hours for {}'.format(title))
                hours = scrape_activity_hours(soup=schedule)
                all_schedules.append({"title":title,"schedules":hours})
    return all_schedules