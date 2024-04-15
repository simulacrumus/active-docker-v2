import logging
import os
from config import *
from utility import *
from activity.scraper import *

def scrape_all_facilities():
    logging.info('Scraping Facilities')
    facilities = []
    try:
        facility_list_page_links = scrape_all_facility_list_page_links()
        for link in facility_list_page_links:
            facility_soups = scrape_facilities_from_list_page(url=link)
            for soup in facility_soups:
                facility = scrape_facility_from_list_page_table_row(facility_soup=soup)
                facilities.append(facility)
    except(ConnectionError, Exception) as e:
        logging.warning(e)
    return facilities

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
        facility["title"] = facility_soup.find("a").text

        address_fields = facility_soup.find("p", {"class":"address"})
        street = address_fields.find("span", {"class":"address-line1"}).text

        city = address_fields.find("span", {"class":"locality"}).text

        province = address_fields.find("span", {"class":"administrative-area"}).text

        postal_code = address_fields.find("span", {"class":"postal-code"}).text.replace(" ","")
        facility["address"] = {"street":street, "city":city, "province":province, "postal_code":postal_code,"country":"Canada"}
    except(Exception) as e:
        logging.warning(e)
    return facility

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

def scrape_facility_reservation_links(url:str):
    soup = get_soup_for_url(url=url)
    reservation_links = soup.find_all("a", text=re.compile("Reserve a spot"))
    reservations = []
    for rl in reservation_links:
        reservations.append(os.path.basename(os.path.normpath(rl.attrs.get("href"))))
    return remove_duplicates_from_list(reservations)