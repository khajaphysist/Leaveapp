CREATE TABLE IF NOT EXISTS employees (
  employee_id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL,
  PRIMARY KEY (employee_id));

CREATE TABLE IF NOT EXISTS leaves (
  leave_id INT(11) NOT NULL AUTO_INCREMENT,
  employee_id INT(11) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  status VARCHAR(45) NULL DEFAULT NULL,
  type VARCHAR(45) NOT NULL,
  PRIMARY KEY (leave_id),
  CONSTRAINT employee_id
    FOREIGN KEY (employee_id)
    REFERENCES Employees (employee_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);