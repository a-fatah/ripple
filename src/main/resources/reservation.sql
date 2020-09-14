
CREATE TABLE IF NOT EXISTS fare_quote(
    id UUID PRIMARY KEY,
    access_point UUID,
    contract UUID,
    contractor UUID,
    base_price DECIMAL,
    taxes DECIMAL,
    adjustment DECIMAL,
    total_amount DECIMAL,
    currency_code VARCHAR(3)
);

CREATE TABLE IF NOT EXISTS segment_ref(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    fare_quote UUID,
    segment UUID,
    connection BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS booking(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    fare_quote UUID,
    passenger_type VARCHAR,
    count INT
);

CREATE TABLE IF NOT EXISTS booking_info(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    booking LONG,
    booking_code VARCHAR(1),
    segment_ref VARCHAR,
    fare_basis VARCHAR
);

CREATE TABLE IF NOT EXISTS segment(
    id UUID PRIMARY KEY,
    reservation UUID,
    key VARCHAR,
    _group INTEGER,
    origin VARCHAR(3),
    destination VARCHAR(3),
    departure_Time VARCHAR,
    arrival_time VARCHAR,
    carrier VARCHAR(3),
    flight_number VARCHAR,
    equipment VARCHAR,
    provider_code VARCHAR(2)
);
-- TODO change datatype of date values from string to datetime
CREATE TABLE IF NOT EXISTS universal_record(
    id UUID PRIMARY KEY,
    locator_code VARCHAR, -- TODO enforce UNIQUE constraint
    version VARCHAR,
    status VARCHAR,
    cancelled BOOLEAN,
    contract UUID,
    access_point UUID
);

CREATE TABLE IF NOT EXISTS reservation(
    id UUID PRIMARY KEY,
    locator_code VARCHAR,
    provider_code VARCHAR(2),
    provider_locator_code VARCHAR,
    supplier_code VARCHAR(2),
    supplier_locator_code VARCHAR,
    created_date VARCHAR,
    modified_date VARCHAR,
    cancelled BOOLEAN
);

CREATE TABLE IF NOT EXISTS reservation_ref(
    universal_record UUID,
    reservation UUID
);

CREATE TABLE IF NOT EXISTS traveler_ref(
    universal_record UUID,
    traveler UUID
);

CREATE TABLE IF NOT EXISTS reservation_segment_ref(
    reservation UUID,
    segment UUID
);

CREATE TABLE IF NOT EXISTS traveler(
    id UUID PRIMARY KEY,
    universal_record UUID,
    key VARCHAR,
    type VARCHAR,
    prefix VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    dob VARCHAR,
    gender VARCHAR
);

CREATE TABLE phone(
    id INT AUTO_INCREMENT PRIMARY KEY,
    traveler UUID,
    country_code VARCHAR,
    city_code VARCHAR,
    area_code VARCHAR,
    phone_number VARCHAR
);

CREATE TABLE email(
    id INT AUTO_INCREMENT PRIMARY KEY,
    traveler UUID,
    type VARCHAR,
    email_id VARCHAR
);

CREATE TABLE IF NOT EXISTS contractor(
    id UUID PRIMARY KEY,
    name VARCHAR,
    active BOOLEAN
);

CREATE TABLE IF NOT EXISTS contract(
    id UUID PRIMARY KEY,
    contractor UUID,
    consolidator UUID,
    balance DECIMAL,
    state VARCHAR
);

CREATE TABLE IF NOT EXISTS access_point(contract UUID, ref UUID);

CREATE TABLE IF NOT EXISTS credentials(
    access_point UUID PRIMARY KEY,
    type VARCHAR,
    username VARCHAR,
    password VARCHAR,
    authorized_by VARCHAR,
    target_branch VARCHAR
);

CREATE TABLE IF NOT EXISTS contract_policy(contract UUID, ref UUID);

CREATE TABLE IF NOT EXISTS contractor_policy(contractor UUID, ref UUID);
