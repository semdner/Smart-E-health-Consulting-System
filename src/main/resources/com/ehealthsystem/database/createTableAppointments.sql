CREATE TABLE "appointments" (
	"id"	INTEGER,
	"user"	INTEGER,
	"doctor"	INTEGER,
	"healthProblem"	INTEGER,
	"healthProblemDescription"	TEXT,
	"timestamp"	INTEGER CHECK("timestamp" >= 0),
	"minutesBeforeReminder"	INTEGER CHECK("minutesBeforeReminder" IN (10, 60, 4320, 10080)),
	"duration"	INTEGER,
    PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("user") REFERENCES "users"("id") ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("doctor") REFERENCES "doctors"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
