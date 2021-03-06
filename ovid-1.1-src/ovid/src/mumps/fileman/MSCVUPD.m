MSCVUPD	; MSC/JDA - OVID FileMan UPDATE ; SEP 28, 2010 22:00:00
 ;;1.0;OpenVista Interface Domain;**1500**;May 14, 2009
 ;
RUN(REPLY,RES)	;
 N PROPNAME,FILE,FDA,IENS,MSG,IDX,INPUTFDA
 ; Parse out the command properties
 F  Q:'$$NEXTPROP^MSCVRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCVRES(.RES)
 . I PROPNAME="FIELD" D PROCFLD(.RES,FILE,IENS,.INPUTFDA,.FDA)
 . I PROPNAME="FILE" S FILE=$$FILE2NUM^MSCV($$GETVAL^MSCVRES(.RES)) Q
 . I PROPNAME="IENS" S IENS=$$GETVAL^MSCVRES(.RES) S:$E(IENS,$L(IENS))'="," IENS=IENS_"," Q
 . Q
 D VALS^DIE(,"INPUTFDA","FDA","MSG")
 I $G(MSG("DIERR"))'="" D  Q
 . D ADDCMPD^MSCVRES(.REPLY,"ERRORS")
 . F IDX=1:1 Q:$G(MSG("DIERR",IDX))=""  D
 . . D ADDCMPD^MSCVRES(.REPLY,"ERROR")
 . . D ADDPROP^MSCVRES(.REPLY,"NUMBER",MSG("DIERR",IDX,"PARAM","FIELD"))
 . . D ADDPROP^MSCVRES(.REPLY,"TEXT",MSG("DIERR",IDX,"TEXT",1))
 . . D ENDCMPD^MSCVRES(.REPLY) ; ERROR
 . D ENDCMPD^MSCVRES(.REPLY) ; ERRORS
 . Q
 D FILE^DIE(,"FDA","MSG")
 I $G(MSG("DIERR"))'="" D
 . D ADDCMPD^MSCVRES(.REPLY,"ERRORS")
 . F IDX=1:1 Q:'$G(MSG("DIERR",IDX))  D
 . . D ADDCMPD^MSCVRES(.REPLY,"ERROR")
 . . D ADDPROP^MSCVRES(.REPLY,"NUMBER",MSG("DIERR",IDX))
 . . D ADDPROP^MSCVRES(.REPLY,"TEXT",MSG("DIERR",IDX,"TEXT",1))
 . . D ENDCMPD^MSCVRES(.REPLY) ; ERROR
 . . Q
 . D ENDCMPD^MSCVRES(.REPLY) ; ERROR
 . Q
 E  D ADDCMPD^MSCVRES(.REPLY,"RESULTS") D ENDCMPD^MSCVRES(.REPLY) ; Empty results 
 Q
PROCFLD(RES,FILE,IENS,INPUTFDA,FDA)	;
 N NAME,VALUE,INTERN,RESULT
 S INTERN=0
 F  Q:'$$NEXTPROP^MSCVRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCVRES(.RES)
 . I PROPNAME="NAME" S NAME=$$GETVAL^MSCVRES(.RES) Q  ; Either label or number is OK
 . I PROPNAME="VALUE" S VALUE=$$GETVAL^MSCVRES(.RES) Q
 . I PROPNAME="INTERNAL" S INTERN=$$GETVAL^MSCVRES(.RES) Q
 . Q
 I INTERN S INPUTFDA(FILE,IENS,NAME)=VALUE
 E  S FDA(FILE,IENS,NAME)=VALUE
 Q
