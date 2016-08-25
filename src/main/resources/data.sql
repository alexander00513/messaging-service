INSERT INTO user_ (id, first_name, last_name, email, login, password_hash, created_by, created_date) VALUES (nextval('user__id'), 'John', 'Doe', 'john.doe@example.com', 'user', '$2a$10$ayxRBWM8eOhuCAUiyikjAesnHY/93YYC3GrSJdkkcD9rxr98TyeW2', 'system', now());
INSERT INTO user_ (id, first_name, last_name, email, login, password_hash, created_by, created_date) VALUES (nextval('user__id'), 'Richard', 'Rou', 'richard.rou@example.com', 'admin', '$2a$10$WOcoCor6PLpbfcBZAnWAeOxgfZXWAkIB/YORfeHigM1VNrj39kTgy', 'system', now());

INSERT INTO authority (name) VALUES ('ROLE_USER');
INSERT INTO authority (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO message (id, subject, message, from_id, to_id, created_by, created_date) VALUES (nextval('message_id'), 'Test subject', 'Test message', 1, 2, 'system', now());