#!/bin/bash

use_java 1.8
use_maven

succeeded=true
echo $BUILD_NAME
mvn --batch-mode || succeeded=false

git tag -m 'Preserve all Glassfish 4.1.1 builds, as these are infrequent and almost always tied to a Controller release' Glassfish-$BUILD_NAME $BUILD_NAME
git push origin Glassfish-$BUILD_NAME

$succeeded

