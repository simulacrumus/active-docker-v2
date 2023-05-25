from facility_controller import *
from activity_controller import *
import schedule
import time
import logging
import logging

logging.basicConfig(
    format='%(asctime)s %(levelname)-8s %(message)s',
    level=logging.INFO,
    datefmt='%Y-%m-%d %H:%M:%S'
)

def update_activity_status():
    try:
        # Update the availability status of the activities
        update_status_of_all_activities()
    except (Exception) as e:
        logging.error(e)
        
def update_schedules():
    try:
        # Existing facilities in prod
        current_facilities = get_current_facilities()
        # Scraped facilities from city of Ottawa websites
        scraped_facilities = get_scraped_facilities()
        # Notify if there's a new facility
        check_facilities(current=current_facilities,new=scraped_facilities)
        # Scrape activities for existing facilities
        facilities_with_scraped_activities = add_scraped_activities_to_facilities(current_facilities)
        # Save activities to db
        save_activities_for_facilities(facilities_with_scraped_activities)
    except(Exception) as e:
        logging.error(e)

if __name__ == '__main__':
    logging.info('Starting Python Web Scraper..')
    time.sleep(300)
    update_schedules()
    # update schedules every midnight
    schedule.every().day.at("00:10").do(update_schedules)
    # update activity availability status every 15 mins
    schedule.every().hour.at(":01").do(update_activity_status)
    schedule.every().hour.at(":15").do(update_activity_status)
    schedule.every().hour.at(":30").do(update_activity_status)
    schedule.every().hour.at(":45").do(update_activity_status)
    while 1:
        schedule.run_pending()
        time.sleep(1)