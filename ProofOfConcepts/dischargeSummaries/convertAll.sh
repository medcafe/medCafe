#!/bin/bash
for f in *
do
  echo "Processing $f file..."
  # take action on each file. $f store current file name
  text2pdf $f > $f.pdf
done
