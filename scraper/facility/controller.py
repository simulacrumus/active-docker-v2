import logging
from db.faciliity import *
from .scraper import *
from pyjarowinkler import distance

def saved_facilities():
    logging.info('Downloading saved facilities..')
    try:
       facilities = db_facilities()
       logging.info('Saved facilities downloaded')
       return facilities
    except(Exception) as e:
        logging.error(e)

async def scraped_facilities():
    logging.info('Scraping facilities from source websites..')
    try:
        all_facilities = await scrape_all_facilities()
        logging.info('Facilities downloaded from source websites')
        return await filter_facilities_with_drop_in_activity_schedules(all_facilities)
    except(Exception) as e:
        logging.error(e)

def check_new_facilities(scraped_facilities:list,current_facilities:list):
    new_facilities = []
    try:
        for scraped_facility in scraped_facilities:
            match = False
            for current_facility in current_facilities:
                score = distance.get_jaro_distance(current_facility['title'].lower(), scraped_facility['title'].lower(), winkler=False, scaling=0.1)
                if score > 0.9:
                    match = True
                    break
            if not match and 'senior' not in scraped_facility['title'].lower():
                new_facilities.append(scraped_facility)
        
        if new_facilities:
            logging.warning('Missing facilities')
            for new_facility in new_facilities:
                logging.info(new_facility['title'])
                logging.info(new_facility['url'])
        else:
            logging.info('No new facilities found')

    except(Exception) as e:
        logging.error('Error while comparing facilities')
        logging.error(e)
    
async def filter_facilities_with_drop_in_activity_schedules(facilities:list):
    logging.info('Filtering Facilities with drop-in activity schedules')
    filtered_facilities = []
    for facility in facilities:
        try:
            reservation_links = await scrape_facility_reservation_links(url=facility['url'])
            if reservation_links:
                facility['reservation_links'] = reservation_links
                filtered_facilities.append(facility)
            if await facility_has_drop_in_actiivities(url=facility['url']):
                filtered_facilities.append(facility)

        except(Exception) as e:
            logging.warning('Error while filtering facilities with drop-in activity schedules')
            logging.warning(e)
    return filtered_facilities