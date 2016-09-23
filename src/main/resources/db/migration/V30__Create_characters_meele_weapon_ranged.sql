CREATE TABLE charactersmeleeweaponrangeds
(
    ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CHARACTERID INTEGER NOT NULL,
    MELEEWEAPONRANGEDID INTEGER NOT NULL,
    COUNT INTEGER,

    FOREIGN KEY (CHARACTERID) REFERENCES CHARACTERS (ID),
    FOREIGN KEY (MELEEWEAPONRANGEDID) REFERENCES MELEEWEAPONRANGEDS (ID)
);
