import requests
from bs4 import BeautifulSoup
import re
from util import *
from schedule_parser import parse_schedule_string
import logging
import os

FACILITIES_LIST_URL = "https://ottawa.ca/en/recreation-and-parks/recreation-facilities/place-listing?page="
CITY_OF_OTTAWA_BASE_URL = "https://ottawa.ca"
RESERVATION_BASE_URL='https://reservation.frontdesksuite.ca/'

def scrape_all_facility_list_page_links():
    facility_pages = []
    try:
        page_number = 0
        url = FACILITIES_LIST_URL + str(page_number)
        soup = get_soup_for_url(url=url)
        pages = soup.find("ul",{"class":"pager__items"}).find_all("li")
        max_page_number = len(pages)
        while page_number < max_page_number-1:
            url = FACILITIES_LIST_URL + str(page_number)
            facility_pages.append(url)
            page_number += 1
    except(ConnectionError, Exception) as e:
        logging.warning('Scrape page links')
        logging.warning(e)
    return facility_pages

def scrape_facilities_from_list_page(url:str):
    try:
        facilities = []
        soup = get_soup_for_url(url=url)
        facility_list = soup.find("tbody").find_all("tr")
        for facility in facility_list:
            facilities.append(facility)
        return facilities
    except(ConnectionError, Exception) as e:
        logging.warning(e)

def scrape_facility_from_list_page_table_row(facility_soup):
    facility = {}
    try:
        facility["url"] = CITY_OF_OTTAWA_BASE_URL+facility_soup.find("a").attrs.get("href")
        facility["title"] = facility_soup.find("a").string
        address_fields = facility_soup.find("p", {"class":"address"})
        street = address_fields.find("span", {"class":"address-line1"}).string
        city = address_fields.find("span", {"class":"locality"}).string
        province = address_fields.find("span", {"class":"administrative-area"}).string
        postal_code = address_fields.find("span", {"class":"postal-code"}).string.replace(" ","")
        facility["address"] = {"street":street, "city":city, "province":province, "postal_code":postal_code,"country":"Canada"}
    except(Exception) as e:
        logging.warning('Scrape facility from row')
        logging.warning(e)
    return facility

def scrape_facility_reservation_links(url:str):
    soup = get_soup_for_url(url=url)
    reservation_links = soup.find_all("a", text=re.compile("Reserve a spot"))
    reservations = []
    for rl in reservation_links:
        reservations.append(os.path.basename(os.path.normpath(rl.attrs.get("href"))))
    return remove_duplicates_from_list(reservations)

def scrape_facility_operating_hours(soup):
    try:
        hours = {}
        days = soup.find_all("div", {"class":"office-hours__item"})
        if days is not None:
            for day in days:
                if day is not None:
                    label = day.find("span",{"class":"office-hours__item-label"}).string
                    label = re.sub("^[A-Za-z]*$", '', str(label)).replace(':','').strip()
                    slots = day.find("span",{"class":"office-hours__item-slots"}).string
                    slots = re.sub("^[A-Za-z0-9-]*$", '', str(slots)).strip()
                    hours[label] = slots
        return hours
    except(ConnectionError, Exception) as e:
        logging.warning('Operating hours scraper')
        logging.warning(e)

def scrape_facility_email(soup):
    email = ''
    try:
        email = soup.find("a", {"href":re.compile('mailto')}).string.lower()
    except(Exception) as e:
        logging.warning('Could not scrape email')
        logging.warning(e)
    return email

def scrape_facility_phone(soup):
    phone =''
    try:
        phone = soup.find(text=re.compile('613-'))
        phone = get_phone_from_string(phone)
    except(Exception) as e:
        logging.warning('Could not scrape phone number')
        logging.warning(e)
    return phone

def scrape_facility_url_fr(soup):
    url_fr =''
    try:
        url_fr = soup.find("a",{"href":re.compile('/fr/')}).get("href")
    except(Exception) as e:
        logging.warning('Could not scrape French url')
        logging.warning(e)
    return url_fr

def scrape_facility_schedules(soup):
    all_schedules = []
    activities = soup.find_all("tbody")
    for activity in activities:
        schedules = activity.find_all("tr")
        for schedule in schedules:
            title = scrape_activity_title(soup=schedule)
            if title:
                hours = scrape_activity_hours(soup=schedule)
                all_schedules.append({"title":title,"schedules":hours})
    return all_schedules

def scrape_activity_title(soup):
    title = ''
    regex = re.compile(r'[\n\r\t]')
    try:
        title = soup.find("th").string
        if title.__contains__('*'):
            title = title.split('*')[0].strip()
    except(Exception) as e:
        try:
            title = soup.find("th").find("strong").string
            if title.__contains__('*'):
                title = title.split('*')[0].strip()
        except(Exception) as e:
            logging.warning('Could not scrape activity title')
            logging.warning(e)
    try:
        title = re.compile(r'[\u202f\u00a0\u00ae]').sub('', str(title)).strip()
        title = re.compile(r'[\u2013]').sub('-', str(title)).strip()
        title = re.compile(r'[\u2019]').sub('\'', str(title)).strip()
        title = regex.sub("", str(title))
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
            if hour_str.em: hours_str =  hour_str.em.decompose()
            if hour_str.span: hour_str = hour_str.span.decompose()
            hour_str = regex.sub("", str(hour_str.text))
            times = parse_schedule_string(hour_str)
            hours.append({"day":days[index],"times":times})
            index = index+1
        except(Exception)as e:
            logging.warning('Could not scrape hours')
            logging.warning(e)
            logging.warning(hour_str)
    return hours
        
def scrape_address_fr(soup):
    address_fr = ''
    try:
        address_fr = soup.find("span", {"class":"place-address-1"}).string
    except(Exception) as e:
        logging.warning('Could not scrape french address')
        logging.warning(e)
    return address_fr
    
def scrape_title_fr(soup):
    title_fr = ''
    try:
        title_fr = soup.find("h1", {"class":"page-title"}).find("span").string
    except(Exception) as e:
        logging.warning('Could not scrape french address')
        logging.warning(e)
    return title_fr

def get_soup_for_url(url:str):
    session = requests.Session()
    response = session.get(url=url)
    return BeautifulSoup(response.content, 'html.parser')

# 
# FROM v1
# 

# Scrape all activity titles from facility's reservation page
def scrape_activity_titles(url:str):
    try:
        activity_titles = []
        session = requests.Session()
        response = session.get(url)
        soup = BeautifulSoup(response.content, 'html.parser')
        activities = soup.find_all("div", {"class":"content"})
        if activities is not None:
            for activity in activities:
                if activity is not None:
                    a = activity.string
                    activity_titles.append(a)
        return activity_titles
    except(ConnectionError, Exception) as e:
        logging.warning('Could not scrape activities for url: {}'.format(url))
        logging.warning(e)

# Scrape active times and availibility for an activity
def scrape_activity_details(url, activity_title, submission_link):
    try:
        times_and_availibility = []
        session = requests.Session()
        response = session.get(url=url)
        soup = BeautifulSoup(response.content, 'html.parser')
        activity_url = soup.find("div", string=re.compile(activity_title)).parent.attrs.get('href')
        response = session.get(url=RESERVATION_BASE_URL+activity_url)
        soup = BeautifulSoup(response.text, "html.parser")
        form_data_tags = soup.find_all("input", {"type" : "hidden"})
        form_data = {}
        for tag in form_data_tags:
            form_data[tag.get('name')] = tag.get("value")
        response = session.post(url=RESERVATION_BASE_URL+submission_link, data=form_data)
        soup = BeautifulSoup(response.text, "html.parser")
        time_tags = soup.find_all("a", {"class":"time-container"})
        for tag in time_tags:
            times_and_availibility.append({
                "time":tag.get('aria-label'),
                "available": tag.get("onclick") != "return false;"
            })
        return times_and_availibility
    except(ConnectionError, Exception) as e:
        logging.warning('{} does not have any scheduled availability'.format(activity_title))
        logging.warning(e)