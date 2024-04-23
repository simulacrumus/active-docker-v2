import sqlalchemy
import logging
from .engine import db_engine
from .schema import Facility, FacilityType
from sqlalchemy.orm import Session
from .translation import get_description_by_translation_id
from sqlalchemy.exc import IntegrityError

def db_facilities():
    logging.info('Retrieving facilities from db')
    with Session(db_engine) as session:
        facilities = []
        db_facilities = session.execute(sqlalchemy.select(Facility))
        for db_facility in db_facilities:
            facility = {}
            facility["id"] = db_facility[0].id
            facility["title"] = get_description_by_translation_id(session,id=db_facility[0].title_translation_id)
            facility["url"] = get_description_by_translation_id(session,id=db_facility[0].url_translation_id)
            facility["branches"] = []
            for db_branch in db_facility[0].facility_branches:
                facility["branches"].append({"id":db_branch.id,"description":db_branch.description})
            facilities.append(facility)
        session.close()
        return facilities
    
def save_facility_type(session, facility_type):
    try:
        session.execute(sqlalchemy.insert(FacilityType),facility_type)
        session.commit()
    except(IntegrityError) as e:
        # logging.warning(e._message)
        pass
        