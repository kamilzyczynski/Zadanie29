INSERT INTO application_user (username, password) VALUES ('admin', '{noop}admin');
INSERT INTO application_user (username, password) VALUES ('user', '{noop}user');

INSERT INTO user_role (user_id, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_role (user_id, role) VALUES (2, 'ROLE_USER');