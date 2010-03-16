MSCFME ;
 ;
 L +^TMP("FME",0):0
 E  W "Scanner is already running.",! Q
 ; Delete the data in 21455
 N FDA,TMP
 D LIST^DIC(21455,,"@","Q",,,,,,,"TMP")
 S TMP="" F  S TMP=$O(TMP("DILIST",2,TMP)) Q:TMP=""  S FDA(21455,TMP_",",.01)="@"
 D FILE^DIE(,"FDA")
 K ^TMP("FME")
 S ^TMP("FME","FILE")=1
 F  D  Q:^TMP("FME","FILE")="@"
 . S ^TMP("FME",1)=0
 . J CHILD($J)
 . F  Q:^TMP("FME",1)  H 2
 . L +^TMP("FME",1)
 . L -^TMP("FME",1)
 . D:^TMP("FME","FILE")'="@" SAVE(^TMP("FME","FILE"),"*","S","Halted, possible due to a SCREEN on the file")
 . Q
 L -^TMP("FME",0)
 Q 
CHILD(PARENT)
 N SL,TRG,MSG,FILE,FIELD,HADERROR,ETYPE,REASON
 L +^TMP("FME",1)
 S ^TMP("FME",1)=1
 S $ETRAP="G ERROR^MSCFME"
 S HADERROR=0
 S FILE=^TMP("FME","FILE")
 F  S FILE=$O(^DIC(FILE)) Q:FILE'=+FILE  D
 . S ^TMP("FME","FILE")=FILE
 . S FIELD=0
 . F  S FIELD=$O(^DD(FILE,FIELD)) Q:FIELD'=+FIELD  D
 . . K MSG
 . . D FIELD^DID(FILE,FIELD,,"MULTIPLE-VALUED","TRG","MSG")
 . . Q:TRG("MULTIPLE-VALUED")
 . . S REASON=""
 . . D TRY(FILE,FIELD)
 . . I $D(MSG) D
 . . . S REASON="FileMan reported the error: "_MSG("DIERR",1,"TEXT",1)
 . . . S HADERROR=1
 . . Q:'HADERROR
 . . S ETYPE="I"
 . . D TRY(FILE,FIELD_"I")
 . . S:HADERROR ETYPE="N"
 . . D SAVE(FILE,FIELD,ETYPE,REASON)
 . . Q
 . Q
 S ^TMP("FME","FILE")="@"
 L -^TMP("FME",1)
 Q
TRY(FILE,FIELD)
 S HADERROR=0
 S SL=$STACK,HADERROR=0
 D LIST^DIC(FILE,,"@;"_FIELD,"Q",50,,,,,,"TRG","MSG")
 Q
ERROR
 N I
 I REASON="" S REASON="FileMan error with $ECODE="_$ECODE
 S:SL=$STACK $ECODE="",HADERROR=1
 Q
SAVE(FILE,FIELD,ETYPE,REASON)
 N FDA
 S FDA(21455,"+1,",.01)=FILE_"^"_FIELD
 S FDA(21455,"+1,",1)=FILE
 S FDA(21455,"+1,",2)=FIELD
 S FDA(21455,"+1,",3)=ETYPE
 S FDA(21455,"+1,",4)=REASON
 D UPDATE^DIE(,"FDA")
 Q
