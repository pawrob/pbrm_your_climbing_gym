FROM postgres:alpine
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD m4gnus123!
RUN chmod 777 /docker-entrypoint-initdb.d
RUN chmod 777 /var
COPY ./1-create_users.sql /docker-entrypoint-initdb.d
