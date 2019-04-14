INSERT INTO ROLES (NAME)
values ('ADMINISTRATOR'),
       ('USER');


INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, ROLE_ID)
values ('test@test.com', 'Adam', 'Kowalski', '$2a$10$3gC0OqRcgWBkeQe9wVwU3uzriHThp7DM6GdrB0QYD.XXfWNOozKiy', 1),
       ('test1@test.com', 'Anna', 'Nowak', '$2a$10$GR4fXpgTG6/VpKO/7Dy5y.Nuqmdl7nxwXZzfKLZSZXlo1txHKu7kG', 2);
