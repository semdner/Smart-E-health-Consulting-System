CREATE TABLE appointment
(
    "id" INTEGER,
    "doctor_id"	INTEGER,
    "user"	TEXT,
    "healthProblemDescription"	TEXT,
    date        TEXT NOT NULL,
    time        TEXT NOT NULL,
    "minutesBeforeReminder"	INTEGER CHECK("minutesBeforeReminder" IN (0, 10, 60, 4320, 10080)),
    "duration" INTEGER, -- not used yet
    PRIMARY KEY("id" AUTOINCREMENT),
    UNIQUE (doctor_id, date, time),
    FOREIGN KEY("user") REFERENCES "user"("username") ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY("doctor_id") REFERENCES "doctor"("doctor_id") ON UPDATE CASCADE ON DELETE CASCADE
);