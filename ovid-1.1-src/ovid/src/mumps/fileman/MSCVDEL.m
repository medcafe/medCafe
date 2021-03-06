MSCVDEL	; MSC/JDA - OVID FileMan DELETE ; SEP 28, 2010 22:00:00
 ;;1.0;OpenVista Interface Domain;**1500**;May 14, 2009
RUN(REPLY,RES)	;
 N PROPNAME,FILE,FDA,IENS,MSG,ERRNO
 ; Parse out the command properties
 F  Q:'$$NEXTPROP^MSCVRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCVRES(.RES)
 . I PROPNAME="FILE" S FILE=$$FILE2NUM^MSCV($$GETVAL^MSCVRES(.RES)) Q
 . I PROPNAME="IENS" S IENS=$$GETVAL^MSCVRES(.RES) Q
 . Q
 S:$E(IENS,$L(IENS))'="," IENS=IENS_","
 S FDA(FILE,IENS,.01)="@"
 D FILE^DIE(,"FDA","MSG")
 D ADDCMPD^MSCVRES(.REPLY,"RESULTS")
 F ERRNO=1:1 Q:'$G(MSG("DIERR",ERRNO))  D
 . D ADDCMPD^MSCVRES(.REPLY,"ERROR")
 . D ADDPROP^MSCVRES(.REPLY,"ERROR NUMBER",MSG("DIERR",ERRNO))
 . D ADDPROP^MSCVRES(.REPLY,"ERROR TEXT",MSG("DIERR",ERRNO,"TEXT",1))
 . D ENDCMPD^MSCVRES(.REPLY) ; ERROR
 . Q
 D ENDCMPD^MSCVRES(.REPLY) ; RESULTS
 Q
