CREATE TABLE IF NOT EXISTS insurance_case (
    id int GENERATED ALWAYS AS IDENTITY,
    insurance_case_number varchar(255) not null,
    license_plate varchar(255),
    insurance_tier integer,
    is_minor_severity boolean,
    is_audited boolean,
    driver varchar(255));

-- CREATE SEQUENCE IF NOT EXISTS insurance_case_seq INCREMENT 50 START 100;