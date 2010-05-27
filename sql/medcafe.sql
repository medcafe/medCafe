
CREATE TABLE roles (
    rolename character varying(20) NOT NULL
);

CREATE TABLE user_roles (
    username character varying(20) NOT NULL,
    rolename character varying(20) NOT NULL
);

CREATE TABLE users (
    username character varying(50) NOT NULL,
    password character varying(100),
    emailaddress text
);

CREATE RULE enable_user AS ON INSERT TO users DO INSERT INTO user_roles (username, rolename) VALUES (new.username, 'User'::character varying);

COPY roles (rolename) FROM stdin;
Admin
User
manager
\.

COPY user_roles (username, rolename) FROM stdin;
jchoyt	User
jchoyt	Admin
jchoyt	manager
\.

COPY users (username, password, emailaddress) FROM stdin;
jchoyt	a94a8fe5ccb19ba61c4c0873d391e987982fbbd3	jchoyt@mitre.org
\.


ALTER TABLE ONLY users
    ADD CONSTRAINT player_pkey PRIMARY KEY (username);

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (rolename);

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (username, rolename);

CREATE TABLE user_text (
	user_text_id SERIAL PRIMARY KEY,
    username character varying(50) NOT NULL,
   	patient_id integer NOT NULL,
   	subject character varying(250) NULL,
   	note text NULL,
   	note_added date NOT NULL DEFAULT CURRENT_DATE

);


CREATE TABLE user_bookmark (
	user_bookmark_id SERIAL PRIMARY KEY,
    username character varying(50) NOT NULL,
   	patientId character varying(50) NOT NULL,
   	name character varying(250) NULL,
   	description character varying(250) NULL,
   	url character varying(500) NULL,
   	note text NULL
);

CREATE TABLE text_templates (
	user_template_id SERIAL PRIMARY KEY,
    username character varying(50) NOT NULL,
   	subject character varying(250) NULL,
   	template text NULL

);

CREATE TABLE patient (
	id SERIAL PRIMARY KEY,
    rep_patient_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
   	repository character varying(250) NULL

);


CREATE TABLE patient_user_assoc (
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	username character varying(50) NOT NULL,
	role character varying(250) NULL
);

CREATE TABLE schedule (
	id SERIAL PRIMARY KEY,
    patient_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    username character varying(50) NOT NULL,
   	appoint_date date NULL,
   	appoint_time time NULL,
   	title character varying(50) NOT NULL DEFAULT 'Appointment',
   	end_time time NULL
);

CREATE TABLE widget_params (
	id SERIAL PRIMARY KEY,
	widget_id integer NOT NULL,
    patient_id integer NOT NULL,
    username character varying(50) NOT NULL,
    param character varying(50) NOT NULL,
   	value character varying(500) NOT NULL
);

CREATE TABLE category (
	id SERIAL PRIMARY KEY,
	category character varying(500) NOT NULL,
    description character varying(1000) NOT NULL
);

CREATE TABLE file (
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	username character varying(50) NOT NULL,
	filename character varying(500) NOT NULL,
	thumbnail character varying(500) NOT NULL,
	title character varying(100) NULL,
	file_date date NULL
);

CREATE TABLE file_category (
	category_id integer NOT NULL,
	file_id  integer NOT NULL,
	notes character varying(500) NULL
);

CREATE TABLE file_annotations
(
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	username character varying(50) NOT NULL,
	file_id  integer NOT NULL,
	x_1 integer NULL,
	y_1 integer NULL,
	x_2 integer NULL,
	y_2 integer NULL,
	note character varying(500) NOT NULL
);

CREATE TABLE recent_patients
(
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	username character varying(50) NOT NULL,
	date_accessed date NOT NULL DEFAULT CURRENT_DATE
);
ALTER TABLE ONLY file_category
    ADD CONSTRAINT file_category_pkey PRIMARY KEY (username, rolename);

CREATE TABLE symptom_list
(
	id SERIAL PRIMARY KEY,
	physical_category integer NOT NULL,
	symptom character varying(200) NOT NULL,
	description text NULL
);

CREATE TABLE patient_symptom_list
(
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	symptom_id integer NOT NULL,
	start_date  date NOT NULL DEFAULT CURRENT_DATE,
	note text NULL
);


CREATE TABLE physical_category
(
	id integer PRIMARY KEY NOT NULL,
	category character varying(200) NOT NULL,
	description text null
);

CREATE TABLE medical_history
(
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	history character varying(200) NOT NULL,
	category_id integer NOT NULL DEFAULT 2,
	history_date date NOT NULL DEFAULT CURRENT_DATE,
	history_notes text NULL,
	date_accessed date NOT NULL DEFAULT CURRENT_DATE,
	priority integer NOT NULL DEFAULT 3
);

CREATE TABLE priority
(
	id integer NOT NULL PRIMARY KEY,
	priority character varying(100) NOT NULL,
	description character varying(500) NOT NULL,
	color character varying(100) NULL
);

CREATE TABLE history_category
(
	id SERIAL PRIMARY KEY,
	category character varying(50) NOT NULL,
	title character varying(200) NOT NULL,
	description character varying(500) NOT NULL
);

CREATE TABLE link
(
	id SERIAL PRIMARY KEY,
	username character varying(50) NOT NULL,
	title character varying(100) NOT NULL,
	url character varying(300) NOT NULL,
	description character varying(500) NULL
);

insert into priority (id, priority, description) values (1, 'Very High', 'Very High Priority -immediate attention.');
insert into priority (id, priority, description) values (2, 'High', 'High Priority - action required in short term');
insert into priority (id, priority, description) values (3, 'Medium', 'Medium Priority - ongoing monitoring recommended');
insert into priority (id, priority, description) values (4, 'Low', 'Low Priority - periodic monitoring recommended');
insert into priority (id, priority, description) values (5, 'Very Low', 'Very Low Priority - no action required');


insert into history_category (category, title, description) values ('NONE', 'No category', 'Issues that are not categorized');
insert into history_category (category, title, description) values ('Personal', 'Past Medical History', 'List of major medical issues noted by physician');
insert into history_category (category, title, description) values ('Family', 'Family/ Social History', 'List of known issues with family history');

insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '3', 'PATIENT','CLINICAL F','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '2', 'PATIENT','DIETARY','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '1', 'ZZTEST','ONE','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '7', 'PATIENT','PHARMACY','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '6', 'PATIENT','LABORATORY','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '5', 'PATIENT','RADIOLOGY','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '4', 'PATIENT','CLINICAL M','OurVista');
insert into  patient (rep_patient_id, first_name, last_name, repository) values ( '8', 'PATIENT','PEDIATRIC','OurVista');

insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'tab_order', 1);
insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'type', 'Allergies');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'repository','OurVista');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'location', 'center');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'name', 'Allergies-Patient-7');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '1', '7', 'gaily', 'server', 'http://127.0.0.1:8080/medcafe/allergyJSON.jsp');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'tab_order', 2);
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'type', 'Medications');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'repository','OurVista');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'location', 'center');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'name', 'Medications-Patient-7');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '2', '7', 'gaily', 'server', 'http://127.0.0.1:8080/medcafe/prescriptionJSON.jsp');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'tab_order', 3);
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'type', 'Bookmarks');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'repository','OurVista');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'location', 'center');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'name', 'Bookmarks-Patient-7');
insert into widget_params(widget_id, patient_id, username, param, value) values ( '3', '7', 'gaily', 'server', 'http://127.0.0.1:8080/medcafe/bookmarksJSON.jsp');

insert into category (category, description)  values ('NONE','No category specified');
insert into category (category, description)  values ('Smoker','Is active smoker');
insert into file_category (category_id, file_id, notes)  values (2,4,'' );
insert into file_category (category_id, file_id, notes)  values (2,5,'' );

insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'assessment.png', 'assessment_thumb.png', 'Assessment', '01/01/2008');
insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'bloodstat.jpg', 'bloodstat_thumb.png', 'Blood Stats', '02/03/2008');
insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'cardioReport.gif', 'cardioReport_thumb.png', 'Cardio Report', '05/07/2008');
insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'chest-xray.jpg', 'chest-xray_thumb.png', 'Chest XRay', '06/08/2008');
insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'chest-xray2.jpg', 'chest-xray2_thumb.png', 'Chest XRay2', '07/08/2008');
insert into  file(patient_id, username, filename, thumbnail, title, file_date) values ( 7, 'gaily', 'mri.jpg', 'mri_thumb.png', 'MRI', '10/01/2008');

insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Hypertension', 2, '2009-06-06','Noted increase in hypertension');
insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Diabetes, Type 2', 2, '2001-03-18','Diagnosed with type 2 diabetes');
insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Migraine', 2, '2000-03-18','Experiences migraines every 6 months');

insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Heart Disease', 3, '2010-05-11','Father had heart disease age 60');
insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Colon Cancer', 3, '2010-05-11','Father had colon cancer age 54');
insert into medical_history (patient_id, history, category_id, history_date, history_notes) values (7, 'Smoking', 3, '2010-05-11','Mother was smoker');

insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 3, '353_SMOKER.pdf', '353_SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 3, '371_NON-SMOKER.pdf', '371_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '176_NON-SMOKER.pdf', '176_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '177_NON-SMOKER.pdf', '177_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '111_NON-SMOKER.pdf', '111_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '135_NON-SMOKER.pdf', '135_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '136_UNKNOWN.pdf', '136_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 1, '185_NON-SMOKER.pdf', '185_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 5, '502_UNKNOWN.pdf', '502_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 5, '500_UNKNOWN.pdf', '500_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 5, '56_SMOKER.pdf', '56_SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 5, '518_NON-SMOKER.pdf', '518_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '852_UNKNOWN.pdf', '852_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '803_UNKNOWN.pdf', '803_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '871_UNKNOWN.pdf', '871_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '819_UNKNOWN.pdf', '819_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '878_UNKNOWN.pdf', '878_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '886_UNKNOWN.pdf', '886_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '891_UNKNOWN.pdf', '891_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '880_UNKNOWN.pdf', '880_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '844_UNKNOWN.pdf', '844_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '817_UNKNOWN.pdf', '817_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '838_NON-SMOKER.pdf', '838_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '822_UNKNOWN.pdf', '822_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '832_UNKNOWN.pdf', '832_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '885_UNKNOWN.pdf', '885_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '893_UNKNOWN.pdf', '893_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '859_UNKNOWN.pdf', '859_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '848_UNKNOWN.pdf', '848_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '206_NON-SMOKER.pdf', '206_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '829_NON-SMOKER.pdf', '829_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '298_UNKNOWN.pdf', '298_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 2, '826_UNKNOWN.pdf', '826_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '705_UNKNOWN.pdf', '705_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '712_UNKNOWN.pdf', '712_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '767_UNKNOWN.pdf', '767_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '756_UNKNOWN.pdf', '756_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '773_UNKNOWN.pdf', '773_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '760_NON-SMOKER.pdf', '760_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '746_UNKNOWN.pdf', '746_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '790_NON-SMOKER.pdf', '790_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '739_UNKNOWN.pdf', '739_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '782_UNKNOWN.pdf', '782_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '722_UNKNOWN.pdf', '722_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '721_NON-SMOKER.pdf', '721_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '728_UNKNOWN.pdf', '728_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 7, '787_UNKNOWN.pdf', '787_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '683_UNKNOWN.pdf', '683_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '674_UNKNOWN.pdf', '674_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '650_UNKNOWN.pdf', '650_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '698_UNKNOWN.pdf', '698_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '644_UNKNOWN.pdf', '644_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '691_NON-SMOKER.pdf', '691_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '663_NON-SMOKER.pdf', '663_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '659_UNKNOWN.pdf', '659_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 6, '665_UNKNOWN.pdf', '665_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '457_UNKNOWN.pdf', '457_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '455_UNKNOWN.pdf', '455_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '423_UNKNOWN.pdf', '423_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '413_UNKNOWN.pdf', '413_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '432_UNKNOWN.pdf', '432_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '425_UNKNOWN.pdf', '425_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '456_UNKNOWN.pdf', '456_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '410_UNKNOWN.pdf', '410_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '433_UNKNOWN.pdf', '433_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '488_UNKNOWN.pdf', '488_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '487_UNKNOWN.pdf', '487_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '466_UNKNOWN.pdf', '466_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '443_UNKNOWN.pdf', '443_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '486_UNKNOWN.pdf', '486_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '473_UNKNOWN.pdf', '473_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '427_SMOKER.pdf', '427_SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '479_UNKNOWN.pdf', '479_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '442_UNKNOWN.pdf', '442_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '492_UNKNOWN.pdf', '492_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '467_UNKNOWN.pdf', '467_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '493_UNKNOWN.pdf', '493_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '480_UNKNOWN.pdf', '480_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '474_UNKNOWN.pdf', '474_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '435_NON-SMOKER.pdf', '435_NON-SMOKER_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '476_UNKNOWN.pdf', '476_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');
insert into file (patient_id, filename, thumbnail, username, title, file_date) values ( 4, '484_UNKNOWN.pdf', '484_UNKNOWN_thumb.png', 'jchoyt', 'Discharge Summary', '2010-05-14');


insert into link (username, title, url, description) values ( 'gaily', 'PubMed', 'http://www.ncbi.nlm.nih.gov/pubmed/','');
insert into link (username, title, url, description) values ( 'gaily', 'Email', 'https://imc.mitre.org','');
insert into link (username, title, url, description) values ( 'gaily', 'Journal of Medical Internet Research', 'http://www.jmir.org/','');


--set some randomly to SMOKING category
insert into file_category (select 2, min(id) from file group by patient_id);

insert into physical_category (id , category,description ) values (1, 'General','General Category');
insert into physical_category (id , category,description ) values (2, 'Gastrointestinal','Gastrointestinal Category');
insert into physical_category (id , category,description ) values (3, 'Cardiovascular','Cardiovascular Category');
insert into physical_category (id , category,description ) values (4, 'Pulmonary/lungs','Pulmonary/lungs Category');
insert into physical_category (id , category,description ) values (5, 'Neurologic','Neurologic Category');
insert into physical_category (id , category,description ) values (6, 'Endocrine','Endocrine Category');
insert into physical_category (id , category,description ) values (7, 'Eyes, ears, nose, throat','Eyes, ears, nose, throat Category');

insert into symptom_list (physical_category, symptom, description) values (1, 'Weight Change','weight gain/loss of  10+ lbs during last 6 months');
insert into symptom_list (physical_category, symptom, description) values (1, 'Sleep Disturbance','Poor sleep patterns');
insert into symptom_list (physical_category, symptom, description) values (1, 'Fever','');
insert into symptom_list (physical_category, symptom, description) values (1, 'Headache','');
insert into symptom_list (physical_category, symptom, description) values (1, 'Depression','');
insert into symptom_list (physical_category, symptom, description) values (2, 'Poor Appetite','');
insert into symptom_list (physical_category, symptom, description) values (2, 'Indigestion','');
insert into symptom_list (physical_category, symptom, description) values (2, 'Diarrhea','');
insert into symptom_list (physical_category, symptom, description) values (2, 'Constipation','');
insert into symptom_list (physical_category, symptom, description) values (2, 'Nausea or Vomiting','');
insert into symptom_list (physical_category, symptom, description) values (3, 'Chest Pain','');
insert into symptom_list (physical_category, symptom, description) values (3, 'Angina','');
insert into symptom_list (physical_category, symptom, description) values (3, 'High Blood Pressure','');
insert into symptom_list (physical_category, symptom, description) values (3, 'Irregular Heart Beat','');
insert into symptom_list (physical_category, symptom, description) values (3, 'Poor Circulation','');

insert into patient_symptom_list (patient_id, symptom_id, note) values (7, 1,'Lost 10lbs in 3 months');
insert into patient_symptom_list (patient_id, symptom_id, note) values (7, 2,'Problems sleeping');
insert into patient_symptom_list (patient_id, symptom_id, note) values (7, 4,'Headaches - weekly - sinus');
insert into patient_symptom_list (patient_id, symptom_id, note) values (7, 2,'Recent loss of appetite screen for depression');
