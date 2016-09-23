CREATE TABLE note_items
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  itemtype VARCHAR(255),
  itemid INT,
  noteid INT,
  CONSTRAINT note_items_notes_id_fk FOREIGN KEY (noteid) REFERENCES notes (ID)
);
CREATE INDEX "note_items_itemtype_itemid_index" ON note_items (itemtype, itemid);
CREATE INDEX "note_items_noteid_index" ON note_items (noteid)