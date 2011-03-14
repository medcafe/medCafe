create table FlowsheetItem (
    ID                      int not null AUTO_INCREMENT,
    Name                    varchar(50) not null,
    DisplayName             varchar(50) not null,
    DataType                varchar(50) not null,
    UnitsDescription        varchar(50) not null default '',
    DefaultReferenceRange   varchar(50) not null default '',
    Parent                  varchar(50) default '',
    ModificationTimestamp   timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
);

/* add flowsheets */
insert into FlowsheetItem (Name, DisplayName, DataType) values ('Main', 'Main', 'Flowsheet');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('IsActive', 'IsActive', 'Boolean', 'Main');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('CanAddSection', 'CanAddSection', 'Boolean', 'Main');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('CanAddReading', 'CanAddReading', 'Boolean', 'Main');

insert into FlowsheetItem (Name, DisplayName, DataType) values ('Renal', 'Renal', 'Flowsheet');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('IsActive', 'IsActive', 'Boolean', 'Renal');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('CanAddSection', 'CanAddSection', 'Boolean', 'Renal');
insert into FlowsheetItem (Name, DisplayName, DataType, Parent) values ('CanAddReading', 'CanAddReading', 'Boolean', 'Renal');

/* add sections */
insert into FlowsheetItem (Name, DisplayName, DataType) values ('Vitals', 'Vitals', 'Section');
insert into FlowsheetItem (Name, DisplayName, DataType) values ('Physical Assessment', 'Physical Assessment', 'Section');

create table FlowsheetProfile (
    ID                      int not null AUTO_INCREMENT,
    Owner                   varchar(50) not null,
    OwnerType               varchar(50) not null, -- e.g. 'FACILITY, UNIT, USER'
    ItemID                  int not null,
    ItemType                varchar(50) not null, -- e.g. FlowsheetItem or MedicalEvent    
) engine = INNODB;

/* setup Facility Flowsheet for MAIN */
insert into FlowsheetProfile (Owner, OwnerType, ItemID, ItemType) values ('1', 'FACILITY', 
create table FlowsheetItemx (
    ID                      int not null AUTO_INCREMENT,
    Name                    varchar(50) not null,
    DisplayName             varchar(50) not null,
    DataType                varchar(50) not null,
    UnitsDescription        varchar(50) not null default '',
    DefaultReferenceRange   varchar(50) not null default '',
    Parent                  varchar(50) default '',
    ModificationTimestamp   timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,

    primary key PKFlowsheetItem (ID),
    unique index IDXFlowsheetItem_Name (Name,Parent),
    constraint FKFlowsheetItem2MedicalEventDataType foreign key (DataType)
                references MedicalEventDataType(Name) on delete cascade,
    constraint FKFlowsheetItem2FlowsheetItemParent foreign key (Parent)
                references FlowsheetItem (Name) on delete cascade
) engine = INNODB;

/* this is needed to allow a parent of '' */
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('', '', 'String', '');

/* add flowsheets */
insert into MedicalEvent (Name, DisplayName, DataType) values ('Main', 'Main', 'Flowsheet');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('IsActive', 'IsActive', 'Boolean', 'Main');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('CanAddSection', 'CanAddSection', 'Boolean', 'Main');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('CanAddReading', 'CanAddReading', 'Boolean', 'Main');

insert into MedicalEvent (Name, DisplayName, DataType) values ('Renal', 'Renal', 'Flowsheet');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('IsActive', 'IsActive', 'Boolean', 'Renal');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('CanAddSection', 'CanAddSection', 'Boolean', 'Renal');
insert into MedicalEvent (Name, DisplayName, DataType, Parent) values ('CanAddReading', 'CanAddReading', 'Boolean', 'Renal');

/* add sections */
insert into MedicalEvent (Name, DisplayName, DataType) values ('Vitals', 'Vitals', 'Section');
insert into MedicalEvent (Name, DisplayName, DataType) values ('Physical Assessment', 'Physical Assessment', 'Section');

/* vital sign flavored medical events */
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Temperature', 'Temp', 'Double', 'F');
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Blood Pressure', 'BP', 'MillimetersOfMercury', 'mmHg');
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Pulse', 'Pulse', 'Integer', 'beats/minute');
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Respirations', 'Respirations', 'Integer', 'breaths/minute');
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Weight', 'Weight', 'Double', 'lbs');
insert into MedicalEvent (Name, DisplayName, DataType, UnitsDescription) values ('Pain Score', 'Pain Score', 'Integer', '');

/* physical assessment flavored medical events */
insert into MedicalEvent (Name, DisplayName, DataType) values ('Pregnant', 'Pregnant', 'Boolean');
insert into MedicalEvent (Name, DisplayName, DataType) values ('Lactating', 'Lactating', 'Boolean');
insert into MedicalEvent (Name, DisplayName, DataType) values ('Reflexes', 'Reflexes', 'SubSection');
insert into MedicalEvent (Name, DisplayName, DataType) values ('Arm', 'Arm', 'String');

/*---------------------------------------------------------------------------
 * 
 *---------------------------------------------------------------------------*/
create table FlowsheetDisplayConfiguration (
    ID                                  int not null AUTO_INCREMENT,
    OwnerID                             varchar(50) not null,  -- this is the ID of the Facility, Unit, User that owns it.
    MedicalEvent                        varchar(50) not null,
    Flowsheet                           varchar(50),
    Section                             varchar(50),
    SubSection                          varchar(50),
    DisplayOrder                        decimal(11,5) not null,
    Unit                                varchar(50) not null default 'ALL',
    DUZ                                 int not null default 0,
    ModificationTimestamp               timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  
    primary key PKFlowsheetDisplayConfiguration (ID),
    unique index IDXFlowsheetDisplayConfiguration_DisplayOrder (DisplayOrder),
    constraint FKFlowsheetDisplayConfiruation2MedicalEventType foreign key (MedicalEvent)
                references MedicalEvent(Name) on delete cascade
) engine = INNODB;

/* main flowsheet */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet) values (100.0, 'Main', null);
/* vitals section */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet) values (101.0, 'Vitals', 'Main');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (102.0, 'Temperature', 'Main', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (103.0, 'Blood Pressure', 'Main', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (104.0, 'Pulse', 'Main', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (105.0, 'Respirations', 'Main', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (106.0, 'Weight', 'Main', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (107.0, 'Pain Score', 'Main', 'Vitals');

/* physical assessment section */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet) values (201.0, 'Physical Assessment', 'Main');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (202.0, 'Pregnant', 'Main', 'Physical Assessment');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (203.0, 'Lactating', 'Main', 'Physical Assessment');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (204.0, 'Reflexes', 'Main', 'Physical Assessment');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section, SubSection) values (205.0, 'Arm', 'Main', 'Physical Assessment', 'Reflexes');

/* Facility == 'ED' adds temperature to Physical Assessment */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section, Facility) values (201.50, 'Temperature', 'Main', 'Physical Assessment', 'ED');

/* USER == 1 adds Pain Score to Physical Assessment */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section, DUZ) values (201.505, 'Pain Score', 'Main', 'Physical Assessment', 1);

/* renal flowsheet */
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet) values (1000.0, 'Renal', null);
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet) values (1001.0, 'Vitals', 'Renal');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1002.0, 'Temperature', 'Renal', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1003.0, 'Blood Pressure', 'Renal', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1004.0, 'Pulse', 'Renal', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1005.0, 'Respirations', 'Renal', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1006.0, 'Weight', 'Renal', 'Vitals');
insert into MedicalEventDisplayConfiguration (DisplayOrder, MedicalEvent, Flowsheet, Section) values (1007.0, 'Pain Score', 'Renal', 'Vitals');
