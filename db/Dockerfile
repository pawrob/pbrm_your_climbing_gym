FROM postgres:alpine
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD m4gnus123!
COPY ./1-create_users.sql /docker-entrypoint-initdb.d
