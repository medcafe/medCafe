MSCBFM; JDA 17OCT2007
 ;
ENTRY(RETVAL,ARGBYTES)
 ; Entry point for RPC that handles FileMan requests
 ;  both parameters are in byte format
 N RES,REPLYRES S RES="",REPLYRES="",RETVAL=""
 D FROMBYTE^MSCRES(.RES,.ARGBYTES)
 D PROCESS^MSCFM(.REPLYRES,.RES)
 D TOBYTE^MSCRES(.RETVAL,.REPLYRES)
 Q
