import sqlalchemy
import logging
from .schema import Availability, Activity
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError
from .translation import get_description_by_translation_id
from .engine import create_db_engine

db_engine = create_db_engine()

def saved_activities():
    logging.info('Retrieving activities from db')
    with Session(db_engine) as session:
        activities = []
        db_activities = session.execute(sqlalchemy.select(Activity))
        for db_activity in db_activities:
            activity = {}
            activity["id"] = db_activity[0].id
            activity["type_id"] = db_activity[0].type_id
            activity["title"] = get_description_by_translation_id(session, id=db_activity[0].title_translation_id)
            type = db_activity[0].type
            activity["category_id"] = type.category_id
            activities.append(activity)
        session.close()
        return activities
    
def save_activity(session, activity):
    try:
        session.execute(sqlalchemy.insert(Availability),activity)
        session.commit()
    except(IntegrityError) as e:
        # logging.warning(e._message)
        # since this error occurs when there are duplicates, no need to log it
        pass

        