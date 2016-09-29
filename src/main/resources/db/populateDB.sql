DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-30T10:00', 'Завтрак', 500, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-30T13:00', 'Обед', 1000, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-30T20:00', 'Ужин', 500, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-31T10:00', 'Завтрак', 1000, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-31T13:00', 'Обед', 500, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-05-31T20:00', 'Ужин', 510, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-06-01T14:00', 'Админ ланч', 510, 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2015-06-01T21:00', 'Админ ужин', 1500, 100001);