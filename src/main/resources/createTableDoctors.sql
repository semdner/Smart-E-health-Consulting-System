CREATE TABLE "doctors" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"specialization"	TEXT NOT NULL,
	"street"	TEXT NOT NULL,
	"houseNo"	INTEGER NOT NULL,
	"zipCode"	INTEGER NOT NULL,
	"latitude"	INTEGER NOT NULL,
	"longitude"	INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
