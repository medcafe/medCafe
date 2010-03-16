#!/bin/bash

if [ ! -n "$4" ]
then

  echo

  echo -n "Enter host that vistalink runs on (localhost): "
  read vaddress
  if [ "$vaddress" = "" ]; then
      vaddress=localhost
  fi

  echo

  echo -n "Enter port that vistalink listens on (8002): "
  read vport
  if [ "$vport" = "" ]; then
      vport=8002
  fi

  echo

  access_code=""
  until [ "$access_code" != "" ]; do
      echo -n "Enter access code: "
      read access_code
  done

  echo

  verify_code=""
  until [ "$verify_code" != "" ]; do
  echo -n "Enter verify code: "
  stty -echo
  read verify_code
  stty echo
  done

  echo

else

  vaddress="$1"
  vport="$2"
  access_code="$3"
  verify_code="$4"
fi

java -Dhttp.proxyHost=gatekeeper.mitre.org -Dhttp.proxyPort=80  -cp build/web/WEB-INF/classes:contrib/medsphere/ovid.jar:contrib/medsphere/msc-vistalink.jar:contrib/medsphere/msc-resource.jar:contrib/medsphere/msc-vistarpc.jar:contrib/medsphere/fileman.jar:contrib/medsphere/fmdomain.jar:contrib/medsphere/msc-common.jar:contrib/medsphere/rpcresadapter.jar:contrib/vistalink/vljConnector-1.5.0.026.jar:contrib/vistalink/vljFoundationsLib-1.5.0.026.jar:contrib/vistalink/vljSecurity-1.5.0.026.jar:contrib/sun/javaee.jar:contrib/log4j/log4j-1.2.13.jar:contrib/jaxen/jaxen-core.jar:contrib/jaxen/saxpath.jar:contrib/jaxen/jaxen-dom.jar com.medsphere.ovid.domain.ov.PatientRepository "$vaddress" "$vport" "$access_code" "$verify_code"

