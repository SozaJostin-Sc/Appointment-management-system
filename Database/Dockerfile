FROM postgres:15-alpine
LABEL authors="jostin"

COPY init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_DB=medical
ENV LANG es_ES.utf8