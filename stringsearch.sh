#!/bin/bash

if [[ ! -z $1 ]] && [[ $1 == "-h" ]]
then
    cat README
else
    java -jar stringsearch-0.0.1-SNAPSHOT.jar $1
fi