ALTER TABLE Treatment DROP CONSTRAINT FK_Treatment_patient_fk
ALTER TABLE Treatment DROP CONSTRAINT FK_Treatment_provider_fk
ALTER TABLE DRUGTREATMENT DROP CONSTRAINT FK_DRUGTREATMENT_PHYSICIAN_ID
ALTER TABLE DRUGTREATMENT DROP CONSTRAINT FK_DRUGTREATMENT_ID
ALTER TABLE RADIOLOGYTREATMENT DROP CONSTRAINT FK_RADIOLOGYTREATMENT_ID
ALTER TABLE RADIOLOGYTREATMENT DROP CONSTRAINT FK_RADIOLOGYTREATMENT_RADIOLOGIST_ID
ALTER TABLE SURGERYTREATMENT DROP CONSTRAINT FK_SURGERYTREATMENT_SURGEON_ID
ALTER TABLE SURGERYTREATMENT DROP CONSTRAINT FK_SURGERYTREATMENT_ID
DROP TABLE Treatment CASCADE
DROP TABLE DRUGTREATMENT CASCADE
DROP TABLE Patient CASCADE
DROP TABLE PROVIDER CASCADE
DROP TABLE RADIOLOGYTREATMENT CASCADE
DROP TABLE SURGERYTREATMENT CASCADE
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'
