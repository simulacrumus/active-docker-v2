from scraper import *
from util import *
from db import *
from datetime import datetime
import logging
from pyjarowinkler import distance

RESERVATIONS_BASE_URL='https://reservation.frontdesksuite.ca/'
SUBMISSION_URL_SUFFIX = "/ReserveTime/SubmitSlotCount?culture=en"
SUBMISSION_URL_PREFIX = "rcfs/"
CITY_OF_OTTAWA_BASE_URL = "https://ottawa.ca"

def build_facilities():
    facilities = get_all_facilities()
    facilities = filter_facilities_with_drop_in_activity_schedules(facilities)
    for facility in facilities:
        logging.info(facility['title'])
        facility['soup'] = get_facility_soup(facility=facility)
        facility['phone'] = get_facility_phone(facility=facility)
        facility['email'] = get_facility_email(facility=facility)
        facility['activities'] = get_facility_schedules(facility=facility)
        facility['operating_hours'] = get_facility_operating_hours(facility=facility)
        facility['url_fr'] = CITY_OF_OTTAWA_BASE_URL+get_facility_url_fr(facility=facility)
        facility['soup_fr'] = get_facility_soup_fr(facility=facility)
        facility['address']['street_fr'] = get_facility_address_fr(facility=facility)
        facility['title_fr'] = get_title_fr(facility=facility)
        facility['operating_hours_fr'] = get_facility_operating_hours_fr(facility=facility)
        facility.pop('soup', None)
        facility.pop('soup_fr', None)
    return facilities

def get_all_facilities():
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

def filter_facilities_with_drop_in_activity_schedules(facilities:list):
    logging.info('Filtering Facilities with drop-in activity schedules')
    filtered_facilities = []
    for facility in facilities:
        try:
            reservation_links = get_facility_reservation_links(url=facility['url'])
            if reservation_links:
                facility['reservation_links'] = reservation_links
                filtered_facilities.append(facility)
        except(Exception) as e:
            logging.warning('Error while filtering facilities with drop-in activity schedules')
            logging.warning(e)
    return filtered_facilities

def get_facility_soup(facility):
    soup = get_soup_for_url(facility['url'])
    return soup

def get_facility_soup_fr(facility):
    soup = get_soup_for_url(facility['url_fr'])
    return soup

def get_facility_operating_hours(facility):
    operating_hours = scrape_facility_operating_hours(facility['soup'])
    return operating_hours

def get_facility_operating_hours_fr(facility):
    operating_hours = scrape_facility_operating_hours(facility['soup_fr'])
    return operating_hours

def get_facility_email(facility):
    email = scrape_facility_email(facility['soup'])
    return email

def get_facility_phone(facility):
    phone = scrape_facility_phone(facility['soup'])
    return phone

def get_facility_url_fr(facility):
    url_fr = scrape_facility_url_fr(soup=facility['soup'])
    return url_fr

def get_facility_schedules(facility):
    schedules = scrape_facility_schedules(soup=facility['soup'])
    return schedules

def get_facility_reservation_links(url:str):
    reservation_links = scrape_facility_reservation_links(url=url)
    return reservation_links

def get_facility_address_fr(facility):
    address_fr = scrape_address_fr(soup=facility['soup_fr'])
    return address_fr

def get_title_fr(facility):
    title_fr = scrape_title_fr(soup=facility['soup_fr'])
    return title_fr

# FROM V1

def build_activities_with_availability_status():
    all_activities = []
    facility_branches = get_facility_branches()
    for facility_branch_id in facility_branches:
        activities = build_activities_with_availability_status_for_facility_branch_id(facility_branch_id)
        all_activities += activities
    return all_activities

def build_activities_with_availability_status_for_facility_branch_id(facility_branch_id):
    activities = []
    reservation_page_url = RESERVATIONS_BASE_URL + SUBMISSION_URL_PREFIX + facility_branch_id
    current_activities = get_activities()
    activity_titles = scrape_activity_titles(reservation_page_url)
    for activity_title in activity_titles:
        activity_id = 0
        try:
            matched_activity = {}
            score = 0
            for current_activity in current_activities:
                new_score = distance.get_jaro_distance(current_activity['title'].lower(), activity_title.lower(), winkler=False, scaling=0.1)
                if new_score > score:
                    matched_activity = current_activity
                    score = new_score
            if score >= 0.9:
                activity_id = matched_activity["id"]
        except (RuntimeError) as e:
                logging.warning(e)
        if activity_id != 0:
            submission_link = "/"+SUBMISSION_URL_PREFIX+facility_branch_id+SUBMISSION_URL_SUFFIX
            activity_details = scrape_activity_details(reservation_page_url, activity_title, submission_link)
            try:
                for activity_detail in activity_details:
                    activity = {
                        "facility_branch_id": facility_branch_id,
                        "title": activity_title.strip().lower().replace("–", "-").replace("’", "'"),
                        "start_time": string_to_datetime(activity_detail["time"]),
                        "is_available": activity_detail["available"],
                        "last_updated": datetime.now().strftime('%Y-%m-%dT%H:%M:%S.%f'),
                        "activity_id": activity_id
                    }
                    activities.append(activity)
            except(Exception) as e:
                logging.warning(e)
    return activities;