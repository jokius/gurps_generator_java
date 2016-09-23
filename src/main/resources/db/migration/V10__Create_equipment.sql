CREATE TABLE equipments
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  applicationareaid INT,
  name VARCHAR(255),
  tl VARCHAR(255),
  description CLOB,
  cost VARCHAR(255),
  weight VARCHAR(255),
  time VARCHAR(255),
  legalityclass INT,
  CONSTRAINT equipments_equipmentsapplicationareas_id_fk FOREIGN KEY (applicationareaid) REFERENCES equipmentsapplicationareas (id)
);

CREATE INDEX "equipments_applicationareaid_index" ON equipments (applicationareaid)
