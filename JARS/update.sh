#!/bin/bash

# Step 1
mvn -f ../ClienteCalculadora/pom.xml clean install

# Step 2
mvn -f ../MOMCalculadora/pom.xml clean install

# Step 3
mvn -f ../ServerCalculadora/pom.xml clean install

# Step 4
cp ../ClienteCalculadora/target/ClienteCalculadora-1.0-SNAPSHOT-shaded.jar cliente.jar

# Step 5
cp ../MOMCalculadora/target/MOMCalculadora-1.0-SNAPSHOT-shaded.jar mom.jar

# Step 6
cp ../ServerCalculadora/target/ServerCalculadora-1.0-SNAPSHOT-shaded.jar servers.jar

