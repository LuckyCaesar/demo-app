#!/bin/bash

echo "Ready to start..."
specified_jar="demo-app-1.0.0.jar"

if [ ! -f "$specified_jar" ];then
    echo "Warning: No executable jar!"
    exit 0
fi

echo "Start run..."
#echo "Please visit [ http://localhost:8088/app/data/index ] in the browser!"

java -jar $specified_jar
echo $?
