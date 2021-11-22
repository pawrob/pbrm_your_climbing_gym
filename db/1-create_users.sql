CREATE DATABASE perfectbeta;
CREATE DATABASE perfectbeta_test;
CREATE USER perfectbeta_admin WITH PASSWORD '0ndr4123!';
CREATE USER perfectbeta_auth WITH PASSWORD 'rungn3123!';
CREATE USER perfectbeta_mok WITH PASSWORD 'r0ck3ntry123!';
CREATE USER perfectbeta_mos WITH PASSWORD 'j4k0b1k123!';
CREATE USER perfectbeta_moch WITH PASSWORD 'lach3t4!';

GRANT ALL PRIVILEGES ON DATABASE perfectbeta TO perfectbeta_admin;
GRANT ALL PRIVILEGES ON DATABASE perfectbeta_test TO perfectbeta_admin;