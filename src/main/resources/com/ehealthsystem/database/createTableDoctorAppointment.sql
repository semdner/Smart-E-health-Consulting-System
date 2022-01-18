CREATE TABLE doctor_appointment
(
    doctor_id   INTEGER,
    date        TEXT NOT NULL,
    time        TEXT NOT NULL,
    free        BOOLEAN,
    PRIMARY KEY (doctor_id, date, time),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);