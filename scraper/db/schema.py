from sqlalchemy import Column, ForeignKey, Integer, DateTime, String, Boolean
from sqlalchemy.orm import declarative_base, relationship, backref
import datetime

Base = declarative_base()

class Translation(Base):
    __tablename__ = "translation"
    id = Column(Integer, primary_key=True)
    last_updated = Column(DateTime, default=datetime.datetime.now)
    language_translations = relationship("LanguageTranslation", backref=backref("translation", lazy=True))

class Language(Base):
    __tablename__ = "language"
    id = Column(String(2), primary_key=True)
    title = Column(String(30), nullable=False)
    last_updated = Column(DateTime, default=datetime.datetime.now)

class LanguageTranslation(Base):
    __tablename__ = "language_translation"
    translation_id = Column(Integer, ForeignKey("translation.id"), nullable=False, primary_key=True)
    # translation = relationship("Translation", backref=backref("language_translations", lazy=True))
    language_id = Column(String(2), ForeignKey("language.id"), nullable=False, primary_key=True)
    # language = relationship("Language", backref=backref("language_translations", lazy=True))
    description = Column(String, nullable=False)
    last_updated = Column(DateTime, default=datetime.datetime.now)

class Facility(Base):
    __tablename__ = "facility"
    id = Column(Integer, primary_key=True)
    title_translation_id = Column(Integer, ForeignKey("translation.id"), nullable=False)
    url_translation_id = Column(Integer, ForeignKey("translation.id"), nullable=False)
    address_id = Column(Integer)
    city_id = Column(Integer)
    phone = Column(String(12))
    email = Column(String(50))
    last_updated = Column(DateTime, default=datetime.datetime.now)
    facility_branches = relationship("FacilityBranch", back_populates="facility")

class Category(Base):
    __tablename__ = "category"
    id = Column(Integer, primary_key=True)
    city_id = Column(Integer)
    title_translation_id = Column(Integer)
    last_updated = Column(DateTime, default=datetime.datetime.now)
    types = relationship("Type", back_populates="category")

class Type(Base):
    __tablename__ = "type"
    id = Column(Integer, primary_key=True)
    title_translation_id = Column(Integer)
    category_id = Column(Integer, ForeignKey("category.id"), nullable=False)
    category = relationship("Category", back_populates="types")
    activities = relationship("Activity", back_populates="type")
    last_updated = Column(DateTime, default=datetime.datetime.now)

class FacilityBranch(Base):
    __tablename__ = "facility_branch"
    id = Column(String(30), primary_key=True)
    reservation_url = Column(String)
    facility_id = Column(Integer, ForeignKey("facility.id"), nullable=False)
    facility = relationship("Facility", back_populates="facility_branches", lazy=True)
    description = Column(String)
    last_updated = Column(DateTime, default=datetime.datetime.utcnow)
    availabilities = relationship("Availability", back_populates="facility_branch")

class Activity(Base):
    __tablename__ = "activity"
    id = Column(Integer, primary_key=True)
    title_translation_id = Column(Integer, ForeignKey('translation.id'))
    type_id = Column(Integer, ForeignKey("type.id"), nullable=False)
    type = relationship("Type", back_populates="activities")
    min_age = Column(Integer)
    max_age = Column(Integer)
    availabilities = relationship("Availability", back_populates="activity")
    last_updated = Column(DateTime, default=datetime.datetime.now)

class Availability(Base):
    __tablename__ = "availability"
    id = Column(Integer, autoincrement=True)
    start_time = Column(DateTime, nullable=False, primary_key=True)
    end_time = Column(DateTime, nullable=False)
    is_available = Column(Boolean, default=True)
    facility_branch_id = Column(String(30), ForeignKey("facility_branch.id"), primary_key=True)
    facility_branch = relationship("FacilityBranch", back_populates="availabilities")
    activity_id = Column(Integer, ForeignKey("activity.id"), primary_key=True)
    activity = relationship("Activity", back_populates="availabilities")
    last_updated = Column(DateTime, default=datetime.datetime.now)

class FacilityType(Base):
    __tablename__ = "facility_type"
    facility_id = Column(String(30), ForeignKey("facility.id"), nullable=False,primary_key=True)
    # facility = relationship("Facility", back_populates="facility_types")
    type_id = Column(Integer, ForeignKey("type.id"), nullable=False,primary_key=True)
    # type = relationship("Type", back_populates="facility_types")
    last_updated = Column(DateTime, default=datetime.datetime.now)
       