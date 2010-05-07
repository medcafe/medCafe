
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
	da39a3ee5e6b4b0d3255bfef95601890afd80709
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

