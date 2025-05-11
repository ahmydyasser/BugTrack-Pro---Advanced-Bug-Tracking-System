#!/bin/bash

# Clean and build the project using Maven
mvn clean package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful! You can run the application using:"
    echo "java -jar target/bug-tracker-1.0-SNAPSHOT.jar"
else
    echo "Build failed!"
    exit 1
fi
