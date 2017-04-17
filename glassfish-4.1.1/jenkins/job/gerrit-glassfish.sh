#!/bin/bash

use_java 1.8
use_maven

succeeded=true
echo $BUILD_NAME
mvn --batch-mode || succeeded=false
$succeeded
