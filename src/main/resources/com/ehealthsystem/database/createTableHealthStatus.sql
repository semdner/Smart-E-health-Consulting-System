CREATE TABLE health_status
(
    id                  INTEGER,
    user_id             INTEGER,
    ICD                 INTEGER,
    PRIMARY KEY("id" AUTOINCREMENT),
    UNIQUE (user_id, ICD),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ICD) REFERENCES disease(ICD)
);