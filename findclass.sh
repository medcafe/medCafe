#!/bin/bash
for zip in `find . -name *"*.jar"`
do
    echo "==> $zip"
    unzip -t $zip | grep $1
done
