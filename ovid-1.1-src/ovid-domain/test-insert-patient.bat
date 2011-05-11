@if "%4%"=="" goto GETINPUT

:ARGS
@set rpcaddress="%1"
@set rpcport="%2"
@set access_code="%3"
@set verify_code="%4"

@goto RUN

:GETINPUT
@set /p rpcaddress="Enter host that rpcbroker runs on: "
@set /p rpcport="Enter port that rpcbroker listens on: "
@set /p access_code="Enter OVID access code: "
@set /p verify_code="Enter OVID verify code (warning: not hidden): "

@goto RUN

:RUN

@java -cp bin;contrib/medsphere/ovid.jar;contrib/sun/javaee.jar;contrib/lib/log4j-1.2.13.jar;contrib/lib/jaxen-1.1.1.jar com.medsphere.ovid.tutorial.fm.insert.PatientInsertTutorial "%rpcaddress%" "%rpcport%" "%access_code%" "%verify_code%"


