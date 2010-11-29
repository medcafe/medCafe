
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
mgreer	User
jpiescik User
\.

COPY users (username, password, emailaddress) FROM stdin;
jchoyt	a94a8fe5ccb19ba61c4c0873d391e987982fbbd3	jchoyt@mitre.org
mgreer	a94a8fe5ccb19ba61c4c0873d391e987982fbbd3	mgreer@mitre.org
jpiescik	a94a8fe5ccb19ba61c4c0873d391e987982fbbd3	jpiescik@mitre.org
\.

insert into user_roles select username, 'User' from users;

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
   	repository character varying(250) NULL,
   	photo character varying(100) NULL

);

CREATE TABLE patient_repository_assoc (
	patient_id  integer NOT NULL,
    rep_patient_id integer NOT NULL,
   	repository character varying(250) NULL,
   	PRIMARY KEY (patient_id, rep_patient_id,repository )

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
	x_origin integer NULL,
	y_origin integer NULL,
	shape_x float NULL,
	shape_y float NULL,
	width float NULL,
	height float NULL,
	zoom integer NULL,
	color character varying(50) NULL,
	shape_type character varying(50) NOT NULL,
	note text NOT NULL
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


CREATE TABLE address
(
	id SERIAL PRIMARY KEY,
	patient_id integer NOT NULL,
	street character varying(500) NOT NULL,
	street2 character varying(500) NULL,
	city character varying(300) NOT NULL,
	state character varying(20) NOT NULL,
	zip character varying(10) NOT NULL,
	country character varying (200) NOT NULL default 'USA'

);

CREATE TABLE template (
	template_id SERIAL PRIMARY KEY,
    name character varying(200) NOT NULL,
    creator character varying(50) NOT NULL,
    description text NULL,
    date_created date NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE template_widget_params (
	id SERIAL PRIMARY KEY,
	widget_id integer NOT NULL,
    template_id integer NOT NULL,
    patient_id integer NOT NULL,
    username character varying(50) NOT NULL,
    param character varying(50) NOT NULL,
   	value character varying(500) NOT NULL
);

ALTER TABLE ONLY template_widget_params
    ADD CONSTRAINT template_widget_params_FK FOREIGN KEY (template_id) REFERENCES TEMPLATE (template_id) MATCH FULL;

CREATE TABLE category (
	id SERIAL PRIMARY KEY,
	category character varying(500) NOT NULL,
    description character varying(1000) NOT NULL
);
insert into priority (id, priority, description) values (1, 'Very High', 'Very High Priority -immediate attention.');
insert into priority (id, priority, description) values (2, 'High', 'High Priority - action required in short term');
insert into priority (id, priority, description) values (3, 'Medium', 'Medium Priority - ongoing monitoring recommended');
insert into priority (id, priority, description) values (4, 'Low', 'Low Priority - periodic monitoring recommended');
insert into priority (id, priority, description) values (5, 'Very Low', 'Very Low Priority - no action required');


insert into history_category (category, title, description) values ('NONE', 'No category', 'Issues that are not categorized');
insert into history_category (category, title, description) values ('Personal', 'Past Medical History', 'List of major medical issues noted by physician');
insert into history_category (category, title, description) values ('Family', 'Family/ Social History', 'List of known issues with family history');

--
-- Data for Name: patient; Type: TABLE DATA; Schema: public;
--

INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (1, 3, 'CLINICAL F', 'PATIENT', 'OurVista', 'elizabeth.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (2, 2, 'DIETARY', 'PATIENT', 'OurVista', 'monalisa.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (3, 1, 'ONE', 'ZZTEST', 'OurVista', 'relee.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (4, 7, 'PHARMACY', 'PATIENT', 'OurVista', 'rosie.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (5, 6, 'LABORATORY', 'PATIENT', 'OurVista', 'JohnMuir.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (6, 5, 'RADIOLOGY', 'PATIENT', 'OurVista', 'lincoln.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (7, 4, 'CLINICAL M', 'PATIENT', 'OurVista', 'sherman.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (8, 8, 'PEDIATRIC', 'PATIENT', 'OurVista', 'girl-silhouette.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (9, 9, 'JOHN Q', 'SMITH', 'OurVista', 'elvis.jpg');
INSERT INTO patient (id, rep_patient_id, first_name, last_name, repository, photo) VALUES (10, 10, 'BARNABY QUINTIN', 'JONES', 'OurVista', 'gandhi.jpg');


insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (8,8,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (4,7,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (5,6,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (6,5,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (7,4,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (1,3,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (2,2,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (3,1,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (9,9,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (10,10,'OurVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (8,8,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (4,7,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (5,6,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (6,5,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (7,4,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (1,3,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (2,2,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (3,1,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (9,9,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (10,10,'JeffVista');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (1,1,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (2,2,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (3,3,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (4,4,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (5,5,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (6,6,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (7,7,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (8,8,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (9,9,'local');
insert into patient_repository_assoc (patient_id, rep_patient_id,repository) values (10,10,'local');


INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'gaily', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'gaily', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'gaily', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'gaily', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'rep_patient_id', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'rep_patient_id', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'rep_patient_id', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 6, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'type', 'EditorNonIFrame');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'name', 'Editor');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'mgreer', 'clickUrl', 'editor.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 2, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 2, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 2, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES ( 2, 3, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 3, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 3, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 3, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 3, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 3, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 4, 'mgreer', 'order', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 4, 'mgreer', 'name', 'Misc');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 4, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'order', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 4, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'order', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'rep_patient_id', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 4, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 8, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 8, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 8, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 10, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 5, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 5, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 5, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 5, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'order', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 5, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 8, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'order', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 8, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'order', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 8, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'rep_patient_id', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 2, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 2, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 8, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 8, 'jpiescik', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'type', 'Immunizations');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'rep_patient_id', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'name', 'Immunizations');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 8, 'jpiescik', 'clickUrl', 'immunizationJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 10, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'rep_patient_id', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 10, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'rep_patient_id', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 10, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 5, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 5, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 5, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 4, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'type', 'Support');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'name', 'SupportInfo');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 4, 'jpiescik', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 4, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 4, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'jpiescik', 'name', 'New');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'rep_patient_id', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'jpiescik', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'type', 'Immunizations');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'rep_patient_id', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'name', 'Immunizations');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'jpiescik', 'clickUrl', 'immunizationJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 3, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 3, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 3, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 6, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'rep_patient_id', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 6, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 6, 'jpiescik', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'jpiescik', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 7, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 7, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 7, 'jpiescik', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'rep_patient_id', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 7, 'jpiescik', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 1, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 1, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 1, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'type', 'Image');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'name', 'Images');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 1, 'mgreer', 'clickUrl', 'contentFlow.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'order', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'mgreer', 'order', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (1, 9, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'order', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (2, 9, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'order', '4');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (4, 9, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'order', '3');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (3, 9, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'type', 'Chart');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'order', '6');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'name', 'Charts');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 9, 'mgreer', 'clickUrl', 'chart.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 9, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 9, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 9, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'order', '8');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (8, 9, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'order', '7');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (7, 9, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'order', '10');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (10, 9, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'order', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (9, 9, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (6, 1, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'order', '5');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'column', '2');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (widget_id, patient_id, username, param, value) VALUES (5, 1, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');


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

--
-- Data for Name: medical_history; Type: TABLE DATA;
--

INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (1, 7, 'Hypertension', 2, '2009-06-06', 'Noted increase in hypertension', '2010-09-15', 3);
INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (2, 7, 'Diabetes, Type 2', 2, '2001-03-18', 'Diagnosed with type 2 diabetes', '2010-09-15', 3);
INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (3, 7, 'Migraine', 2, '2000-03-18', 'Experiences migraines every 6 months', '2010-09-15', 3);
INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (4, 7, 'Heart Disease', 3, '2010-05-11', 'Father had heart disease age 60', '2010-09-15', 3);
INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (5, 7, 'Colon Cancer', 3, '2010-05-11', 'Father had colon cancer age 54', '2010-09-15', 3);
INSERT INTO medical_history (id, patient_id, history, category_id, history_date, history_notes, date_accessed, priority) VALUES (6, 7, 'Smoking', 3, '2010-05-11', 'Mother was smoker', '2010-09-15', 3);


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


--
-- Data for Name: file_annotations; Type: TABLE DATA;
--

INSERT INTO file_annotations (id, patient_id, username, file_id, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type, note) VALUES (362, 7, 'mgreer', 6, 193, 0, 311.5, 114.50149, 41.309806999999999, 0, 81, 'rgb(0, 255, 0)', 'circle', 'undefined');
INSERT INTO file_annotations (id, patient_id, username, file_id, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type, note) VALUES (363, 7, 'mgreer', 6, 193, 0, 274, 94, 74, 38, 81, 'rgb(0, 255, 0)', 'rectangle', 'undefined');
INSERT INTO file_annotations (id, patient_id, username, file_id, x_origin, y_origin, shape_x, shape_y, width, height, zoom, color, shape_type, note) VALUES (364, 7, 'mgreer', 6, 193, 0, 255, 63, 30, 35, 81, 'rgb(30, 0, 255)', 'rectangle', 'undefined');



--
-- Data for Name: link; Type: TABLE DATA; Schema: public;
--

INSERT INTO link (id, username, title, url, description) VALUES (1, 'gaily', 'PubMed', 'http://www.ncbi.nlm.nih.gov/pubmed/', '');
INSERT INTO link (id, username, title, url, description) VALUES (2, 'gaily', 'Email', 'https://imc.mitre.org', '');
INSERT INTO link (id, username, title, url, description) VALUES (3, 'gaily', 'Journal of Medical Internet Research', 'http://www.jmir.org/', '');
INSERT INTO link (id, username, title, url, description) VALUES (4, 'mgreer', 'PubMed', 'http://www.ncbi.nlm.nih.gov/pubmed/', '');
INSERT INTO link (id, username, title, url, description) VALUES (6, 'mgreer', 'Journal of Medical Internet Research', 'http://www.jmir.org/', '');
INSERT INTO link (id, username, title, url, description) VALUES (5, 'jpiescik', 'PubMed', 'http://www.ncbi.nlm.nih.gov/pubmed/', '');
INSERT INTO link (id, username, title, url, description) VALUES (7, 'jpiescik', 'Journal of Medical Internet Research', 'http://www.jmir.org/', '');



--set some randomly to SMOKING category
insert into file_category (select 2, min(id) from file group by patient_id);

insert into physical_category (id , category,description ) values (1, 'General','General Category');
insert into physical_category (id , category,description ) values (2, 'Gastrointestinal','Gastrointestinal Category');
insert into physical_category (id , category,description ) values (3, 'Cardiovascular','Cardiovascular Category');
insert into physical_category (id , category,description ) values (4, 'Pulmonary/lungs','Pulmonary/lungs Category');
insert into physical_category (id , category,description ) values (5, 'Neurologic','Neurologic Category');
insert into physical_category (id , category,description ) values (6, 'Endocrine','Endocrine Category');
insert into physical_category (id , category,description ) values (7, 'Eyes, ears, nose, throat','Eyes, ears, nose, throat Category');

INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (1, 1, 'Weight Change', 'weight gain/loss of  10+ lbs during last 6 months');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (2, 1, 'Sleep Disturbance', 'Poor sleep patterns');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (3, 1, 'Fever', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (4, 1, 'Headache', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (5, 1, 'Depression', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (6, 2, 'Poor Appetite', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (7, 2, 'Indigestion', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (8, 2, 'Diarrhea', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (9, 2, 'Constipation', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (10, 2, 'Nausea or Vomiting', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (11, 3, 'Chest Pain', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (12, 3, 'Angina', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (13, 3, 'High Blood Pressure', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (14, 3, 'Irregular Heart Beat', '');
INSERT INTO symptom_list (id, physical_category, symptom, description) VALUES (15, 3, 'Poor Circulation', '');


--
-- Data for Name: patient_symptom_list; Type: TABLE DATA; Schema: public;
--

INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (1, 7, 1, '2010-09-15', 'Lost 10lbs in 3 months');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (2, 7, 2, '2010-09-15', 'Problems sleeping');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (3, 7, 4, '2010-09-15', 'Headaches - weekly - sinus');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (4, 7, 2, '2010-09-15', 'Recent loss of appetite screen for depression');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (8, 9, 11, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (9, 9, 10, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (10, 9, 6, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (11, 8, 10, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (12, 8, 2, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (13, 4, 11, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (14, 4, 15, '2010-09-15', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (19, 1, 8, '2010-09-22', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (20, 1, 7, '2010-09-22', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (21, 1, 1, '2010-09-22', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (22, 1, 5, '2010-09-22', '');
INSERT INTO patient_symptom_list (id, patient_id, symptom_id, start_date, note) VALUES (23, 1, 2, '2010-09-22', '');
--
-- Data for Name: recent_patients; Type: TABLE DATA; Schema: public;
--

INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (5, 6, 'mgreer', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (8, 5, 'mgreer', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (6, 2, 'mgreer', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (10, 3, 'mgreer', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (11, 2, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (12, 8, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (13, 10, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (14, 5, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (15, 4, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (17, 3, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (18, 9, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (19, 6, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (20, 7, 'jpiescik', '2010-09-15');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (1, 10, 'mgreer', '2010-09-16');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (2, 9, 'mgreer', '2010-09-17');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (7, 8, 'mgreer', '2010-09-22');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (16, 1, 'jpiescik', '2010-09-17');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (4, 4, 'mgreer', '2010-09-21');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (3, 1, 'mgreer', '2010-09-23');
INSERT INTO recent_patients (id, patient_id, username, date_accessed) VALUES (9, 7, 'mgreer', '2010-09-27');


--
-- Data for Name: user_bookmark; Type: TABLE DATA; Schema: public;
--

INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (1, 'mgreer', '1', 'Arthritis Foundation', 'Journal Summaries', 'http://www.arthritis.org/journal-summaries.php', 'Journal summaries of arthritis research');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (2, 'mgreer', '2', 'Tobacco Cessation', 'Surgeon General Site on Smoking Cessation', 'http://www.surgeongeneral.gov/tobacco/', 'Good resource for smoking cessation');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (3, 'mgreer', '3', 'National MS Society', 'MS Society Website', 'http://www.nationalmssociety.org', 'Support groups');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (4, 'mgreer', '4', 'CDC H1N1', 'Site about H1N1 virus', 'http://www.cdc.gov/h1n1flu/', 'What to do to stay healthy');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (5, 'mgreer', '5', 'Disease Outbreak Alerts', 'Information for travelers', 'http://wwwnc.cdc.gov/travel', 'Info for travelers');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (6, 'mgreer', '6', 'Vaccine Home Page', 'Information about vaccines and immunizations', 'http://www.cdc.gov/vaccines', 'Talk with patient about importance of vaccinations');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (7, 'mgreer', '7', 'American Diabetes Association', 'Homepage for ADA', 'http://www.diabetes.org/', 'Resource for patient');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (8, 'mgreer', '8', 'Mayo Clinic', 'Diabetes Information from the Mayo Clinic', 'http://www.mayoclinic.com/health/diabetes/DS01121', 'Good site about diabetes');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (9, 'mgreer', '9', 'Alcoholics Anonymous', 'Main page for AA', 'http://www.aa.org', 'Discuss benefits of AA with patient');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (10, 'mgreer', '10', 'American Heart Association', 'Main page for AHA', 'http://www.americanheart.org', 'Explains risk factors');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (11, 'jpiescik', '1', 'American Lung Association', 'Main page for American Lung Association', 'http://www.lungusa.org', 'Links for lung cancer and smoking cessation');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (12, 'jpiescik', '2', 'Mayo Clinic Asthma Information', 'Information about asthma', 'http://www.mayoclinic.com/health/asthma/DS00021', 'In-depth asthma information');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (13, 'jpiescik', '3', 'National Kidney Foundation', 'Information about kidney and urinary tract disease', 'http://www.kidney.org', 'General information');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (14, 'jpiescik', '4', 'NIH Site', 'Information about colonoscopy', 'http://digestive.niddk.nih.gov/ddiseases/pubs/colonoscopy/', 'Good information about benefits of colonoscopy');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (15, 'jpiescik', '5', 'Mayo Clinic Cancer Information', 'General information about cancer', 'http://www.mayoclinic.com/health/cancer/DS01076', 'Good general information');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (16, 'jpiescik', '6', 'CDC Healthy Living', 'Ways to improve your general health', 'http://www.cdc.gov/healthyliving/', 'Good information');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (17, 'jpiescik', '7', 'Mayo Healthy Lifestyle', 'Ways to improve your general health', 'http://www.mayoclinic.com/health/HealthyLivingIndex/HealthyLivingIndex', 'Good information');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (18, 'jpiescik', '8', 'Child Development Institute', 'General information on child development', 'http://childdevelopmentinfo.com', 'Talk with mother about this');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (19, 'jpiescik', '9', 'Discovery Health', 'Explanation of MRIs', 'http://health.howstuffworks.com/medicine/tests-treatment/mri.htm', 'MRIs are not scary!');
INSERT INTO user_bookmark (user_bookmark_id, username, patientid, name, description, url, note) VALUES (20, 'jpiescik', '10', 'Glaucoma Research Foundation', 'New research on glaucoma', 'http://www.glaucoma.org', 'Good info');


