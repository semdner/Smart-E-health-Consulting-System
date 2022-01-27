CREATE TABLE "appointment"
(
    "id" INTEGER,
    "doctor_id"	INTEGER,
    "user"	TEXT,
    "healthProblemDescription"	TEXT,
    "timestamp"	INTEGER CHECK("timestamp" >= 0),
    "minutesBeforeReminder"	INTEGER CHECK("minutesBeforeReminder" IN (10, 60, 4320, 10080)),
    PRIMARY KEY("id" AUTOINCREMENT),
    UNIQUE (doctor_id, user, timestamp),
    FOREIGN KEY("user") REFERENCES "user"("username") ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY("doctor_id") REFERENCES "doctor"("doctor_id") ON UPDATE CASCADE ON DELETE CASCADE
);