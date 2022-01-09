create table location
(
    location_id     INTEGER,
    zip             INTEGER,
    city            TEXT NOT NULL,
    PRIMARY KEY (location_id AUTOINCREMENT)
);