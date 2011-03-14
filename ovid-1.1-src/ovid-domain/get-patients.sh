#!/bin/bash

if [ ! -n "$6" ]
then

  echo

  echo -n "Enter host that rpcbroker runs on (openvista.medsphere.org): "
  read vaddress
  if [ "$vaddress" = "" ]; then
      vaddress=openvista.medsphere.org
  fi

  echo

  echo -n "Enter port that rpcbroker listens on (9201): "
  read vport
  if [ "$vport" = "" ]; then
      vport=9201
  fi

  echo

  access_code=""
  until [ "$access_code" != "" ]; do
      echo -n "Enter access code of OVID user: "
      read access_code
  done

  echo

  verify_code=""
  until [ "$verify_code" != "" ]; do
  echo -n "Enter verify code of OVID user: "
  stty -echo
  read verify_code
  stty echo
  done

  useraccess_code=""
  until [ "$useraccess_code" != "" ]; do
      echo -n "Enter access code of user with OR CPRS GUI CHART: "
      read useraccess_code
  done

  echo

  userverify_code=""
  until [ "$userverify_code" != "" ]; do
  echo -n "Enter verify code of user with OR CPRS GUI CHART: "
  stty -echo
  read userverify_code
  stty echo
  done

  echo

else

  vaddress="$1"
  vport="$2"
  access_code="$3"
  verify_code="$4"
  useraccess_code="$5"
  userverify_code="$6"
fi

java -cp bin:contrib/medsphere/ovid.jar:lib/ovid-domain.jar:contrib/slf4j/slf4j-api-1.6.1.jar com.medsphere.ovid.domain.ov.PatientRepository "$vaddress" "$vport" "$access_code" "$verify_code" "$useraccess_code" "$userverify_code"

