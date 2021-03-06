MSCV	; MSC/JDA - Common OVID FileMan routines ; SEP 28, 2010 22:00:00
 ;;1.0;OpenVista Interface Domain;**1500**;May 14, 2009
PROCESS(REPLY,RES)	;
 N TEMP,PROPNAME
 D FIRST^MSCVRES(.RES)
 D START^MSCVRES(.REPLY)
 D ADDCMPD^MSCVRES(.REPLY,"FILEMAN")
 S TEMP=$$NEXTPROP^MSCVRES(.RES)
 ; JDA - TODO : Check for valid top-property
 F  Q:'$$NEXTPROP^MSCVRES(.RES)  D
 . S PROPNAME=$$GETPROP^MSCVRES(.RES)
 . I PROPNAME="GETS" D RUN^MSCVGET(.REPLY,.RES) Q
 . I PROPNAME="LIST" D RUN^MSCVLST(.REPLY,.RES) Q
 . I PROPNAME="FIND" D RUN^MSCVFND(.REPLY,.RES) Q
 . I PROPNAME="INSERT" D RUN^MSCVINS(.REPLY,.RES) Q
 . I PROPNAME="DELETE" D RUN^MSCVDEL(.REPLY,.RES) Q
 . I PROPNAME="UPDATE" D RUN^MSCVUPD(.REPLY,.RES) Q
 . I PROPNAME="FILEINFO" D FILEINFO^MSCVFI(.REPLY,.RES) Q
 . I PROPNAME="FILES" D FILES^MSCVFI(.REPLY,.RES) Q
 . I PROPNAME="PROJECT" X:$T(^MSCSQL2)'="" "D RPCPROJ^MSCSQL2(.REPLY,.RES)" Q
 . I PROPNAME="DIC" D RUN^MSCVDIC(.REPLY,.RES) Q
 . I PROPNAME="INDEX" D RUN^MSCVIDX(.REPLY,.RES) Q
 . I PROPNAME="REFBY" D RUN^MSCVREF(.REPLY,.RES) Q
 . I PROPNAME="QIEN" D QIEN^MSCVQ1(.REPLY,.RES) Q
 . Q
 D ENDCMPD^MSCVRES(.REPLY) ; FILEMAN
 Q
FILE2NUM(FILE)	;
 N PC,PART,FLD
 S PART=$P(FILE,"^",1)
 S FILENUM=$$FIND1^DIC(1,,"X",$P(PART,"^",1))
 Q:FILENUM="" 0
 F PC=2:1:$L(FILE,"^") D
 . S PART=$P(FILE,"^",PC)
 . S FLD=$$FLD2NUM(FILENUM,PART)
 . S FILENUM=+$P(^DD(FILENUM,FLD,0),"^",2)
 . Q
 Q FILENUM
FLD2NUM(FILENUM,FLDNUM)	;
 I FLDNUM?1(.N0.1"I",.N1".".N0.1"I") S:'$D(^DD(FILENUM,FLDNUM)) FLDNUM=0 Q FLDNUM ; already a number, but verify it exists
 E  Q $$FLDNUM^DILFD(FILENUM,FLDNUM)
NUM2FILE(FILENUM)	;
 N FILENAME
 S FILENAME=$O(^DD(FILENUM,0,"NM",""))
 Q:('$D(^DIC(FILENUM,0))&($G(^DD(FILENUM,0,"UP"))="")) "" ; No File. Garbage? Like .3
 Q FILENAME
