from scraper import *
from db import *
from datetime import datetime
from datetime import date
from pyjarowinkler import distance
from builder import *

def add_scraped_activities_to_facilities(facilities:list):
    for facility in facilities:
        # logging.info('Scraping activities for {}'.format(facility['title']))
        facility['soup'] = get_soup_for_url(url=facility['url'])
        facility_activities = scrape_facility_schedules(soup=facility['soup'])
        facility['activities'] = transform_schedules(facility_activities)
        facility.pop('soup', None)
    return facilities

def transform_schedules(facility_activities):
    current_activities = get_current_activities()
    activities = []
    for facility_activity in facility_activities:
        try:
            activity = get_activity_by_scraped_activity_title(current_activities, activity_title=facility_activity['title'])
            activity['times'] = create_activity_times_for_schedules(facility_activity['schedules'])
            activities.append(activity)
        except(RuntimeError) as e:
            logging.warning(e)
    return activities

def get_current_activities():
    current_activities = get_activities()
    return current_activities

def get_activity_by_scraped_activity_title(current_activities:list, activity_title):
    matched_activity = {}
    score = 0
    for current_activity in current_activities:
        new_score = distance.get_jaro_distance(current_activity['title'].lower(), activity_title.lower(), winkler=False, scaling=0.1)
        if new_score > score:
            matched_activity = current_activity
            score = new_score
    if score < 0.8:
        raise RuntimeError('No matching activity found for {}'.format(activity_title))
    return matched_activity

def create_activity_times_for_schedules(schedules):
    activity_times_for_next_week = []
    for i in range(len(schedules)):
        for activity_time in schedules[i]['times']:
            today = date.today().strftime('%Y-%m-%d ')
            start_time = datetime.strptime(today + activity_time['start_time'], '%Y-%m-%d %H:%M:%S')
            start_time = get_next_weekday(start_time, i)
            end_time = datetime.strptime(today + activity_time['end_time'], '%Y-%m-%d %H:%M:%S')
            end_time = get_next_weekday(end_time, i)
            if start_time >= end_time:
                logging.warning('{} is greater than {}, skipping'.format(start_time, end_time))
                continue
            activity_times_for_next_week.append({"start_time":str(start_time),"end_time":str(end_time)})
    return activity_times_for_next_week

def save_activities_for_facilities(facilities):
    for facility in facilities:
        availability_count = 0
        for activity in facility['activities']:
            ft = {"facility_id":facility["id"],"type_id":activity['type_id']}
            save_facility_type(ft)
            facility_branch_id = ''
            for facility_branch in facility['branches']:
                facility_branch_id = facility_branch['id']
                if facility_branch['description'] == 'Arena' and activity['category_id'] == 14:
                    facility_branch_id = facility_branch['id']
                elif facility_branch['description'] == 'Complex' and activity['category_id'] != 14:
                    facility_branch_id = facility_branch['id']
            for time in activity['times']:
                availability_count = availability_count + 1
                a = {"activity_id":activity['id'],"start_time":time['start_time'],"end_time":time['end_time'],"facility_branch_id":facility_branch_id}
                save_activity(a)
        # logging.info('Activities for {} saved, total number of avaiability: {}'.format(facility['title'], availability_count))
    # logging.info('All activities saved')    
   
def update_status_of_all_activities():
    logging.info('Updating status of activities')
    all_activities_with_statuses = build_activities_with_availability_status()
    try:
        for activity in all_activities_with_statuses:
            update_activity_status(activity)
        logging.info('All activities updated') 
    except (RuntimeError) as e:
        logging.error(e)