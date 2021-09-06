CREATE TABLE public."user"
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
ALTER TABLE public."user"
    OWNER TO perfectbeta_admin;


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
ALTER TABLE public."personal_data"
    OWNER TO perfectbeta_admin;

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
ALTER TABLE public."access_level_table"
    OWNER TO perfectbeta_admin;

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
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
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

ALTER TABLE public."authentication_view"
    OWNER TO perfectbeta_admin;

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


-- grants
GRANT USAGE ON SEQUENCE public.user_id_seq TO perfectbeta_mok;
GRANT USAGE ON SEQUENCE public.access_level_table_id_seq TO perfectbeta_mok;
GRANT USAGE ON SEQUENCE public.session_log_id_seq TO perfectbeta_auth;

-- auth
GRANT SELECT ON TABLE public.authentication_view TO perfectbeta_auth;
GRANT INSERT, SELECT, UPDATE ON TABLE public.user TO perfectbeta_auth;
GRANT INSERT, SELECT, UPDATE ON TABLE public.session_log TO perfectbeta_auth;

GRANT INSERT, UPDATE, SELECT ON TABLE public.personal_data TO perfectbeta_auth;
GRANT INSERT, SELECT ON TABLE public.access_level_table TO perfectbeta_auth;
GRANT INSERT, UPDATE, SELECT, DELETE ON TABLE public.flyway_schema_history TO perfectbeta_auth;
-- mok
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.access_level_table TO perfectbeta_mok;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public."user" TO perfectbeta_mok;
GRANT INSERT, UPDATE, SELECT, DELETE ON TABLE public.personal_data TO perfectbeta_mok;
-- mos
GRANT SELECT, UPDATE ON TABLE public.access_level_table TO perfectbeta_mos;
GRANT SELECT ON TABLE public."user" TO perfectbeta_mos;
GRANT SELECT ON TABLE public.personal_data TO perfectbeta_mos;