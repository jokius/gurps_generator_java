CREATE TABLE meleeweapons
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  skills VARCHAR(255),
  tl VARCHAR(255),
  name VARCHAR(255),
  damage VARCHAR(255),
  reach VARCHAR(255),
  parry VARCHAR(255),
  cost VARCHAR(255),
  weight VARCHAR(255),
  st INT,
  twohands BOOLEAN,
  training BOOLEAN
)