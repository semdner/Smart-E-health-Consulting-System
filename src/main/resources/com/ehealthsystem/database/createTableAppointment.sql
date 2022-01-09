CREATE TABLE appointment
(
	doctor_id       INTEGER,
	user_id         INTEGER,
	date            TEXT NOT NULL,
	time            TEXT NOT NULL,
	health_problem  TEXT,
	PRIMARY KEY (doctor_id, user_id, date, time),
	FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
	FOREIGN KEY (user_id) REFERENCES user(user_id)
);