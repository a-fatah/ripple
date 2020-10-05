CREATE TABLE IF NOT EXISTS POLICY
(
    id         UUID PRIMARY KEY,
    start_date DATE,
    end_date   DATE
);
CREATE TABLE IF NOT EXISTS POLICY_ALLOWED_AIRLINE
(
    policy  UUID,
    airline VARCHAR
);
CREATE TABLE IF NOT EXISTS POLICY_DISCOUNT
(
    POLICY        uuid,
    name          VARCHAR,
    booking_class VARCHAR,
    type          VARCHAR,
    strategy      VARCHAR,
    value         DECIMAL
);
CREATE TABLE IF NOT EXISTS POLICY_COMMISSION
(
    POLICY        uuid,
    name          VARCHAR,
    booking_class VARCHAR,
    strategy      VARCHAR,
    value         DECIMAL
);
CREATE TABLE IF NOT EXISTS POLICY_BLOCKED_AIRLINE
(
    POLICY  uuid,
    name    VARCHAR,
    airline VARCHAR
);
CREATE TABLE IF NOT EXISTS POLICY_BLOCKED_SECTOR
(
    POLICY uuid,
    name   VARCHAR,
    origin VARCHAR,
    dest   VARCHAR
);
CREATE TABLE IF NOT EXISTS POLICY_BLOCKED_SECTOR_ON_AIRLINE
(
    policy  uuid,
    name    VARCHAR,
    origin  VARCHAR,
    dest    VARCHAR,
    airline VARCHAR
);

CREATE TABLE IF NOT EXISTS AGENCY
(
    id     UUID PRIMARY KEY,
    name   VARCHAR,
    email  VARCHAR,
    phone  VARCHAR,
    active BOOLEAN
);
CREATE TABLE IF NOT EXISTS AGENCY_POLICY
(
    AGENCY UUID,
    policy UUID
);
CREATE TABLE IF NOT EXISTS ADDRESS
(
    AGENCY  UUID PRIMARY KEY,
    street  VARCHAR,
    city    VARCHAR,
    country VARCHAR
);

CREATE TABLE IF NOT EXISTS CONTRACT
(
    id                    UUID PRIMARY KEY,
    code                  VARCHAR,
    type                  VARCHAR,
    parent                UUID,
    start_date            DATETIME,
    period_length         INT,
    value_cap             DECIMAL,
    locked                BOOLEAN,
    recurrent             BOOLEAN,
    number_of_cycles      INTEGER,
    current_cycle         INTEGER,
    settlement_period     INT,
    allow_distribution    BOOLEAN,
    fare_review           BOOLEAN,
    allow_void            BOOLEAN,
    allow_refund          BOOLEAN,
    allow_exchange        BOOLEAN,
    refund_review         BOOLEAN,
    allow_revalidation    BOOLEAN,
    void_charges          DECIMAL,
    refund_charges        DECIMAL,
    exchange_charges      DECIMAL,
    allow_terminal_access BOOLEAN,
    state                 VARCHAR
);
CREATE TABLE IF NOT EXISTS ACCESS_POINT
(
    id       UUID primary key,
    agency   UUID,
    provider VARCHAR,
    endpoint VARCHAR,
    name     VARCHAR
);
CREATE TABLE IF NOT EXISTS CREDENTIALS
(
    ACCESS_POINT  UUID,
    type          VARCHAR,
    username      VARCHAR,
    password      VARCHAR,
    target_branch VARCHAR
);
CREATE TABLE IF NOT EXISTS CONTRACT_ACCESS_POINT
(
    contract     UUID,
    access_point UUID
);
CREATE TABLE IF NOT EXISTS CONTRACT_POLICY
(
    contract UUID,
    policy   UUID
);
CREATE TABLE IF NOT EXISTS CONTRACT_CONSOLIDATOR
(
    CONTRACT     UUID,
    CONSOLIDATOR UUID
);
CREATE TABLE IF NOT EXISTS CONTRACT_CONTRACTOR
(
    CONTRACT   UUID,
    CONTRACTOR UUID
);
CREATE TABLE IF NOT EXISTS CONTRACT_PAYMENT
(
    id       UUID PRIMARY KEY,
    contract UUID,
    amount   DECIMAL,
    date     DATETIME
);
CREATE TABLE IF NOT EXISTS CONTRACT_TRANSACTION
(
    contract  UUID,
    timestamp TIMESTAMP,
    amount    DECIMAL
);

CREATE TABLE IF NOT EXISTS sale_log
(
    agency_id             UUID,
    issue_date            DATETIME,
    pax_name              VARCHAR,
    pnr                   VARCHAR,
    ticket_number         VARCHAR,
    doc_type              VARCHAR,
    airline               VARCHAR,
    agency                VARCHAR,
    user                  VARCHAR,
    fare                  DECIMAL,
    equivalent_fare       DECIMAL,
    taxes                 DECIMAL,
    other_charges         DECIMAL,
    discount_receivable   DECIMAL,
    discount_payable      DECIMAL,
    commission_receivable DECIMAL,
    commission_payable    DECIMAL,
    wht                   DECIMAL,
    total_receivable      DECIMAL,
    total_payable         DECIMAL,
    status                VARCHAR
);

CREATE TABLE IF NOT EXISTS exchange_log
(
    agency_id               UUID,
    issue_date              DATETIME,
    pax_name                VARCHAR,
    pnr                     VARCHAR,
    ticket_number           VARCHAR,
    doc_type                VARCHAR,
    airline                 VARCHAR,
    agency                  VARCHAR,
    user                    VARCHAR,
    fare                    DECIMAL,
    equivalent_fare         DECIMAL,
    taxes                   DECIMAL,
    other_charges           DECIMAL,
    discount_receivable     DECIMAL,
    discount_payable        DECIMAL,
    commission_receivable   DECIMAL,
    commission_payable      DECIMAL,
    wht                     DECIMAL,
    total_receivable        DECIMAL,
    total_payable           DECIMAL,
    status                  VARCHAR,
    exchange_for            VARCHAR,
    exchange_date           DATETIME,
    fare_difference         DECIMAL,
    airline_charges         DECIMAL,
    consolidator_charges    DECIMAL,
    consolidator_payable    DECIMAL,
    consolidator_receivable DECIMAL
);

CREATE TABLE IF NOT EXISTS refund_log
(
    agency_id             UUID,
    issue_date            DATETIME,
    pax_name              VARCHAR,
    pnr                   VARCHAR,
    ticket_number         VARCHAR,
    doc_type              VARCHAR,
    airline               VARCHAR,
    agency                VARCHAR,
    user                  VARCHAR,
    fare                  DECIMAL,
    equivalent_fare       DECIMAL,
    taxes                 DECIMAL,
    other_charges         DECIMAL,
    discount_receivable   DECIMAL,
    discount_payable      DECIMAL,
    commission_receivable DECIMAL,
    commission_payable    DECIMAL,
    wht                   DECIMAL,
    total_receivable      DECIMAL,
    total_payable         DECIMAL,
    status                VARCHAR,
    refund_date           DATETIME,
    fare_used             DECIMAL,
    fare_refundable       DECIMAL,
    tax_refundable        DECIMAL
);

CREATE TABLE IF NOT EXISTS void_log
(
    agency_id               UUID,
    issue_date              DATETIME,
    pax_name                VARCHAR,
    pnr                     VARCHAR,
    ticket_number           VARCHAR,
    doc_type                VARCHAR,
    airline                 VARCHAR,
    agency                  VARCHAR,
    user                    VARCHAR,
    fare                    DECIMAL,
    void_charges_receivable DECIMAL,
    void_charges_payable    DECIMAL,
    total_payable           DECIMAL,
    total_receivable        DECIMAL
);
