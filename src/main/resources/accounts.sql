CREATE TABLE account
(
    id      UUID PRIMARY KEY,
    agency  UUID,
    title   VARCHAR,
    balance decimal,
    type    VARCHAR
);

CREATE TABLE transaction
(
    id        UUID PRIMARY KEY,
    account   UUID,
    timestamp TIMESTAMP,
    type      VARCHAR,
    period    VARCHAR
);

CREATE TABLE journal_entry
(
    id          UUID,
    account     UUID REFERENCES account (id),
    transaction UUID REFERENCES transaction (id),
    amount      DECIMAL,
    is_debit    BOOLEAN
);
