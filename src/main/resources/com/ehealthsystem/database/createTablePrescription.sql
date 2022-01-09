CREATE TABLE prescription
(
    prescription_id INTEGER,
    doctor_id       INTEGER,
    medication_id   VARCHAR(3),
    user_id         INTEGER,
    date            TEXT,
    PRIMARY KEY (prescription_id AUTOINCREMENT),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (medication_id) REFERENCES medication(medication_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);