
CREATE TABLE public."user"
(
-- klucz glowny identyfikujacy rekord w tabeli
    id                             BIGSERIAL             NOT NULL,
-- login identyfikujacy konto uzytkownika w aplikacji
    login                          CHARACTER VARYING(16) NOT NULL,
-- wynik uzycia funkcji Bcrypt na hasle
    password                       CHARACTER(60)         NOT NULL,
-- adres email do kontaktu
    email                          CHARACTER VARYING(64) NOT NULL,
-- czy konto jest niezablokowane
    is_active                      BOOLEAN               NOT NULL DEFAULT TRUE,
-- czy adres email zostal zweryfikowany
    is_verified                    BOOLEAN               NOT NULL DEFAULT FALSE,
-- zeton wysylany na adres email przy resetowaniu hasla
    password_reset_token           CHARACTER(64),
-- czas wystawienia tokenu
    password_reset_token_timestamp TIMESTAMP WITH TIME ZONE,
-- zeton wysylany na adres email przy resetowaniu emaila
    email_reset_token              CHARACTER(64),
-- czas wystawienia tokenu
    email_reset_token_timestamp    TIMESTAMP WITH TIME ZONE,
--  ilosc blednych prob logowania
    failed_login                   SMALLINT              NOT NULL DEFAULT 0,
-- wersja uzywana do blokad optymistycznych
    version                        BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_login_key UNIQUE (login),
    CONSTRAINT user_email_key UNIQUE (email),
    CONSTRAINT user_email_correctness CHECK ( email ~* '^[-!#$%&*+-/=?^_`{|}~a-z0-9]+@[a-z]+.[a-z]{2,5}$'
) ,
    CONSTRAINT user_password_bcrypt_form CHECK ( password ~*
                                                 '^[$]2[abxy][$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$'
        )
);


-- reprezentuje dane osobowe uzytkownika
CREATE TABLE public.personal_data
(
-- klucz glowny i jednoczesnie klucz obcy odnoszacy się do rekordu z tabeli user
    user_id      BIGINT               NOT NULL,
-- imie uzytkownika
    name         CHARACTER VARYING(30),
-- nazwisko uzytkownika
    surname      CHARACTER VARYING(30),
-- numer telefonu
    phone_number CHARACTER VARYING(15),
-- plec
    gender       BOOLEAN,
-- preferowany jezyk aplikacji
    language     CHARACTER VARYING(3) NOT NULL DEFAULT 'POL',
-- wersja uzywana do blokad optymistycznych
    version      BIGINT               NOT NULL DEFAULT 1,
    CONSTRAINT phone_number_correctness CHECK ( phone_number ~ '^[+]?[-\s0-9]{9,15}$'
) ,
    CONSTRAINT name_correctness CHECK ( name ~* '^[a-z\sżźćńółęąś]+$' ),
    CONSTRAINT surname_correctness CHECK ( surname ~* '^[a-z\sżźćńółęąś]+$' ),
    CONSTRAINT personal_data_pkey PRIMARY KEY (user_id),
    CONSTRAINT personal_data_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- reprezentuje poziomy dostępu dostepne dla danego uzytkownika
CREATE TABLE public.access_level_table
(
-- klucz glowny identyfikujacy rekord w tabeli
    id           BIGSERIAL             NOT NULL,
-- klucz obcy odnoszacy się do rekordu z tabeli user
    user_id      BIGINT                NOT NULL,
-- wyroznik poziomu dostępu
    access_level CHARACTER VARYING(16) NOT NULL,
-- czy poziom jest niezablokowany
    is_active    BOOLEAN               NOT NULL DEFAULT TRUE,
-- wersja uzywana do blokad optymistycznych
    version      BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT access_level_correctness CHECK ( access_level_table.access_level IN ('CLIMBER', 'MANAGER', 'ADMINISTRATOR')),
    CONSTRAINT access_level_pkey PRIMARY KEY (id),
    CONSTRAINT access_level_user_id_access_level_key UNIQUE (user_id, access_level),
    CONSTRAINT access_level_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE public.administrator
(
    access_level_id BIGINT NOT NULL,
    version         BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT entertainer_pkey PRIMARY KEY (access_level_id),
    CONSTRAINT entertainer_access_level_id_fkey FOREIGN KEY (access_level_id)
        REFERENCES public.access_level_table (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



CREATE TABLE public.manager
(
    access_level_id BIGINT NOT NULL,
    version         BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT management_pkey PRIMARY KEY (access_level_id),
    CONSTRAINT management_access_level_id_fkey FOREIGN KEY (access_level_id)
        REFERENCES public.access_level_table (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



CREATE TABLE public.climber
(
    access_level_id BIGINT NOT NULL,
    version         BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT client_pkey PRIMARY KEY (access_level_id),
    CONSTRAINT client_access_level_id_fkey FOREIGN KEY (access_level_id)
        REFERENCES public.access_level_table (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE OR REPLACE VIEW public.authentication_view
AS
SELECT al.id,
       u.login,
       u.password,
       al.access_level
FROM public."user" u
         JOIN access_level_table al
              ON u.id = al.user_id
WHERE u.is_active
  AND u.is_verified
  AND u.password_reset_token IS NULL
  AND al.is_active;



DROP
INDEX IF EXISTS personal_data_user_id;
CREATE
INDEX personal_data_user_id
    ON public.personal_data USING btree
        (user_id ASC NULLS LAST)
    TABLESPACE pg_default;


DROP
INDEX IF EXISTS access_level_user_id;
CREATE
INDEX access_level_user_id
    ON public.access_level_table USING btree
        (user_id ASC NULLS LAST)
    TABLESPACE pg_default;



DROP
INDEX IF EXISTS manager_access_level_id;
CREATE
INDEX manager_access_level_id
    ON public.manager USING btree
        (access_level_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
INDEX IF EXISTS administrator_access_level_id;
CREATE
INDEX administrator_access_level_id
    ON public.administrator USING btree
        (access_level_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
INDEX IF EXISTS climber_access_level_id;
CREATE
INDEX climber_access_level_id
    ON public.climber USING btree
        (access_level_id ASC NULLS LAST)
    TABLESPACE pg_default;

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
VALUES (-4, 'Arkadiusz', 'Remplewicz', '444444444', true, 'ENG');
INSERT INTO public.personal_data (user_id, name, surname, phone_number, gender)
VALUES (-5, 'Aleksander', 'Drajling', '555555555', true);

--access_level
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-11, -1, 'MANAGER',true);
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-21, -1, 'CLIMBER',false);
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-31, -1, 'ADMINISTRATOR',false);
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-12, -2, 'CLIMBER',true);
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-22, -2, 'MANAGER',false);
INSERT INTO public.access_level_table (id, user_id, access_level,is_active)
VALUES (-32, -2, 'ADMINISTRATOR',false);
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

--MANAGER
INSERT INTO public.manager (access_level_id)
VALUES (-11);
INSERT INTO public.manager (access_level_id)
VALUES (-14);
INSERT INTO public.manager (access_level_id)
VALUES (-22);
INSERT INTO public.manager (access_level_id)
VALUES (-33);
INSERT INTO public.manager (access_level_id)
VALUES (-35);

--ADMINISTRATOR
INSERT INTO public.administrator (access_level_id)
VALUES (-13);
INSERT INTO public.administrator (access_level_id)
VALUES (-15);
INSERT INTO public.administrator (access_level_id)
VALUES (-32);
INSERT INTO public.administrator (access_level_id)
VALUES (-34);
INSERT INTO public.administrator (access_level_id)
VALUES (-31);


--CLIMBER
INSERT INTO public.climber (access_level_id)
VALUES (-12);
INSERT INTO public.climber (access_level_id)
VALUES (-21);
INSERT INTO public.climber (access_level_id)
VALUES (-23);
INSERT INTO public.climber (access_level_id)
VALUES (-24);
INSERT INTO public.climber (access_level_id)
VALUES (-25);