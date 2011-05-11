@if "%4%"=="" goto GETINPUT

:ARGS
@set vaddress="%1"
@set vport="%2"
@set access_code="%3"
@set verify_code="%4"

@goto RUN

:GETINPUT
@set /p vaddress="Enter host that vistalink runs on: "
@set /p vport="Enter port that vistalink listens on: "
@set /p access_code="Enter OVID access code: "
@set /p verify_code="Enter OVID verify code (warning: not hidden): "

@goto RUN

:RUN

@java -cp build/web/WEB-INF/classes;contrib/medsphere/ovid.jar;contrib/medsphere/msc-vistalink.jar;contrib/medsphere/msc-resource.jar;contrib/medsphere/msc-vistarpc.jar;contrib/medsphere/fileman.jar;contrib/medsphere/fmdomain.jar;contrib/medsphere/msc-common.jar;contrib/medsphere/rpcresadapter.jar;contrib/vistalink/vljConnector-1.5.0.026.jar;contrib/vistalink/vljFoundationsLib-1.5.0.026.jar;contrib/vistalink/vljSecurity-1.5.0.026.jar;contrib/sun/javaee.jar;contrib/log4j/log4j-1.2.13.jar;contrib/jaxen/jaxen-core.jar;contrib/jaxen/saxpath.jar;contrib/jaxen/jaxen-dom.jar com.medsphere.ovid.domain.ov.PatientRepository "%vaddress%" "%vport%" "%access_code%" "%verify_code%"


