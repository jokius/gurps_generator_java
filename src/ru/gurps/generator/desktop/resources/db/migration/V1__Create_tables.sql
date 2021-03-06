CREATE TABLE ADDONS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  FEATUREID INTEGER,
  NAME VARCHAR(255),
  NAMEEN VARCHAR(255),
  COST INTEGER,
  DESCRIPTION CLOB,
  MAXLEVEL INTEGER
);
CREATE TABLE CULTURAS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL
);
CREATE TABLE FEATUREADDONS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERFEATUREID INTEGER,
  ADDONID INTEGER,
  LEVEL INTEGER,
  COST INTEGER
);
CREATE TABLE FEATURES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  ADVANTAGE BOOLEAN,
  NAME VARCHAR(255),
  NAMEEN VARCHAR(255),
  TYPE VARCHAR(255),
  COST INTEGER,
  DESCRIPTION CLOB,
  MAXLEVEL INTEGER,
  PSI BOOLEAN,
  CYBERNETIC BOOLEAN
);
CREATE TABLE LANGUAGES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL
);
CREATE TABLE MODIFIERS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL,
  NAMEEN VARCHAR(255),
  COST INTEGER NOT NULL,
  DESCRIPTION CLOB,
  MAXLEVEL INTEGER NOT NULL,
  COMBAT BOOLEAN NOT NULL,
  IMPROVING BOOLEAN NOT NULL
);
CREATE TABLE QUIRKS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL
);
CREATE TABLE "schema_version"
(
  "version_rank" INTEGER NOT NULL,
  "installed_rank" INTEGER NOT NULL,
  "version" VARCHAR(50) PRIMARY KEY NOT NULL,
  "description" VARCHAR(200) NOT NULL,
  "type" VARCHAR(20) NOT NULL,
  "script" VARCHAR(1000) NOT NULL,
  "checksum" INTEGER,
  "installed_by" VARCHAR(100) NOT NULL,
  "installed_on" TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
  "execution_time" INTEGER NOT NULL,
  "success" BOOLEAN NOT NULL
);
CREATE TABLE SKILLS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL,
  NAMEEN VARCHAR(255),
  TYPE INTEGER NOT NULL,
  COMPLEXITY INTEGER NOT NULL,
  DEFAULTUSE VARCHAR(255),
  DEMANDS VARCHAR(255),
  DESCRIPTION CLOB,
  MODIFIERS CLOB,
  TWOHANDS BOOLEAN DEFAULT FALSE,
  PARRY BOOLEAN DEFAULT FALSE,
  PARRYBONUS INTEGER DEFAULT 0
);
CREATE TABLE SKILLSPECIALIZATIONS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  SKILLID INTEGER NOT NULL,
  NAME VARCHAR(255) NOT NULL,
  NAMEEN VARCHAR(255),
  TYPE INTEGER,
  COMPLEXITY INTEGER,
  DEFAULTUSE VARCHAR(255),
  DEMANDS VARCHAR(255),
  MODIFIERS CLOB,
  DESCRIPTION CLOB,
  PARRY BOOLEAN DEFAULT FALSE,
  PARRYBONUS INTEGER DEFAULT 0
);
CREATE TABLE SPELLS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  SCHOOL INTEGER,
  NAME VARCHAR(255),
  NAMEEN VARCHAR(255),
  SPELLTYPE INTEGER,
  DESCRIPTION CLOB,
  COMPLEXITY INTEGER,
  COST INTEGER,
  MAXCOST INTEGER,
  NEEDTIME VARCHAR(255),
  DURATION VARCHAR(255),
  MAINTAININGCOST VARCHAR(255),
  THING VARCHAR(255),
  CREATECOST VARCHAR(255),
  DEMANDS VARCHAR(255)
);
CREATE TABLE TECHNIQUES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255) NOT NULL,
  NAMEEN VARCHAR(255),
  COMPLEXITY INTEGER NOT NULL,
  DEFAULTUSE VARCHAR(255),
  DEMANDS VARCHAR(255),
  DESCRIPTION CLOB
);
CREATE TABLE USERCULTURAS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  CULTURAID INTEGER NOT NULL,
  COST INTEGER DEFAULT 0 NOT NULL
);
CREATE TABLE USERFEATURES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER,
  FEATUREID INTEGER,
  LEVEL INTEGER,
  COST INTEGER
);
CREATE TABLE USERLANGUAGES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  LANGUAGEID INTEGER NOT NULL,
  SPOKEN INTEGER DEFAULT 0 NOT NULL,
  WRITTEN INTEGER DEFAULT 0 NOT NULL,
  COST INTEGER DEFAULT 0 NOT NULL
);
CREATE TABLE USERMODIFIERS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  MODIFIERID INTEGER NOT NULL,
  FEATUREID INTEGER NOT NULL,
  COST INTEGER NOT NULL,
  LEVEL INTEGER NOT NULL
);
CREATE TABLE USERQUIRKS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  QUIRKID INTEGER NOT NULL,
  COST INTEGER NOT NULL
);
CREATE TABLE USERS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(255),
  CURRENTPOINTS INTEGER DEFAULT 0,
  MAXPOINTS INTEGER,
  ST INTEGER DEFAULT 10,
  DX INTEGER DEFAULT 10,
  IQ INTEGER DEFAULT 10,
  HT INTEGER DEFAULT 10,
  HP INTEGER DEFAULT 10,
  WILL INTEGER DEFAULT 10,
  PER INTEGER DEFAULT 10,
  FP INTEGER DEFAULT 10,
  SM INTEGER DEFAULT 0,
  BS DOUBLE DEFAULT 5.0,
  MOVE INTEGER DEFAULT 5,
  NOFINEMANIPULATORS BOOLEAN DEFAULT FALSE,
  PLAYER VARCHAR(255),
  GROWTH INTEGER DEFAULT 0 NOT NULL,
  WEIGHT INTEGER DEFAULT 0 NOT NULL,
  AGE INTEGER DEFAULT 0 NOT NULL,
  TL INTEGER DEFAULT 0 NOT NULL,
  TLCOST INTEGER DEFAULT 0 NOT NULL,
  HEAD INTEGER DEFAULT 0 NOT NULL,
  TORSE INTEGER DEFAULT 0 NOT NULL,
  ARM INTEGER DEFAULT 0 NOT NULL,
  LEG INTEGER DEFAULT 0 NOT NULL,
  HAND INTEGER DEFAULT 0 NOT NULL,
  FOOT INTEGER DEFAULT 0 NOT NULL
);
CREATE TABLE USERSKILLS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  SKILLID INTEGER NOT NULL,
  COST INTEGER NOT NULL,
  LEVEL INTEGER NOT NULL
);
CREATE TABLE USERSKILLSPECIALIZATIONS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  SKILLSPECIALIZATIONID INTEGER NOT NULL,
  COST INTEGER NOT NULL,
  LEVEL INTEGER NOT NULL
);
CREATE TABLE USERSPELLS
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  SPELLID INTEGER NOT NULL,
  COST INTEGER NOT NULL,
  LEVEL INTEGER NOT NULL
);
CREATE TABLE USERTECHNIQUES
(
  ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERID INTEGER NOT NULL,
  TECHNIQUEID INTEGER NOT NULL,
  COST INTEGER NOT NULL,
  LEVEL INTEGER NOT NULL
);
ALTER TABLE ADDONS ADD FOREIGN KEY (FEATUREID) REFERENCES FEATURES (ID);
ALTER TABLE FEATUREADDONS ADD FOREIGN KEY (ADDONID) REFERENCES ADDONS (ID);
ALTER TABLE FEATUREADDONS ADD FOREIGN KEY (USERFEATUREID) REFERENCES USERFEATURES (ID);
CREATE INDEX "schema_version_ir_idx" ON "schema_version" ("installed_rank");
CREATE INDEX "schema_version_s_idx" ON "schema_version" ("success");
CREATE INDEX "schema_version_vr_idx" ON "schema_version" ("version_rank");
ALTER TABLE SKILLSPECIALIZATIONS ADD FOREIGN KEY (SKILLID) REFERENCES SKILLS (ID);
ALTER TABLE USERCULTURAS ADD FOREIGN KEY (CULTURAID) REFERENCES CULTURAS (ID);
ALTER TABLE USERCULTURAS ADD FOREIGN KEY (CHARACTERID) REFERENCES USERS (ID);
ALTER TABLE USERFEATURES ADD FOREIGN KEY (FEATUREID) REFERENCES FEATURES (ID);
ALTER TABLE USERFEATURES ADD FOREIGN KEY (USERID) REFERENCES USERS (ID);
ALTER TABLE USERLANGUAGES ADD FOREIGN KEY (LANGUAGEID) REFERENCES LANGUAGES (ID);
ALTER TABLE USERLANGUAGES ADD FOREIGN KEY (USERID) REFERENCES USERS (ID);
ALTER TABLE USERQUIRKS ADD FOREIGN KEY (QUIRKID) REFERENCES QUIRKS (ID);
ALTER TABLE USERQUIRKS ADD FOREIGN KEY (USERID) REFERENCES USERS (ID);
ALTER TABLE USERSPELLS ADD FOREIGN KEY (SPELLID) REFERENCES SPELLS (ID);
ALTER TABLE USERSPELLS ADD FOREIGN KEY (USERID) REFERENCES USERS (ID);
