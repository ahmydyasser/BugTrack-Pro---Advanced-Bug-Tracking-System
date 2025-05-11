#!/bin/bash
# Compile Java files
javac -cp "lib/*" -d target/classes src/main/java/*.java


# Run the app
java -cp "target/classes:lib/*" LoginGUI
