
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
   	patientId character varying(50) NOT NULL,
   	subject character varying(250) NULL,
   	note text NULL
   	
);

CREATE TABLE text_templates (
	user_template_id SERIAL PRIMARY KEY,
    username character varying(50) NOT NULL,
   	subject character varying(250) NULL,
   	template text NULL
   	
);
