CREATE TABLE armorsaddons
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  armortype VARCHAR(255),
  armorid INT,
  name VARCHAR(255),
  protection VARCHAR(255),
  damageresist VARCHAR(255),
  cost VARCHAR(255),
  weight VARCHAR(255)
);
CREATE INDEX "armorsaddons_armortype_armorid_index" ON armorsaddons (ARMORTYPE, ARMORID)