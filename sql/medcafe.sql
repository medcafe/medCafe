
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


INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (5, 1, 7, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (6, 1, 7, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (7, 1, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (8, 1, 7, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (9, 2, 7, 'gaily', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (10, 2, 7, 'gaily', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (11, 2, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (12, 2, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (13, 2, 7, 'gaily', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (14, 2, 7, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (15, 2, 7, 'gaily', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (16, 2, 7, 'gaily', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (17, 2, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (18, 2, 7, 'gaily', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (19, 3, 7, 'gaily', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (20, 3, 7, 'gaily', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (21, 3, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (22, 3, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (23, 3, 7, 'gaily', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (24, 3, 7, 'gaily', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (25, 3, 7, 'gaily', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (26, 3, 7, 'gaily', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (27, 3, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (28, 3, 7, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (29, 4, 7, 'gaily', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (30, 4, 7, 'gaily', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (31, 4, 7, 'gaily', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (32, 4, 7, 'gaily', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (33, 4, 7, 'gaily', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (34, 4, 7, 'gaily', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (35, 4, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (36, 4, 7, 'gaily', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (37, 4, 7, 'gaily', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (38, 4, 1, 'gaily', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (39, 1, 2, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (40, 1, 2, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (41, 1, 2, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (42, 1, 2, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (43, 1, 3, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (44, 1, 3, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (45, 1, 3, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (46, 1, 3, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (47, 1, 4, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (48, 1, 4, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (49, 1, 4, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (50, 1, 4, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (51, 1, 5, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (52, 1, 5, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (53, 1, 5, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (54, 1, 5, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (55, 1, 6, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (56, 1, 6, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (57, 1, 6, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (58, 1, 6, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (59, 1, 7, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (60, 1, 7, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (61, 1, 7, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (62, 1, 7, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (63, 1, 8, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (64, 1, 8, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (65, 1, 8, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (66, 1, 8, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (67, 1, 9, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (68, 1, 9, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (69, 1, 9, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (70, 1, 9, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (71, 1, 10, 'gaily', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (72, 1, 10, 'gaily', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (73, 1, 10, 'gaily', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (74, 1, 10, 'gaily', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4429, 2, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4430, 2, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4431, 2, 7, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4432, 2, 7, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4433, 2, 7, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4434, 2, 7, 'mgreer', 'rep_patient_id', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4435, 2, 7, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4436, 2, 7, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4437, 2, 7, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2819, 1, 6, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2820, 1, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2821, 1, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2822, 1, 6, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2823, 1, 6, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2824, 3, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2825, 3, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2826, 3, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2827, 3, 6, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2828, 3, 6, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2829, 3, 6, 'mgreer', 'rep_patient_id', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2830, 3, 6, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4438, 2, 7, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4439, 2, 7, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4440, 3, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4441, 3, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2831, 3, 6, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2832, 3, 6, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2833, 3, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4442, 3, 7, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4443, 3, 7, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4444, 3, 7, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4445, 3, 7, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4446, 3, 7, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4447, 3, 7, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4448, 3, 7, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4449, 3, 7, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4450, 3, 7, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4451, 1, 7, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4452, 1, 7, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4453, 1, 7, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4454, 1, 7, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4455, 1, 7, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2834, 3, 6, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2835, 2, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2836, 2, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2837, 2, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2838, 2, 6, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2839, 2, 6, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2840, 2, 6, 'mgreer', 'rep_patient_id', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2841, 2, 6, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2842, 2, 6, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2843, 2, 6, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2844, 2, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2845, 2, 6, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2846, 5, 6, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2847, 5, 6, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2848, 5, 6, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2849, 5, 6, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2850, 5, 6, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2851, 5, 6, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2852, 5, 6, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2853, 5, 6, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2854, 5, 6, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2855, 5, 6, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2856, 5, 6, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2409, 1, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2410, 1, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2411, 1, 2, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2412, 1, 2, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2413, 1, 2, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2414, 2, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2415, 2, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2416, 2, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2417, 2, 2, 'mgreer', 'type', 'EditorNonIFrame');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2418, 2, 2, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2419, 2, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2420, 2, 2, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2421, 2, 2, 'mgreer', 'name', 'Editor');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2422, 2, 2, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2423, 2, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2424, 2, 2, 'mgreer', 'clickUrl', 'editor.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2425, 4, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2426, 4, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2427, 4, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2428, 4, 2, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2429, 4, 2, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2430, 4, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2431, 4, 2, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2432, 4, 2, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2433, 4, 2, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2434, 4, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2435, 4, 2, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2436, 3, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2437, 3, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2438, 3, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2439, 3, 2, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2440, 3, 2, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2441, 3, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2442, 3, 2, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2443, 3, 2, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2444, 3, 2, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2445, 3, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2446, 3, 2, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2447, 5, 2, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2448, 5, 2, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2449, 5, 2, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2450, 5, 2, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2451, 5, 2, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2452, 5, 2, 'mgreer', 'rep_patient_id', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2453, 5, 2, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2454, 5, 2, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2455, 5, 2, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2456, 5, 2, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2457, 5, 2, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2458, 1, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2459, 1, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2460, 1, 3, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2461, 1, 3, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2462, 1, 3, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2463, 2, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2464, 2, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2465, 2, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2466, 2, 3, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2467, 2, 3, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2468, 2, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2469, 2, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2470, 2, 3, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2471, 2, 3, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2472, 2, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2473, 2, 3, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2474, 3, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2475, 3, 3, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2476, 3, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2477, 3, 3, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2478, 3, 3, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2479, 3, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2480, 3, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2481, 3, 3, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2482, 3, 3, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2483, 3, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2484, 3, 3, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2485, 4, 3, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2486, 4, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2487, 4, 3, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2488, 4, 3, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2489, 4, 3, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2490, 5, 3, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2491, 5, 3, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2492, 5, 3, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2493, 5, 3, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2494, 5, 3, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2495, 5, 3, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2496, 5, 3, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2497, 5, 3, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2498, 5, 3, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2499, 5, 3, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2500, 5, 3, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2501, 1, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2502, 1, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2503, 1, 10, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2504, 1, 10, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2505, 1, 10, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2506, 3, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2507, 3, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2508, 3, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2509, 3, 10, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2510, 3, 10, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2511, 3, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2512, 3, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2513, 3, 10, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2514, 3, 10, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2515, 3, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2516, 3, 10, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2517, 2, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2518, 2, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2519, 2, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2520, 2, 10, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2521, 2, 10, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2938, 1, 5, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3495, 1, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3496, 1, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3497, 1, 4, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3498, 1, 4, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3499, 1, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3500, 2, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3501, 2, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3502, 2, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3503, 2, 4, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3504, 2, 4, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3505, 2, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3506, 2, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3507, 2, 4, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3508, 2, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2939, 1, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2940, 1, 5, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2941, 1, 5, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2942, 1, 5, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2943, 2, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2944, 2, 5, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2945, 2, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2946, 2, 5, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2947, 2, 5, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2948, 2, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2949, 2, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2950, 2, 5, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2951, 2, 5, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2952, 2, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2953, 2, 5, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3509, 2, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3510, 2, 4, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3511, 3, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3512, 3, 4, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3513, 3, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3514, 3, 4, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3515, 3, 4, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3516, 3, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3517, 3, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3518, 3, 4, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3519, 3, 4, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3520, 3, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3521, 3, 4, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3522, 6, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3523, 6, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3524, 6, 4, 'mgreer', 'order', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3525, 6, 4, 'mgreer', 'name', 'Misc');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3526, 6, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3527, 4, 4, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3528, 4, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3529, 4, 4, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3530, 4, 4, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3531, 4, 4, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3532, 5, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3533, 5, 4, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3534, 5, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3535, 5, 4, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3536, 5, 4, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3537, 5, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3538, 5, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3539, 5, 4, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3540, 5, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3541, 5, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3542, 5, 4, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3543, 7, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3544, 7, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3545, 7, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3546, 7, 4, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3547, 7, 4, 'mgreer', 'order', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3548, 7, 4, 'mgreer', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3549, 7, 4, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3550, 7, 4, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3551, 7, 4, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3552, 7, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3553, 7, 4, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3554, 8, 4, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3555, 8, 4, 'mgreer', 'tab_num', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3556, 8, 4, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3557, 8, 4, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3558, 8, 4, 'mgreer', 'order', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3559, 8, 4, 'mgreer', 'rep_patient_id', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3560, 8, 4, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3561, 8, 4, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3562, 8, 4, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3563, 8, 4, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3564, 8, 4, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4168, 1, 8, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4169, 1, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4170, 1, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4171, 1, 8, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4172, 1, 8, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4173, 5, 8, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4174, 5, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4175, 5, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4176, 5, 8, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4177, 5, 8, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4178, 3, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4179, 3, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4180, 3, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4181, 3, 8, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4182, 3, 8, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4183, 3, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4184, 3, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4185, 3, 8, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2522, 2, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2523, 2, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2524, 2, 10, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2525, 2, 10, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2526, 2, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4186, 3, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2527, 2, 10, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2528, 4, 10, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2529, 4, 10, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2530, 4, 10, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2531, 4, 10, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2532, 4, 10, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2533, 4, 10, 'mgreer', 'rep_patient_id', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2534, 4, 10, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2535, 4, 10, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2536, 4, 10, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2537, 4, 10, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2538, 4, 10, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2965, 4, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2966, 4, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2967, 4, 5, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2968, 4, 5, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2969, 4, 5, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2970, 5, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2971, 5, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2972, 5, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2973, 5, 5, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2974, 5, 5, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2975, 5, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2976, 5, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2977, 5, 5, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2978, 5, 5, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2979, 5, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2980, 5, 5, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2981, 6, 5, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2982, 6, 5, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2983, 6, 5, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2984, 6, 5, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2985, 6, 5, 'mgreer', 'order', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2986, 6, 5, 'mgreer', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2987, 6, 5, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2988, 6, 5, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2989, 6, 5, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2990, 6, 5, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (2991, 6, 5, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4187, 3, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4188, 3, 8, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4189, 2, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4190, 2, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4191, 2, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4192, 2, 8, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4193, 2, 8, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4194, 2, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4195, 2, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4196, 2, 8, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4197, 2, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4198, 2, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4199, 2, 8, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4200, 4, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4201, 4, 8, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4202, 4, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4203, 4, 8, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4204, 4, 8, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4205, 4, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4206, 4, 8, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4207, 4, 8, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4208, 4, 8, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4209, 4, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4210, 4, 8, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4211, 7, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4212, 7, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4213, 7, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4214, 7, 8, 'mgreer', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4215, 7, 8, 'mgreer', 'order', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4216, 7, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4217, 7, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4218, 7, 8, 'mgreer', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4219, 7, 8, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4220, 7, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4221, 7, 8, 'mgreer', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4222, 6, 8, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4223, 6, 8, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4224, 6, 8, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4225, 6, 8, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4226, 6, 8, 'mgreer', 'order', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4227, 6, 8, 'mgreer', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4228, 6, 8, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4229, 6, 8, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4230, 6, 8, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4231, 6, 8, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4232, 6, 8, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3046, 2, 2, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3047, 2, 2, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3048, 2, 2, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3049, 2, 2, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3050, 2, 2, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3051, 2, 2, 'jpiescik', 'rep_patient_id', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3052, 2, 2, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3053, 2, 2, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3054, 2, 2, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3055, 2, 2, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3056, 2, 2, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3057, 1, 2, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3058, 1, 2, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3059, 1, 2, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3060, 1, 2, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3061, 1, 2, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3062, 1, 8, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3063, 1, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3064, 1, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3065, 1, 8, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3066, 1, 8, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3067, 2, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3068, 2, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3069, 2, 8, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3070, 2, 8, 'jpiescik', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3071, 2, 8, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3072, 2, 8, 'jpiescik', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3073, 2, 8, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3074, 2, 8, 'jpiescik', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3075, 2, 8, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3076, 2, 8, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3077, 2, 8, 'jpiescik', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3078, 3, 8, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3079, 3, 8, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3080, 3, 8, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3081, 3, 8, 'jpiescik', 'type', 'Immunizations');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3082, 3, 8, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3083, 3, 8, 'jpiescik', 'rep_patient_id', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3084, 3, 8, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3085, 3, 8, 'jpiescik', 'name', 'Immunizations');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3086, 3, 8, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3087, 3, 8, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3088, 3, 8, 'jpiescik', 'clickUrl', 'immunizationJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3089, 1, 10, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3090, 1, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3091, 1, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3092, 1, 10, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3093, 1, 10, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3094, 2, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3095, 2, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3096, 2, 10, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3097, 2, 10, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3098, 2, 10, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3099, 2, 10, 'jpiescik', 'rep_patient_id', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3100, 2, 10, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3101, 2, 10, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3102, 2, 10, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3103, 2, 10, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3104, 2, 10, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3105, 3, 10, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3106, 3, 10, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3107, 3, 10, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3108, 3, 10, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3109, 3, 10, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3110, 3, 10, 'jpiescik', 'rep_patient_id', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3111, 3, 10, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3112, 3, 10, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3113, 3, 10, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3114, 3, 10, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3115, 3, 10, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3116, 1, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3117, 1, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3118, 1, 5, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3119, 1, 5, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3120, 1, 5, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3121, 2, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3122, 2, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3123, 2, 5, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3124, 2, 5, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3125, 2, 5, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3126, 2, 5, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3127, 2, 5, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3128, 2, 5, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3129, 2, 5, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3130, 2, 5, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3131, 2, 5, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3132, 3, 5, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3133, 3, 5, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3134, 3, 5, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3135, 3, 5, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3136, 3, 5, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3137, 3, 5, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3138, 3, 5, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3139, 3, 5, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3140, 3, 5, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3141, 3, 5, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3142, 3, 5, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3143, 1, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3144, 1, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3145, 1, 4, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3146, 1, 4, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3147, 1, 4, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3148, 2, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3149, 2, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3150, 2, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3151, 2, 4, 'jpiescik', 'type', 'Support');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3152, 2, 4, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3153, 2, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3154, 2, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3155, 2, 4, 'jpiescik', 'name', 'SupportInfo');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3156, 2, 4, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3157, 2, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3158, 2, 4, 'jpiescik', 'clickUrl', 'supportListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3159, 3, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3160, 3, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3161, 3, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3162, 3, 4, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3163, 3, 4, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3164, 3, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3165, 3, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3166, 3, 4, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3167, 3, 4, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3168, 3, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3169, 3, 4, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3170, 4, 4, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3171, 4, 4, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3172, 4, 4, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3173, 4, 4, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3174, 4, 4, 'jpiescik', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3175, 4, 4, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3176, 4, 4, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3177, 4, 4, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3178, 4, 4, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3179, 4, 4, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3180, 4, 4, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3181, 1, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3182, 1, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3183, 1, 1, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3184, 1, 1, 'jpiescik', 'name', 'New');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3185, 1, 1, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3186, 2, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3187, 2, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3188, 2, 1, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3189, 2, 1, 'jpiescik', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3190, 2, 1, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3191, 2, 1, 'jpiescik', 'rep_patient_id', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3192, 2, 1, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3193, 2, 1, 'jpiescik', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3194, 2, 1, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3195, 2, 1, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3196, 2, 1, 'jpiescik', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3197, 3, 1, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3198, 3, 1, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3199, 3, 1, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3200, 3, 1, 'jpiescik', 'type', 'Immunizations');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3201, 3, 1, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3202, 3, 1, 'jpiescik', 'rep_patient_id', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3203, 3, 1, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3204, 3, 1, 'jpiescik', 'name', 'Immunizations');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3205, 3, 1, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3206, 3, 1, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3207, 3, 1, 'jpiescik', 'clickUrl', 'immunizationJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3208, 1, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3209, 1, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3210, 1, 3, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3211, 1, 3, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3212, 1, 3, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3213, 2, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3214, 2, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3215, 2, 3, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3216, 2, 3, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3217, 2, 3, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3218, 2, 3, 'jpiescik', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3219, 2, 3, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3220, 2, 3, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3221, 2, 3, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3222, 2, 3, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3223, 2, 3, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3224, 3, 3, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3225, 3, 3, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3226, 3, 3, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3227, 3, 3, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3228, 3, 3, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3229, 3, 3, 'jpiescik', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3230, 3, 3, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3231, 3, 3, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3232, 3, 3, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3233, 3, 3, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3234, 3, 3, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3235, 1, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3236, 1, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3237, 1, 9, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3238, 1, 9, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3239, 1, 9, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3240, 3, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3241, 3, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3242, 3, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3243, 3, 9, 'jpiescik', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3244, 3, 9, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3245, 3, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3246, 3, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3247, 3, 9, 'jpiescik', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3248, 3, 9, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3249, 3, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3250, 3, 9, 'jpiescik', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3251, 2, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3252, 2, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3253, 2, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3254, 2, 9, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3255, 2, 9, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3256, 2, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3257, 2, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3258, 2, 9, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3259, 2, 9, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3260, 2, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3261, 2, 9, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3262, 4, 9, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3263, 4, 9, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3264, 4, 9, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3265, 4, 9, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3266, 4, 9, 'jpiescik', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3267, 4, 9, 'jpiescik', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3268, 4, 9, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3269, 4, 9, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3270, 4, 9, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3271, 4, 9, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3272, 4, 9, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3273, 1, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3274, 1, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3275, 1, 6, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3276, 1, 6, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3277, 1, 6, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3278, 2, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3279, 2, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3280, 2, 6, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3281, 2, 6, 'jpiescik', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3282, 2, 6, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3283, 2, 6, 'jpiescik', 'rep_patient_id', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3284, 2, 6, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3285, 2, 6, 'jpiescik', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3286, 2, 6, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3287, 2, 6, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3288, 2, 6, 'jpiescik', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3289, 3, 6, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3290, 3, 6, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3291, 3, 6, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3292, 3, 6, 'jpiescik', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3293, 3, 6, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3294, 3, 6, 'jpiescik', 'rep_patient_id', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3295, 3, 6, 'jpiescik', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3296, 3, 6, 'jpiescik', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3297, 3, 6, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3298, 3, 6, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3299, 3, 6, 'jpiescik', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3300, 1, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3301, 1, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3302, 1, 7, 'jpiescik', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3303, 1, 7, 'jpiescik', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3304, 1, 7, 'jpiescik', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3305, 2, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3306, 2, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3307, 2, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3308, 2, 7, 'jpiescik', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3309, 2, 7, 'jpiescik', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3310, 2, 7, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3311, 2, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3312, 2, 7, 'jpiescik', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3313, 2, 7, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3314, 2, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3315, 2, 7, 'jpiescik', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3316, 3, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3317, 3, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3318, 3, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3319, 3, 7, 'jpiescik', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3320, 3, 7, 'jpiescik', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3321, 3, 7, 'jpiescik', 'rep_patient_id', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3322, 3, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3323, 3, 7, 'jpiescik', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3324, 3, 7, 'jpiescik', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3325, 3, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3326, 3, 7, 'jpiescik', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3327, 4, 7, 'jpiescik', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3328, 4, 7, 'jpiescik', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3329, 4, 7, 'jpiescik', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3330, 4, 7, 'jpiescik', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3331, 4, 7, 'jpiescik', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3332, 4, 7, 'jpiescik', 'rep_patient_id', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3333, 4, 7, 'jpiescik', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3334, 4, 7, 'jpiescik', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3335, 4, 7, 'jpiescik', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3336, 4, 7, 'jpiescik', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3337, 4, 7, 'jpiescik', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4369, 1, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4370, 1, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4371, 1, 1, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4372, 1, 1, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4373, 1, 1, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4374, 4, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4375, 4, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4376, 4, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4377, 4, 1, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4378, 4, 1, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4379, 4, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4380, 4, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4381, 4, 1, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4382, 4, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4383, 4, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4384, 4, 1, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4385, 3, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4386, 3, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4387, 3, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4388, 3, 1, 'mgreer', 'type', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4389, 3, 1, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4390, 3, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4391, 3, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4392, 3, 1, 'mgreer', 'name', 'Medications');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4393, 3, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4394, 3, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4395, 3, 1, 'mgreer', 'clickUrl', 'prescriptionJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4396, 2, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4397, 2, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4398, 2, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4399, 2, 1, 'mgreer', 'type', 'Image');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4400, 2, 1, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4401, 2, 1, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4402, 2, 1, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4403, 2, 1, 'mgreer', 'name', 'Images');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4404, 2, 1, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4405, 2, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4406, 2, 1, 'mgreer', 'clickUrl', 'contentFlow.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4407, 6, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4408, 6, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4409, 6, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4410, 6, 1, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4411, 6, 1, 'mgreer', 'order', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4412, 6, 1, 'mgreer', 'rep_patient_id', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4413, 6, 1, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3804, 1, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3805, 1, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3806, 1, 9, 'mgreer', 'order', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3807, 1, 9, 'mgreer', 'name', 'Summary');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3808, 1, 9, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3809, 2, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3810, 2, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3811, 2, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3812, 2, 9, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3813, 2, 9, 'mgreer', 'order', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3814, 2, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3815, 2, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3816, 2, 9, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3817, 2, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3818, 2, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3819, 2, 9, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3820, 4, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3821, 4, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3822, 4, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3823, 4, 9, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3824, 4, 9, 'mgreer', 'order', '4');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3825, 4, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3826, 4, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3827, 4, 9, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3828, 4, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3829, 4, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3830, 4, 9, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3831, 3, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3832, 3, 9, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3833, 3, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3834, 3, 9, 'mgreer', 'type', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3835, 3, 9, 'mgreer', 'order', '3');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3836, 3, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3837, 3, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3838, 3, 9, 'mgreer', 'name', 'Problem');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3839, 3, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3840, 3, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3841, 3, 9, 'mgreer', 'clickUrl', 'problemListJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3842, 6, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3843, 6, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3844, 6, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3845, 6, 9, 'mgreer', 'type', 'Chart');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3846, 6, 9, 'mgreer', 'order', '6');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3847, 6, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3848, 6, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3849, 6, 9, 'mgreer', 'name', 'Charts');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3850, 6, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3851, 6, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3852, 6, 9, 'mgreer', 'clickUrl', 'chart.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3853, 5, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3854, 5, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3855, 5, 9, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3856, 5, 9, 'mgreer', 'name', 'Concerns');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3857, 5, 9, 'mgreer', 'type', 'tab');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3858, 8, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3859, 8, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3860, 8, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3861, 8, 9, 'mgreer', 'type', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3862, 8, 9, 'mgreer', 'order', '8');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3863, 8, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3864, 8, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3865, 8, 9, 'mgreer', 'name', 'Bookmarks');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3866, 8, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3867, 8, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3868, 8, 9, 'mgreer', 'clickUrl', 'bookmarksJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3869, 7, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3870, 7, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3871, 7, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3872, 7, 9, 'mgreer', 'type', 'Detail');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3873, 7, 9, 'mgreer', 'order', '7');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3874, 7, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3875, 7, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3876, 7, 9, 'mgreer', 'name', 'Details');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3877, 7, 9, 'mgreer', 'column', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3878, 7, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3879, 7, 9, 'mgreer', 'clickUrl', 'repository-listJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3880, 10, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3881, 10, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3882, 10, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3883, 10, 9, 'mgreer', 'type', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3884, 10, 9, 'mgreer', 'order', '10');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3885, 10, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3886, 10, 9, 'mgreer', 'repository', 'OurVista');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3887, 10, 9, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3888, 10, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3889, 10, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3890, 10, 9, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3891, 9, 9, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3892, 9, 9, 'mgreer', 'tab_num', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3893, 9, 9, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3894, 9, 9, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3895, 9, 9, 'mgreer', 'order', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3896, 9, 9, 'mgreer', 'rep_patient_id', '9');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3897, 9, 9, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3898, 9, 9, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3899, 9, 9, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3900, 9, 9, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (3901, 9, 9, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4414, 6, 1, 'mgreer', 'name', 'Allergies');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4415, 6, 1, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4416, 6, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4417, 6, 1, 'mgreer', 'clickUrl', 'allergyJSON.jsp');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4418, 5, 1, 'mgreer', 'remove', 'false');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4419, 5, 1, 'mgreer', 'tab_num', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4420, 5, 1, 'mgreer', 'location', 'center');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4421, 5, 1, 'mgreer', 'type', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4422, 5, 1, 'mgreer', 'order', '5');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4423, 5, 1, 'mgreer', 'rep_patient_id', '1');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4424, 5, 1, 'mgreer', 'repository', 'local');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4425, 5, 1, 'mgreer', 'name', 'Symptoms');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4426, 5, 1, 'mgreer', 'column', '2');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4427, 5, 1, 'mgreer', 'server', '');
INSERT INTO widget_params (id, widget_id, patient_id, username, param, value) VALUES (4428, 5, 1, 'mgreer', 'clickUrl', 'listHistoryTemplate.jsp');


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


