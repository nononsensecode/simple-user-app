INSERT INTO user (id, created_at, username, email, first_name, last_name, is_enabled, is_email_verified, password) VALUES ('CDDBA91602144818B3B8E68ACC4674CF', {ts '2021-01-09 15:11:00.00'}, 'kaushikam', 'kaushikam@gmail.com', 'kaushik', 'asokan', 'Y', 'N', '$2a$12$PlhESbPi83zE2COHqqyUYuoq/a.FALDnil5sQebPj1fEJo6I0Y86e');
INSERT INTO client (id, name) VALUES (1, 'hero-app-client');
INSERT INTO role (id, name, client_id) VALUES (1, 'heroes', 1);
INSERT INTO role (id, name, client_id) VALUES (2, 'villains', 1);
INSERT INTO user_roles (user_id, roles_id) VALUES ('CDDBA91602144818B3B8E68ACC4674CF', 1);