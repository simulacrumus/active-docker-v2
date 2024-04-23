from db.activity import *
from db.faciliity import *
from db.engine import db_engine
from utility import *
from .scraper import * 
from pyjarowinkler import distance
from datetime import date, datetime

def update_activities_for_facilities(facilities:list):
    current_activities = saved_activities()
    with Session(db_engine) as session:
        for facility in facilities:
            logging.info('Scraping activities for {}'.format(facility['title']))
            soup = get_soup_for_url(url=facility['url'])
            facility_activities = scrape_facility_schedules(soup)
            facility['activities'] = transform_schedules(facility_activities, current_activities)
            logging.info('Saving activities for {}'.format(facility['title']))
            availability_count = 0
            for activity in facility['activities']:
                logging.info('Saving availabilities for {}'.format(activity['title']))
                facility_type = {"facility_id":facility["id"],"type_id":activity['type_id']}
                save_facility_type(session, facility_type)
                facility_branch_id = ''
                for facility_branch in facility['branches']:
                    facility_branch_id = facility_branch['id']
                    if facility_branch['description'] == 'Arena' and activity['category_id'] == 14:
                        facility_branch_id = facility_branch['id']
                    elif facility_branch['description'] == 'Complex' and activity['category_id'] != 14:
                        facility_branch_id = facility_branch['id']
                for time in activity['times']:
                    availability_count = availability_count + 1
                    availability = {"activity_id":activity['id'],"start_time":time['start_time'],"end_time":time['end_time'],"facility_branch_id":facility_branch_id}
                    save_activity(session, availability)
            logging.info('Activities for {} saved, total number of avaiability: {}'.format(facility['title'], availability_count))
        session.close()

def transform_schedules(facility_activities, current_activities):
    activities = []
    for facility_activity in facility_activities:
        try:
            activity = get_activity_by_scraped_activity_title(current_activities, activity_title=facility_activity['title'])
            activity['times'] = create_activity_times_for_schedules(facility_activity['schedules'])
            activities.append(activity)
        except(RuntimeError) as e:
            logging.warning(e)
    return activities

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
            next_weekday = get_next_weekday(date.today(), i).strftime('%Y-%m-%d ')
            start_time = datetime.strptime(next_weekday + activity_time['start_time'], '%Y-%m-%d %H:%M:%S')
            end_time = datetime.strptime(next_weekday + activity_time['end_time'], '%Y-%m-%d %H:%M:%S')

            if start_time >= end_time:
                logging.warning('{} is greater than {}, skipping'.format(start_time, end_time))
                continue
            activity_times_for_next_week.append({"start_time":str(start_time),"end_time":str(end_time)})
    return activity_times_for_next_week