CREATE TABLE doctor
(
    doctor_id   INTEGER,
    first_name  TEXT NOT NULL,
    last_name   TEXT NOT NULL,
    street	    TEXT NOT NULL,
    number	    TEXT NOT NULL,
    zip 	    TEXT NOT NULL,
    latitude    NUMERIC NOT NULL,
    longitude   NUMERIC NOT NULL,
    PRIMARY KEY(doctor_id AUTOINCREMENT)
    -- FOREIGN KEY (zip) REFERENCES location(zip)
);
