DROP TABLE LOG_EVENT_MODEL;
CREATE TABLE LOG_EVENT_MODEL
(
    ID VARCHAR(255) not null
        primary key,
    STATE VARCHAR(255),
    TYPE VARCHAR(255),
    HOST VARCHAR(255),
    TIMESTAMP BIGINT
);
CREATE TABLE LOG_EVENT_ALERT
(
    ID VARCHAR(255) not null
        primary key,
    DURATION BIGINT,
    TYPE VARCHAR(255),
    HOST VARCHAR(255),
    ALERT BOOLEAN
);