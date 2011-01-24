#!/bin/bash
#for f in `find . -name "*gif"`
for f in `find . -name "*pdf"`
do
  echo "Processing $f file..."
  # take action on each file. $f store current file name
  # convert -thumbnail 300x300 756_UNKNOWN.txt.pdf[0] thumb.png
  name=${f%\.*}
  convert -thumbnail 300x300 ${f}[0] ${name}_thumb.png
  # echo ${name}
done
