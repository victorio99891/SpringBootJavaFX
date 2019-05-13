INSERT INTO ROLES (NAME)
values ('ADMINISTRATOR'),
       ('USER'),
       ('DOCTOR'),
       ('TECHNICIAN');

INSERT INTO PATIENTS (FIRST_NAME, LAST_NAME, PESEL, IS_WOMEN)
values ('Danuta', 'Pokorny', '93031667841', 1),
       ('Marta', 'Bosko', '83101568266', 1),
       ('Kazik', 'Kowalski', '60040918576', 0),
       ('Krystyna', 'Nowacka', '88022641287', 1),
       ('Joachim', 'Wroński', '95073126115', 0),
       ('Jan', 'Ząbek', '72073035951', 0);

INSERT INTO IMAGING_TECHNIQUES (NAME)
values ('RTG'),
       ('CT'),
       ('MRI'),
       ('USG');

INSERT INTO EXAMINATIONS (IMG_TECH_ID, PATIENT_ID, STATUS, DESCRIPTION)
values (1, 3, 'REGISTERED',''),
       (2, 1, 'REQUESTED',''),
       (3, 2, 'IN PROGRESS',''),
       (1, 4, 'FOR DESCRIPTION',''),
       (1, 6, 'DONE','	=======================================
                                        PATIENT DETAILS
=======================================
Name: Jan
Lastname: Ząbek
PESEL: 72073035951
=======================================
                                 EXAMINATION DETAILS
=======================================
Date: 2019-04-13
Examination type: RTG

Description: All seems to be normal. Patient is full of health.');


INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, ROLE_ID)
values ('test@test.com', 'Adam', 'Kowalski', '$2a$10$3gC0OqRcgWBkeQe9wVwU3uzriHThp7DM6GdrB0QYD.XXfWNOozKiy', 1),
       ('test1@test.com', 'Anna', 'Nowak', '$2a$10$GR4fXpgTG6/VpKO/7Dy5y.Nuqmdl7nxwXZzfKLZSZXlo1txHKu7kG', 2);
