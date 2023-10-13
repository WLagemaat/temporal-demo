CREATE TABLE IF NOT EXISTS transgression (
    id int GENERATED ALWAYS AS IDENTITY,
    transgression_number varchar(255) not null,
    license_plate varchar(255),
    is_mulder boolean,
    is_prosecuted boolean,
    person_concerned varchar(255));

-- CREATE SEQUENCE IF NOT EXISTS transgression_seq INCREMENT 50 START 100;