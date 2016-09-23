CREATE TABLE characterstransportswaters
(
    ID INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CHARACTERID INTEGER NOT NULL,
    ITEMID INTEGER NOT NULL,
    COUNT INTEGER,

    FOREIGN KEY (CHARACTERID) REFERENCES CHARACTERS (ID),
    FOREIGN KEY (ITEMID) REFERENCES TRANSPORTSWATERS (ID)
);
