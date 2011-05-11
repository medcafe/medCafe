@if "%6%"=="" goto GETINPUT

:ARGS
@set vaddress="%1"
@set vport="%2"
@set access_code="%3"
@set verify_code="%4"
@set useraccess_code="%5"
@set userverify_code="%6"

@goto RUN

:GETINPUT
@set /p vaddress="Enter host that rpcbroker runs on: "
@set /p vport="Enter port that rpcbroker listens on: "
@set /p access_code="Enter OVID access code: "
@set /p verify_code="Enter OVID verify code (warning: not hidden): "
@set /p useraccess_code="Enter access code of user with OR CPRS GUI CHART: "
@set /p userverify_code="Enter verify code of user with OR CPRS GUI CHART (warning: not hidden): "

@goto RUN

:RUN

@java -cp bin;contrib/medsphere/ovid.jar;lib/ovid-domain.jar;contrib/slf4j/slf4j-api-1.6.1.jar com.medsphere.ovid.domain.ov.PatientRepository "%vaddress%" "%vport%" "%access_code%" "%verify_code%" "%useraccess_code%" "%userverify_code%"


