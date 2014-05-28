set -ex
mvn install -Dmaven.test.skip -Djdk16_home=/Library/Java/Home
cd distribution
mvn assembly:assembly

