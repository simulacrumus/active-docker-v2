import sqlalchemy
from .schema import LanguageTranslation

def get_description_by_translation_id(session, id:int):
    description =  session.execute(sqlalchemy.select(LanguageTranslation).where(LanguageTranslation.translation_id == id and LanguageTranslation.language_id == 'en')).first()[0].description
    return description
        