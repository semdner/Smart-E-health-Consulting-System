INSERT INTO health_status VALUES (2, 'M5450', 1);
INSERT INTO health_status VALUES (2, 'M5459', 2);

SELECT health_status.ICD, disease.disease_name, medication.medication_name
FROM ((((health_status
LEFT JOIN disease on health_status.ICD = disease.ICD)
LEFT JOIN prescription on prescription.prescription_id = health_status.prescription_id)
LEFT JOIN medication on prescription.medication_id = medication.medication_id)
LEFT JOIN user on user.user_id = health_status.user_id) WHERE user.email = 'mxprivate@protonmail.com';
