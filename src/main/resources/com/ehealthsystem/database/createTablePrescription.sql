CREATE TABLE prescription
(
    prescription_id INTEGER,
    health_status   INTEGER,
    doctor_id       INTEGER,
    medication_id   VARCHAR(3),
    date            TEXT,
    PRIMARY KEY (prescription_id AUTOINCREMENT),
    FOREIGN KEY (health_status) REFERENCES health_status(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (medication_id) REFERENCES medication(medication_id)
);