#!/bin/sh
awk 'BEGIN{FS=","}; {print NF,$0}' $1 | sort -r -n | head -1 | cut -d" " -f2-
