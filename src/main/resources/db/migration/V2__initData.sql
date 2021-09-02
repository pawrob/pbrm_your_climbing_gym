--user
--login:jkowalski haslo:Kowal123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-1, 'jkowalski', '$2y$12$ealytKG0z6p3hipT2XpxfeDDL7fPS7Dy5aDKkOzKaMnSUfoYnCuPu', 'kowal@example.com', true,
        true);
--login:anowak haslo:Nowak123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-2, 'anowak', '$2y$12$Rv/0rpWtKOKcoM2k2R7zbOyNjSx2hVDceU8EE9RXJRl6URTQehw/a', 'nowak@example.com', true, true);
--login:msipinski haslo:Sipin123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-3, 'msipinski', '$2y$12$q1rYhhNHlYr2Mq.vZuIklO31YsVliLfii2ug4WkvB7D.CtFUUhnNy', 'sipin@example.com', true,
        true);
--login:aremplewicz haslo:Rempek123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-4, 'aremplewicz', '$2y$12$yyzobmoHaPCC87aPF27I2.4IjNRPLo12KNB8HE27u15dcPczlRls6', 'rempek@example.com', true,
        true);
--login:adrajling haslo:Drajling123!
INSERT INTO public.user (id, login, password, email, is_active, is_verified)
VALUES (-5, 'adrajling', '$2y$12$g66Xs5jGrjGiSQRI.ikuMeadyAHwpgF4noiofSG09j47CQV.EnDK6', 'drajling@example.com', true,
        true);

--personal_data
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-1, 'Jan', 'Kowalski', '111111111', true);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-2, 'Anna', 'Nowak', '222222222', false);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-3, 'Mateusz', 'Sipiński', '333333333', true);
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender, language)
VALUES (-4, 'Arkadiusz', 'Remplewicz', '444444444', true, 'EN');
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-5, 'Aleksander', 'Drajling', '555555555', true);

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