#!/bin/bash

for file in *.yaml *.yml
do
        kubectl delete -f "$file"
done
