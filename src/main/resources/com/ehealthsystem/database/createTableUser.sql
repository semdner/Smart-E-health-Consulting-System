create table user
(
    user_id             INTEGER,
    username            TEXT NOT NULL UNIQUE,
    email               TEXT NOT NULL UNIQUE,
    first_name          TEXT NOT NULL,
    last_name           TEXT NOT NULL,
    street              TEXT NOT NULL,
    number              TEXT NOT NULL,
    zip                 INTEGER,
    birthday            TEXT NOT NULL,
    sex                 TEXT NOT NULL,
    password	        TEXT NOT NULL,
    private_insurance   BOOLEAN,
    PRIMARY KEY (user_id AUTOINCREMENT),
    FOREIGN KEY (zip) REFERENCES location(zip)
);