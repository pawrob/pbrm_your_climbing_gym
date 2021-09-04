--user
--login:jkowalski haslo:Kowal123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-1, 'jkowalski', '$2y$12$ealytKG0z6p3hipT2XpxfeDDL7fPS7Dy5aDKkOzKaMnSUfoYnCuPu', 'kowal@example.com', true,
        true);
--login:anowak haslo:Nowak123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-2, 'anowak', '$2y$12$Rv/0rpWtKOKcoM2k2R7zbOyNjSx2hVDceU8EE9RXJRl6URTQehw/a', 'nowak@example.com', true, true);
--login:pbucki haslo:Ppucki123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-3, 'pbucki', '$2a$10$1LtTBewk4PopyuRkUPbgdO3j2uifQKNB7bBL8tholoamonzHUjENS', 'pbucki@example.com', true,
        true);
--login:rmakrocki haslo:Makro123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-4, 'rmakrocki', '$2a$10$ZvRquOlERcwSp/OGgoJPYeerZZnTNeD/xQUASse3Tgs85aAn1lHx2', 'rmakrocki@example.com', true,
        true);
--login:johndoe haslo:Jdoe123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-5, 'johndoe', '$2a$10$CHy4qgOzzBZbJTXw1nGfAuAjuwCdt1Y8NCD41.LIyYMfLr750KiTC', 'jdoe@example.com', true,
        true);

--personal_data
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-1, 'Jan', 'Kowalski', '111111111', true);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-2, 'Anna', 'Nowak', '222222222', false);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-3, 'Paweł', 'Bucki', '333333333', true);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-4, 'Robert', 'Makrocki', '444444444', true);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender, language)
VALUES (-5, 'John', 'Doe', '555555555', true, 'EN');

--access_level
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-11, -1, 'MANAGER', true);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-21, -1, 'CLIMBER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-31, -1, 'ADMINISTRATOR', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-12, -2, 'CLIMBER', true);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-22, -2, 'MANAGER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-32, -2, 'ADMINISTRATOR', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-13, -3, 'ADMINISTRATOR', true);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-23, -3, 'CLIMBER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-33, -3, 'MANAGER', true);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-14, -4, 'MANAGER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-24, -4, 'CLIMBER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-34, -4, 'ADMINISTRATOR', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-15, -5, 'ADMINISTRATOR', true);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-25, -5, 'CLIMBER', false);
INSERT INTO public.access_level_table (id, user_id, access_level, is_active)
VALUES (-35, -5, 'MANAGER', false);