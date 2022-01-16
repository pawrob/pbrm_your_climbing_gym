CREATE TABLE public.user
(
    id                             BIGSERIAL             NOT NULL,
    login                          CHARACTER VARYING(16) NOT NULL,
    password                       CHARACTER(60)         NOT NULL,
    email                          CHARACTER VARYING(64) NOT NULL,
    is_active                      BOOLEAN               NOT NULL DEFAULT TRUE,
    is_verified                    BOOLEAN               NOT NULL DEFAULT FALSE,
    password_reset_token           CHARACTER(64),
    password_reset_token_timestamp TIMESTAMP WITH TIME ZONE,
    email_reset_token              CHARACTER(64),
    email_reset_token_timestamp    TIMESTAMP WITH TIME ZONE,
    verify_token                   CHARACTER(64),
    verify_token_timestamp         TIMESTAMP WITH TIME ZONE,
    failed_login                   INTEGER               NOT NULL DEFAULT 0,
    version                        BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_login_key UNIQUE (login),
    CONSTRAINT user_email_key UNIQUE (email),
    CONSTRAINT user_email_correctness CHECK ( email ~* '^[-!#$%&*+-/=?^_`{|}~a-z0-9]+@[a-z]+.[a-z]{2,5}$'),
    CONSTRAINT user_password_bcrypt_form CHECK ( password ~*
                                                 '^[$]2[abxy][$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$')
);



CREATE TABLE public.personal_data
(
    id           BIGSERIAL            NOT NULL,
    user_id      BIGINT               NOT NULL,
    name         CHARACTER VARYING(30),
    surname      CHARACTER VARYING(30),
    phone_number CHARACTER VARYING(15),
    gender       BOOLEAN,
    language     CHARACTER VARYING(2) NOT NULL DEFAULT 'PL',
    version      BIGINT               NOT NULL DEFAULT 1,
    CONSTRAINT phone_number_correctness CHECK ( phone_number ~ '^[+]?[-\s0-9]{9,15}$'),
    CONSTRAINT name_correctness CHECK ( name ~* '^[a-z\sżźćńółęąś]+$' ),
    CONSTRAINT surname_correctness CHECK ( surname ~* '^[a-z\sżźćńółęąś]+$' ),
    CONSTRAINT personal_data_pkey PRIMARY KEY (id),
    CONSTRAINT personal_data_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE public.access_level_table
(
    id           BIGSERIAL             NOT NULL,
    user_id      BIGINT                NOT NULL,
    access_level CHARACTER VARYING(16) NOT NULL,
    is_active    BOOLEAN               NOT NULL DEFAULT TRUE,
    version      BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT access_level_correctness CHECK ( access_level_table.access_level IN
                                                ('CLIMBER', 'MANAGER', 'ADMINISTRATOR')),
    CONSTRAINT access_level_pkey PRIMARY KEY (id),
    CONSTRAINT access_level_user_id_access_level_key UNIQUE (user_id, access_level),
    CONSTRAINT access_level_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.session_log
(
    id               BIGSERIAL                NOT NULL,
    user_id          BIGINT,
    action_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address       CHARACTER VARYING(15)    NOT NULL,
    is_successful    BOOLEAN                  NOT NULL,
    version          BIGINT                   NOT NULL DEFAULT 1,
    CONSTRAINT ip_address_correctness CHECK (ip_address ~
                                             '^(([0-2]?[0-9]?[0-9])?\s?([.]||[:])){3,7}([0-2]?[0-9]?[0-9])?\s?$'),
    CONSTRAINT session_log_pkey PRIMARY KEY (id),
    CONSTRAINT session_log_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
);


CREATE TABLE public.climbing_gym
(
    id       BIGSERIAL             NOT NULL,
    gym_name CHARACTER VARYING(64) NOT NULL,
    status   CHARACTER VARYING(64) NOT NULL DEFAULT 'UNVERIFIED',
    user_id  BIGINT                NOT NULL,
    version  BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT gym_name_key UNIQUE (gym_name),
    CONSTRAINT climbing_gym_pkey PRIMARY KEY (id),
    CONSTRAINT climbing_gym_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION

);

CREATE TABLE public.climbing_gym_details
(
    id              BIGSERIAL NOT NULL,
    version         BIGINT    NOT NULL DEFAULT 1,
    climbing_gym_id BIGINT    NOT NULL,
    country         CHARACTER VARYING(64),
    city            CHARACTER VARYING(64),
    street          CHARACTER VARYING(64),
    number          CHARACTER VARYING(64),
    description     CHARACTER VARYING(2048),


    CONSTRAINT climbing_gym_details_pkey PRIMARY KEY (id),
    CONSTRAINT climbing_gym_id_fkey FOREIGN KEY (climbing_gym_id)
        REFERENCES public.climbing_gym (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION

);

CREATE TABLE public.gym_maintainer
(
    id              BIGSERIAL NOT NULL,
    climbing_gym_id BIGINT    NOT NULL,
    user_id         BIGINT    NOT NULL,
    is_active       BOOLEAN   NOT NULL DEFAULT TRUE,
    version         BIGINT    NOT NULL DEFAULT 1,
    CONSTRAINT gym_maintainer_pkey PRIMARY KEY (id),
    CONSTRAINT user_id_key UNIQUE (user_id),
    CONSTRAINT gym_maintainer_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT gym_maintainer_gym_id_fkey FOREIGN KEY (climbing_gym_id)
        REFERENCES public.climbing_gym (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE public.route
(
    id              BIGSERIAL             NOT NULL,
    climbing_gym_id BIGINT                NOT NULL,
    route_name      CHARACTER VARYING(64) NOT NULL,
    avg_rating      FLOAT                 NOT NULL DEFAULT 0,
    description     CHARACTER VARYING,
    holds_details   CHARACTER VARYING     NOT NULL,
    difficulty      CHARACTER VARYING(10),
    version         BIGINT                NOT NULL DEFAULT 1,
    CONSTRAINT route_name_key UNIQUE (route_name),
    CONSTRAINT route_pkey PRIMARY KEY (id),
    CONSTRAINT climbing_gym_id_fkey FOREIGN KEY (climbing_gym_id)
        REFERENCES public.climbing_gym (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.photo
(
    id        BIGSERIAL         NOT NULL,
    photo_url CHARACTER VARYING NOT NULL,
    route_id  BIGSERIAL         NOT NULL,
    version   BIGINT            NOT NULL DEFAULT 1,
    CONSTRAINT photo_url_key UNIQUE (photo_url),
    CONSTRAINT photo_pkey PRIMARY KEY (id),
    CONSTRAINT route_id_fkey FOREIGN KEY (route_id)
        REFERENCES public.route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.favourites
(
    climber_id BIGINT NOT NULL,
    route_id   BIGINT NOT NULL,
    version    BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT favourites_pkey PRIMARY KEY (climber_id, route_id),
    CONSTRAINT favourites_climber_id_fkey FOREIGN KEY (climber_id)
        REFERENCES public.user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT favourites_route_id_fkey FOREIGN KEY (route_id)
        REFERENCES public.route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.rating
(
    id       BIGSERIAL NOT NULL,
    rate     FLOAT     NOT NULL,
    comment  CHARACTER VARYING,
    route_id BIGSERIAL NOT NULL,
    user_id  BIGINT    NOT NULL,
    version  BIGINT    NOT NULL DEFAULT 1,

    CONSTRAINT rating_pkey PRIMARY KEY (id),
    CONSTRAINT route_id_fkey FOREIGN KEY (route_id)
        REFERENCES public.route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rating_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.user (id) MATCH SIMPLE
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
  AND u.verify_token IS NULL
  AND al.is_active;


-- indexes
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
    INDEX IF EXISTS climbing_gym_user_id;
CREATE
    INDEX climbing_gym_user_id
    ON public.climbing_gym USING btree
        (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
    INDEX IF EXISTS gym_maintainer_user_id;
CREATE
    INDEX gym_maintainer_user_id
    ON public.gym_maintainer USING btree
        (user_id ASC NULLS LAST)
    TABLESPACE pg_default;
DROP
    INDEX IF EXISTS gym_maintainer_gym_id;
CREATE
    INDEX gym_maintainer_gym_id
    ON public.gym_maintainer USING btree
        (climbing_gym_id ASC NULLS LAST)
    TABLESPACE pg_default;


DROP
    INDEX IF EXISTS climbing_gym_details_gym_id;
CREATE
    INDEX climbing_gym_details_gym_id
    ON public.climbing_gym_details USING btree
        (climbing_gym_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
    INDEX IF EXISTS route_gym_id;
CREATE
    INDEX route_gym_id
    ON public.route USING btree
        (climbing_gym_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
    INDEX IF EXISTS photo_route_id;
CREATE
    INDEX photo_route_id
    ON public.photo USING btree
        (route_id ASC NULLS LAST)
    TABLESPACE pg_default;

DROP
    INDEX IF EXISTS rating_route_id;
CREATE
    INDEX rating_route_id
    ON public.rating USING btree
        (route_id ASC NULLS LAST)
    TABLESPACE pg_default;
DROP
    INDEX IF EXISTS rating_user_id;
CREATE
    INDEX rating_user_id
    ON public.rating USING btree
        (user_id ASC NULLS LAST)
    TABLESPACE pg_default;
DROP
    INDEX IF EXISTS favourites_climber_id;
CREATE
    INDEX favourites_climber_id
    ON public.favourites (climber_id);

DROP
    INDEX IF EXISTS favourites_route_id;
CREATE
    INDEX favourites_route_id
    ON public.favourites (route_id);

-- grants
GRANT USAGE ON SEQUENCE public.user_id_seq TO perfectbeta_mok;
GRANT USAGE ON SEQUENCE public.access_level_table_id_seq TO perfectbeta_mok;
GRANT USAGE ON SEQUENCE public.personal_data_id_seq TO perfectbeta_mok;
GRANT USAGE ON SEQUENCE public.session_log_id_seq TO perfectbeta_auth;
GRANT USAGE ON SEQUENCE public.climbing_gym_id_seq TO perfectbeta_mos;
GRANT USAGE ON SEQUENCE public.route_id_seq TO perfectbeta_mos;
GRANT USAGE ON SEQUENCE public.climbing_gym_details_id_seq TO perfectbeta_mos;
GRANT USAGE ON SEQUENCE public.gym_maintainer_id_seq TO perfectbeta_mos;
GRANT USAGE ON SEQUENCE public.photo_id_seq TO perfectbeta_mos;
GRANT USAGE ON SEQUENCE public.rating_id_seq TO perfectbeta_mos;
-- auth
GRANT SELECT ON TABLE public.authentication_view TO perfectbeta_auth;
GRANT INSERT, SELECT, UPDATE ON TABLE public.user TO perfectbeta_auth;
GRANT INSERT, SELECT, UPDATE ON TABLE public.session_log TO perfectbeta_auth;

GRANT INSERT, UPDATE, SELECT ON TABLE public.personal_data TO perfectbeta_auth;
GRANT INSERT, SELECT ON TABLE public.access_level_table TO perfectbeta_auth;
GRANT INSERT, UPDATE, SELECT, DELETE ON TABLE public.flyway_schema_history TO perfectbeta_mok;
GRANT INSERT, UPDATE, SELECT, DELETE ON TABLE public.flyway_schema_history TO perfectbeta_auth;
-- mok
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.access_level_table TO perfectbeta_mok;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public."user" TO perfectbeta_mok;
GRANT INSERT, UPDATE, SELECT, DELETE ON TABLE public.personal_data TO perfectbeta_mok;
-- mos
GRANT SELECT, UPDATE ON TABLE public.access_level_table TO perfectbeta_mos;
GRANT SELECT ON TABLE public."user" TO perfectbeta_mos;
GRANT SELECT ON TABLE public.personal_data TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.climbing_gym TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.route TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.climbing_gym_details TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.gym_maintainer TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.photo TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.favourites TO perfectbeta_mos;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.rating TO perfectbeta_mos;