DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(50)                 NOT NULL,
  email            VARCHAR(50)                 NOT NULL,
  password         VARCHAR(50)                 NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOL DEFAULT TRUE       NOT NULL,
  calories_per_day INTEGER DEFAULT 2000    NOT NULL
);
ALTER TABLE users AUTO_INCREMENT = 100000;
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(20),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  date_time    TIMESTAMP NOT NULL,
  description VARCHAR(50) NOT NULL,
  calories    INT NOT NULL,
  user_id     INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX meals_unique_user_datetime_idx
  ON meals (user_id, date_time);
ALTER TABLE meals AUTO_INCREMENT = 100002;