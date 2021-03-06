MSCFMINS; JDA 16NOV2007
 ;
 Q
RUN(REPLY,RES)
 N PROPNAME,FILE,INPUTFDA,IEN,SEQ,IDX,MSG,FDA
 ; Parse out the command properties
 F  Q:'$$NEXTPROP^MSCRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCRES(.RES)
 . I PROPNAME="FIELD" D PROCFLD(.RES,FILE,.INPUTFDA,.FDA)
 . I PROPNAME="FILE" S FILE=$$FILE2NUM^MSCFM($$GETVAL^MSCRES(.RES)) Q
 . Q
 D VALS^DIE(,"INPUTFDA","FDA","MSG")
 Q:$$ERROR(.MSG)
 D UPDATE^DIE(,"FDA","IEN","MSG")
 Q:$$ERROR(.MSG)
 D ADDCMPD^MSCRES(.REPLY,"RESULTS")
 S SEQ=""
 F  S SEQ=$O(IEN(SEQ)) Q:SEQ=""  D
 . D ADDCMPD^MSCRES(.REPLY,"IEN")
 . D ADDPROP^MSCRES(.REPLY,"VALUE",IEN(SEQ))
 . D ADDPROP^MSCRES(.REPLY,"SEQ",SEQ)
 . D ENDCMPD^MSCRES(.REPLY) ; IEN
 . Q
 D ENDCMPD^MSCRES(.REPLY) ; RESULTS
 Q
PROCFLD(RES,FILE,INPUTFDA,FDA)
 N NAME,VALUE,IENS,INTERN
 S IENS="+1,"
 S INTERN=0
 F  Q:'$$NEXTPROP^MSCRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCRES(.RES)
 . I PROPNAME="NAME" S NAME=$$FLD2NUM^MSCFM(FILE,$$GETVAL^MSCRES(.RES)) Q
 . I PROPNAME="VALUE" S VALUE=$$GETVAL^MSCRES(.RES) Q
 . I PROPNAME="INTERNAL" S INTERN=$$GETVAL^MSCRES(.RES) Q
 . I PROPNAME="IENS" S IENS=$$GETVAL^MSCRES(.RES) Q
 . Q
 I INTERN S INPUTFDA(FILE,IENS,NAME)=VALUE
 E  S FDA(FILE,IENS,NAME)=VALUE
 Q
ERROR(MSG)
 Q:$G(MSG("DIERR"))="" 0
 N NUM
 D ADDCMPD^MSCRES(.REPLY,"ERRORS")
 F IDX=1:1 Q:$G(MSG("DIERR",IDX))=""  D
 . S NUM=""
 . D ADDCMPD^MSCRES(.REPLY,"ERROR")
 . S NUM=$G(MSG("DIERR",IDX,"PARAM","FIELD"))
 . S:NUM="" NUM=$G(MSG("DIERR",IDX,"PARAM","FILE"))
 . D ADDPROP^MSCRES(.REPLY,"NUMBER",NUM)
 . D ADDPROP^MSCRES(.REPLY,"TEXT",MSG("DIERR",IDX,"TEXT",1))
 . D ENDCMPD^MSCRES(.REPLY) ; FIELD
 D ENDCMPD^MSCRES(.REPLY) ; ERRORS
 Q 1
