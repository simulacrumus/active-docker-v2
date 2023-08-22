import sqlalchemy
from schema import *
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError
from sqlalchemy import update
from sqlalchemy import and_
from os import environ as env
import logging

def get_db_engine():
    connection_string = 'mysql+mysqlconnector://{MYSQL_USER}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DATABASE}'.format(
        MYSQL_USER = env['MYSQL_USER'],
        MYSQL_PASSWORD = env['MYSQL_PASSWORD'],
        MYSQL_HOST = env['MYSQL_HOST'],
        MYSQL_PORT = env['MYSQL_PORT'],
        MYSQL_DATABASE = env['MYSQL_DATABASE']
    )

    engine = sqlalchemy.create_engine(connection_string,echo=False, future=True)
    return engine

def get_facilities():
    engine = get_db_engine()
    with Session(engine) as session:
        facilities = []
        db_facilities = session.execute(sqlalchemy.select(Facility))
        for db_facility in db_facilities:
            facility = {}
            facility["id"] = db_facility[0].id
            facility["title"] = get_description_by_translation_id(engine,id=db_facility[0].title_translation_id)
            facility["url"] = get_description_by_translation_id(engine,id=db_facility[0].url_translation_id)
            facility["branches"] = []
            for db_branch in db_facility[0].facility_branches:
                facility["branches"].append({"id":db_branch.id,"description":db_branch.description})
            facilities.append(facility)
        return facilities

def get_facility_branches():
    engine = get_db_engine()
    with Session(engine) as session:
        db_facility_branches = session.execute(sqlalchemy.select(FacilityBranch))
        facility_branches = []
        for db_facility_branch in db_facility_branches:
            facility_branches.append(db_facility_branch[0].id)
        session.close()
        return facility_branches

def get_activities():
    logging.info('Getting all current activities')
    engine = get_db_engine()
    with Session(engine) as session:
        activities = []
        db_activities = session.execute(sqlalchemy.select(Activity))
        for db_activity in db_activities:
            activity = {}
            activity["id"] = db_activity[0].id
            activity["type_id"] = db_activity[0].type_id
            activity["title"] = get_description_by_translation_id(engine, id=db_activity[0].title_translation_id)
            type = db_activity[0].type
            activity["category_id"] = type.category_id
            activities.append(activity)
        session.close()
        return activities

def get_description_by_translation_id(engine, id:int):
    engine = get_db_engine()
    with Session(engine) as session:
        description =  session.execute(sqlalchemy.select(LanguageTranslation).where(LanguageTranslation.translation_id == id and LanguageTranslation.language_id == 'en')).first()[0].description
        session.close()
        return description

def save_activity(activity):
    engine = get_db_engine()
    with Session(engine) as session:
        try:
            session.execute(sqlalchemy.insert(Availability),activity)
            session.commit()
            session.close()
        except(IntegrityError) as e:
            # logging.warning(e._message)
            session.close()

def save_facility_type(facility_type):
    engine = get_db_engine()
    with Session(engine) as session:
        try:
            session.execute(sqlalchemy.insert(FacilityType),facility_type)
            session.commit()
            session.close()
        except(IntegrityError) as e:
            session.close()
            # logging.warning(e._message)

def update_activity_status(activity):
    engine = get_db_engine()
    with Session(engine) as session:
        try:
            stmt = (update(Availability)
                    .where(
                        and_(
                            Availability.activity_id == activity["activity_id"], 
                            Availability.start_time == datetime.datetime.strptime(activity["start_time"], '%Y-%m-%dT%H:%M:%S.%f'),
                            Availability.facility_branch_id == activity["facility_branch_id"]
                            )
                    )
                    .values(is_available = activity["is_available"], last_updated = activity["last_updated"])
                )
            session.execute(statement=stmt)
            session.commit()
            session.close()
        except(IntegrityError) as e:
            session.close()
        