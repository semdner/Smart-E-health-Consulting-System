CREATE TABLE doctor_category
(
    doctor_id   INTEGER,
    category_id INTEGER,
    PRIMARY KEY (doctor_id, category_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);