CREATE TABLE user
(
    user_id             INTEGER,
    username            TEXT NOT NULL UNIQUE,
    email               TEXT UNIQUE,
    first_name          TEXT,
    last_name           TEXT,
    street              TEXT,
    number              TEXT,
    zip                 TEXT,
    birthday            TEXT,
    sex                 TEXT,
    password	        TEXT NOT NULL,
    insurance_name      TEXT,
    private_insurance   BOOLEAN,
    PRIMARY KEY (user_id AUTOINCREMENT)
    -- FOREIGN KEY (zip) REFERENCES location(zip)
);