INSERT INTO ROLES (NAME)
values ('ADMINISTRATOR'),
       ('USER'),
       ('DOCTOR'),
       ('TECHNICIAN');

INSERT INTO PATIENTS (FIRST_NAME, LAST_NAME, PESEL, IS_WOMAN)
values ('Danuta', 'Pokorny', '93031667841', 1),
       ('Marta', 'Bosko', '83101568266', 1),
       ('Kazik', 'Kowalski', '60040918576', 0),
       ('Krystyna', 'Nowacka', '88022641287', 1),
       ('Joachim', 'Wroński', '95073126115', 0),
       ('Jan', 'Ząbek', '72073035951', 0);


INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, ROLE_ID)
values ('test@test.com', 'Adam', 'Kowalski', '$2a$10$3gC0OqRcgWBkeQe9wVwU3uzriHThp7DM6GdrB0QYD.XXfWNOozKiy', 1),
       ('test1@test.com', 'Anna', 'Nowak', '$2a$10$GR4fXpgTG6/VpKO/7Dy5y.Nuqmdl7nxwXZzfKLZSZXlo1txHKu7kG', 2);
