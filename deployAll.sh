#!/bin/bash

echo "Copying html"
\cp -R html/* tomcat/webapps/medcafe
echo "Copying tags"
\cp config/tags/* tomcat/webapps/medcafe/WEB-INF/tags/
echo "Copying jarfile"
\cp medcafe.jar tomcat/webapps/medcafe/WEB-INF/lib/
echo "Copying templates"
\cp -R config/templates/* tomcat/webapps/medcafe/WEB-INF/templates/
echo "Restarting tomcat"
cd tomcat
./restart.sh
cd -
