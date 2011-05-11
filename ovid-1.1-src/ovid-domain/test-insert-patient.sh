#!/bin/bash

if [ ! -n "$4" ]
then

  echo

  echo -n "Enter host that rpcbroker runs on (openvista.medsphere.org): "
  read rpcaddress
  if [ "$rpcaddress" = "" ]; then
      rpcaddress=openvista.medsphere.org
  fi

  echo

  echo -n "Enter port that rpcbroker listens on (9201): "
  read rpcport
  if [ "$rpcport" = "" ]; then
      rpcport=9201
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

  rpcaddress="$1"
  rpcport="$2"
  access_code="$3"
  verify_code="$4"
fi

java -cp bin:contrib/medsphere/ovid.jar:contrib/sun/javaee.jar:contrib/lib/log4j-1.2.13.jar:contrib/lib/jaxen-1.1.1.jar com.medsphere.ovid.tutorial.fm.insert.PatientInsertTutorial "$rpcaddress" "$rpcport" "$access_code" "$verify_code"

