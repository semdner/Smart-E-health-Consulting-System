CREATE TABLE "appointments" (
	"id"	INTEGER,
	"user"	TEXT,
	"doctor"	INTEGER,
	"healthProblem"	INTEGER,
	"healthProblemDescription"	TEXT,
	"timestamp"	INTEGER CHECK("timestamp" >= 0),
	"minutesBeforeReminder"	INTEGER CHECK("minutesBeforeReminder" IN (10, 60, 4320, 10080)),
	"duration"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("user") REFERENCES "users"("username") ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("doctor") REFERENCES "doctors"("id") ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("healthProblem") REFERENCES "problems"("id") ON UPDATE CASCADE ON DELETE RESTRICT
);
