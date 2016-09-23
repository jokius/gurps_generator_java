CREATE TABLE transportsspaces
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  skills VARCHAR(255),
  tl VARCHAR(255),
  name VARCHAR(255),
  sthp INT,
  handling INT,
  stabilityrating VARCHAR(255),
  ht VARCHAR(255),
  move VARCHAR(255),
  loadedweight FLOAT,
  load FLOAT,
  sizemodifier INT,
  occupant VARCHAR(255),
  damageresist VARCHAR(255),
  range VARCHAR(255),
  cost VARCHAR(255),
  locations VARCHAR(255)
)