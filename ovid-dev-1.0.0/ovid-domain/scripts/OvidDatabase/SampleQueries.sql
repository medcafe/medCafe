-- get list of flowsheets
select me.DisplayName, mepc.Value as IsActive, mepc2.Value as CanAddSection, mepc3.Value as CanAddReading
from MedicalEventDisplayConfiguration mec
join MedicalEvent me
    on mec.MedicalEvent = me.Name and me.DataType = 'Flowsheet'
left join MedicalEventPropertyConfiguration mepc
    on me.Name = mepc.MedicalEvent and mepc.PropertyName = 'IsActive' 
left join MedicalEventPropertyConfiguration mepc2
    on me.Name = mepc2.MedicalEvent and mepc2.PropertyName = 'CanAddSection'
left join MedicalEventPropertyConfiguration mepc3
    on me.Name = mepc3.MedicalEvent and mepc3.PropertyName = 'CanAddReading'
order by mec.ID;
/* DisplayOrder; */

-- get list of flowsheet sections in Main
select me.DisplayName
from MedicalEventDisplayConfiguration mec
join MedicalEvent me
    on mec.MedicalEvent = me.Name
where me.DataType = 'Section'
      and mec.Flowsheet = 'Main'
order by mec.DisplayOrder;

-- get row layout for Main flowsheet for ALL Facilities and all users
select me.ID, me.DisplayName, me.DataType, me.UnitsDescription, me.DefaultReferenceRange, mec.DisplayOrder
from MedicalEventDisplayConfiguration mec
join MedicalEvent me on mec.MedicalEvent = me.Name
where Flowsheet = 'Main'
      and Facility in ('ALL')
      and DUZ in (0)
order by DisplayOrder;

-- get row layout for Main flowsheet for 'ED' facility and all users
select me.ID, me.DisplayName, me.DataType, me.UnitsDescription, me.DefaultReferenceRange, mec.DisplayOrder
from MedicalEventDisplayConfiguration mec
join MedicalEvent me on mec.MedicalEvent = me.Name
where Flowsheet = 'Main'
      and Facility in ('ALL', 'ED')
      and DUZ in (0)
order by DisplayOrder;

-- get row layout for Main flowsheet for user 1
select me.ID, me.DisplayName, me.DataType, me.UnitsDescription, me.DefaultReferenceRange, mec.DisplayOrder
from MedicalEventDisplayConfiguration mec
join MedicalEvent me on mec.MedicalEvent = me.Name
where Flowsheet = 'Main'
      and Facility in ('ALL')
      and DUZ in (0,1)
order by DisplayOrder;


-- get row layout for Main flowsheet for user 1 in the ED Facility
select me.ID, me.DisplayName, me.DataType, me.UnitsDescription, me.DefaultReferenceRange, mec.DisplayOrder
from MedicalEventDisplayConfiguration mec
join MedicalEvent me on mec.MedicalEvent = me.Name
where Flowsheet = 'Main'
      and Facility in ('ALL','ED')
      and DUZ in (0,1)
order by DisplayOrder;


select * 
from MedicalEventDisplayConfiguration mec
join MedicalEvent me on mec.MedicalEvent = me.Name
where Flowsheet = 'Renal' order by DisplayOrder;

update MedicalEvent set DefaultReferenceRange = '95 - 110' where Name = 'Temperature';

select max(ModificationTimestamp) as latest from MedicalEvent
union
select max(ModificationTimestamp) as latest from MedicalEventDisplayConfiguration
union
select max(ModificationTimestamp) as latest from MedicalEventPropertyConfiguration
order by latest desc;


/* the following are queries for the MedicalEventReading table.... */

insert IDReference(ReferenceKey, Type) values (1, 'Patient');
select LAST_INSERT_ID() into @last_id;
select @last_id;
-- insert some data
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 12, 1,'100.0', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 12, 1,'102.3', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 12, 1,'98.6', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser, ReferenceRange) values (@last_id, 12, 1,'98.7', NOW(),  1, '90-120');

insert IDReference(ReferenceKey, Type) values (1, 'Patient');
select LAST_INSERT_ID() into @last_id;
select @last_id;
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 13, 1,'150/100', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 13, 1,'148/100', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser) values (@last_id, 13, 1,'155/120', NOW(),  1);
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser, ReferenceRange) values (@last_id, 13, 1,'150/99', NOW(),  1, '90-120');
insert MedicalEventReading (ReadingID, MedicalEventID, PatientID, Value, ReadingDateTime, VersionUser, ReferenceRange) values (@last_id, 13, 1,'151/99', NOW(),  1, '90-120');

select me.DisplayName,
       UnitsDescription, 
       mer.ReadingID,
       mer.PatientID, 
       mer.Value, 
       mer.ReadingDateTime,
       mer.AbnormalIndication, 
       mer.Version,
       case when mer.ReferenceRange <> '' then mer.ReferenceRange else me.DefaultReferenceRange end as ReferenceRange,
       mer.VersionUser,
       (select count(mer3.Version) from MedicalEventReading mer3 where mer.ReadingID = mer3.ReadingID) as n_versions
from MedicalEventReading mer
join MedicalEvent me on me.ID = mer.MedicalEventID
where 
      mer.PatientID = 1
      and mer.ReadingDateTime >= '2008/1/1 00:00:00'
      and mer.ReadingDateTime <= '2010/9/30 23:00:00'
      and mer.Version = (select max(Version) 
                 from MedicalEventReading mer2
                 where mer.ReadingID = mer2.ReadingID)      
      and mer.DeletedFlag = false
;


select me.DisplayName,
       UnitsDescription, 
       mer.ReadingID,
       mer.PatientID, 
       mer.Value, 
       mer.ReadingDateTime,
       mer.VersionTimestamp,
       mer.AbnormalIndication, 
       case when mer.ReferenceRange <> '' then mer.ReferenceRange else me.DefaultReferenceRange end as ReferenceRange,
       mer.VersionUser,
       mer.Version
from MedicalEventReading mer
join MedicalEvent me on me.ID = mer.MedicalEventID
order by Version desc;

--delete from MedicalEventReading;