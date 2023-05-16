from os import environ as env
import logging
from pyjarowinkler import distance
from db import *
from builder import *

def get_current_facilities():
    logging.info('Downloading current facilities..')
    try:
       facilities = get_facilities()
       logging.info('Current facilities downloaded')
       return facilities
    except(Exception) as e:
        logging.error(e)

def get_scraped_facilities():
    logging.info('Downloading facilities from source websites..')
    try:
        all_facilities = get_all_facilities()
        logging.info('Facilities downloaded from source websites')
        return filter_facilities_with_drop_in_activity_schedules(all_facilities)
    except(Exception) as e:
        logging.error(e)

def get_new_facilities(scraped_facilities:list,current_facilities:list):
    new_facilities = []
    try:
        for scraped_facility in scraped_facilities:
            match = False
            for current_facility in current_facilities:
                score = distance.get_jaro_distance(current_facility['title'].lower(), scraped_facility['title'].lower(), winkler=False, scaling=0.1)
                if score > 0.9:
                    match = True
                    break
            if not match:
                new_facilities.append(scraped_facility)
    except(Exception) as e:
        logging.error('Error while comparing facilities')
        logging.error(e)
    return new_facilities
    
def check_facilities(current:list,new:list):
    new_facilities = get_new_facilities(scraped_facilities=new, current_facilities=current)
    if new_facilities:
        # TODO: send an email to notify
        logging.warning('Missing facilities')
        for new_facility in new_facilities:
            logging.warning(new_facility['title'])
            logging.warning(new_facility['url'])
    else:
        logging.info('No new facilities found')