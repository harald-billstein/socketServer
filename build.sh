#!/usr/bin/env bash
mvn install
cd target
java -jar socketserver-0.0.1-SNAPSHOT.jar
