MSCFMREF; MSC/JDA - OVID FileMan REFBY ; May 14, 2009
 ;;1.0;MSCFM;;May 14, 2009
 Q
RUN(REPLY,RES)
 N FILENUM,PROPNAME,VAR,FILE,FIELD,FILENAME
 S FILENUM=0,FILENAME=""
 F  Q:'$$NEXTPROP^MSCRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCRES(.RES)
 . I PROPNAME="FILE" S FILENAME=$$GETVAL^MSCRES(.RES) Q
 . Q
 S:FILENAME'="" FILENUM=$$FILE2NUM^MSCFM(FILENAME)
 I FILENUM=0 D ADDPROP^MSCRES(.REPLY,"ERROR","Could not find file.") Q
 S VAR="^DD("_FILENUM_",0,""PT"")"
 F  S VAR=$Q(@VAR) Q:($QS(VAR,1)'=FILENUM)!($QS(VAR,3)'="PT")  D
 . S FILE=$QS(VAR,4)
 . S FIELD=$QS(VAR,5)
 . D ADDCMPD^MSCRES(.REPLY,"R")
 . D ADDPROP^MSCRES(.REPLY,"F",$$NUM2FILE^MSCFM(FILE))
 . D ADDPROP^MSCRES(.REPLY,"F#",FILE)
 . D ADDPROP^MSCRES(.REPLY,"C",$P(^DD(FILE,FIELD,0),"^",1))
 . D ADDPROP^MSCRES(.REPLY,"C#",FIELD)
 . D ENDCMPD^MSCRES(.REPLY) ; R
 . Q
 Q