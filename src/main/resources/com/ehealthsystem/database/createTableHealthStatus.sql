CREATE TABLE health_status
(
    user_id             INTEGER,
    ICD                 INTEGER,
    prescription_id     INTEGER,
    PRIMARY KEY (user_id, ICD),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (ICD) REFERENCES disease(ICD),
    FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id)
);