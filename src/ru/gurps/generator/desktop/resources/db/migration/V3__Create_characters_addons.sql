ALTER TABLE PUBLIC.CHARACTERSADDONS ADD CHARACTERID INT NOT NULL DEFAULT 0;
ALTER TABLE PUBLIC.CHARACTERSADDONS ADD FEATUREID INT NOT NULL DEFAULT 0;

ALTER TABLE PUBLIC.CHARACTERSADDONS
ADD FOREIGN KEY (CHARACTERID) REFERENCES PUBLIC.CHARACTERS (ID);

ALTER TABLE PUBLIC.CHARACTERSADDONS
ADD FOREIGN KEY (FEATUREID) REFERENCES PUBLIC.FEATURES (ID);

UPDATE PUBLIC.CHARACTERSADDONS
SET CHARACTERID = (SELECT cf.CHARACTERID
            FROM  PUBLIC.CHARACTERSFEATURES cf
            WHERE cf.ID =  PUBLIC.CHARACTERSADDONS.CHARACTERFEATUREID),
  FEATUREID = (SELECT cf.FEATUREID
            FROM PUBLIC.CHARACTERSFEATURES cf
            WHERE cf.ID =  PUBLIC.CHARACTERSADDONS.CHARACTERFEATUREID)
WHERE PUBLIC.CHARACTERSADDONS.ID IN (SELECT cf.ID FROM PUBLIC.CHARACTERSFEATURES cf);

ALTER TABLE PUBLIC.CHARACTERSADDONS DROP CONSTRAINT CONSTRAINT_769;
ALTER TABLE PUBLIC.CHARACTERSADDONS DROP COLUMN CHARACTERFEATUREID;