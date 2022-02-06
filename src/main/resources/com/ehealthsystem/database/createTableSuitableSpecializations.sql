CREATE TABLE "suitableSpecializations" (
	"problem"	INTEGER,
	"specialization"	INTEGER,
    PRIMARY KEY("problem","specialization"),
	FOREIGN KEY("problem") REFERENCES "problems"("id") ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY("specialization") REFERENCES "category"("category_id") ON UPDATE CASCADE ON DELETE CASCADE
);
