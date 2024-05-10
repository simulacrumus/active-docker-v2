import logging
import schedule
import time
from facility.controller import *
from activity.controller import *

logging.basicConfig(
    # filename='app.log',
    format='%(asctime)s %(levelname)-8s %(message)s',
    level=logging.INFO,
    datefmt='%Y-%m-%d %H:%M:%S'
)

def run():
    current_facilities = saved_facilities()
    listed_facilities = scraped_facilities()
    check_new_facilities(listed_facilities, current_facilities)
    update_activities_for_facilities(current_facilities)

if __name__ == '__main__':
    logging.info('Starting Python Web Scraper..')
    run()