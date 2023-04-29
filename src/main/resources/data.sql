-- roles

INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');

-- users

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
    ('Luka', 'Djordjevic', '06912039','luka@mail.com', 'Hadzi-Ruvimova 45', '$2a$12$EAdGoHdReN.9RKGfYt3HY.DgPu3gCtCH1Bkzb5QCQmzLen4ZgETJi', null, true);

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
    ('Marko', 'Janosevic', '064875321', 'marko@mail.com', 'Mise Dimitrijevica 18', '$2a$12$uvrpCV2.4YqkBGgMieXBY.ozpmzCX1kXL2q.rJbQXi67brtPvrJ.S', null, true);

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
    ('Milos', 'Cuturic', '0651234567', 'milos@mail.com', 'Hadzi-Ruvimova 45', '$2a$12$I5hPbl8r2OflbcElIonVlez.fjDvNdzVMjXvWPSUIEeUGGenjarn.', null, true);

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
    ('Sara', 'Stojanovic', '0639876543', 'sara.stojanovic@example.org', 'Bulevar Mihajla Pupina 67', '$2a$12$9zUpiWjS0/YLuE98/e8UseoeXaQYpt9sROHeQ7qSNTDMX10skBipK', null, true);

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
    ('Ivan', 'Nikolic', '0623456789', 'ivan.nikolic@mail.net', 'Gospodara Vucica 12', '$2a$12$6mPSNDf3Z6vtuKVRkJ9EoOl8/FJGwDRLuvCB1HDQ.j/bJ6Nu18anW', null, true);

INSERT INTO USERS (name, surname, telephone_number, email, address, password, last_password_reset_date, active) VALUES
     ('Milica', 'Markovic', '0664321567', 'milica.markovic@example.net', 'Njegoseva 5', '$2a$12$LznwPRblZczYi4LzgEHSjetyfYQ4c9YUWR0vJKlaJGmvuisnUhGSW', null, true);

-- user-role

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1);


INSERT INTO CERTIFICATE_REQUESTS (approved, flags, request_date, signature_algorithm, certificate_type, issuer_id, owner_id) VALUES
                                 (true, '1,2,3,4,5,6,7', '2023-04-04', 'SHA256withRSA', 'ROOT', null, 1);

INSERT INTO CERTIFICATES (alias, flags, serial_number, signature_algorithm, certificate_type, valid, valid_from, valid_to,
                          certificate_request_id, issuer_id, owner_id) VALUES
                         ('-68299581185896008646150721084814966708_Luka_Djordjevic', '1,2,3,4,5,6,7',
                          -68299581185896008646150721084814966708, 'SHA256withRSA', 'ROOT', true, '2023-04-04',
                          '2024-04-04', 1, null, 1);