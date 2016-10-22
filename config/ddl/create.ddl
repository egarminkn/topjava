CREATE SEQUENCE global_seq START 1 INCREMENT 1;

CREATE TABLE meals (
  id          INT4         NOT NULL,
  calories    INT4         NOT NULL CHECK (calories >= 10 AND calories <= 5000),
  date_time   TIMESTAMP    NOT NULL,
  description VARCHAR(255) NOT NULL,
  user_id     INT4         NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_roles (
  user_id INT4 NOT NULL,
  role    VARCHAR(255)
);

CREATE TABLE users (
  id         INT4         NOT NULL,
  name       VARCHAR(255) NOT NULL,
  calories_per_day     DEFAULT 2000,
  email      VARCHAR(255) NOT NULL,
  enabled    BOOLEAN      NOT NULL,
  password   VARCHAR(255) NOT NULL,
  registered TIMESTAMP DEFAULT now(),
  PRIMARY KEY (id)
);

ALTER TABLE meals
  ADD CONSTRAINT meals_unique_user_datetime_idx UNIQUE (user_id, date_time);

ALTER TABLE users
  ADD CONSTRAINT UK_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

ALTER TABLE meals
  ADD CONSTRAINT FK677c66qpjr7234luomahc1ale
FOREIGN KEY (user_id)
REFERENCES users;

ALTER TABLE user_roles
  ADD CONSTRAINT FKhfh9dx7w3ubf1co1vdev94g3f
FOREIGN KEY (user_id)
REFERENCES users;
