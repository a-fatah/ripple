CREATE TABLE IF NOT EXISTS CITY
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS COUNTRY
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS AIRPORT
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS AIRLINE
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS USER
(
    id           UUID PRIMARY KEY,
    user_name    VARCHAR_IGNORECASE(50) NOT NULL,
    pass         VARCHAR_IGNORECASE(50) NOT NULL,
    agency       UUID,
    allow_refund BOOLEAN,
    allow_void   BOOLEAN,
    enabled      BOOLEAN                NOT NULL
);

CREATE TABLE IF NOT EXISTS ROLE
(
    user UUID,
    name VARCHAR_IGNORECASE(50) NOT NULL
);

INSERT INTO USER (id, user_name, pass, agency, enabled)
VALUES (random_uuid(),
        'admin',
        '{noop}admin',
        random_uuid(),
        true);
