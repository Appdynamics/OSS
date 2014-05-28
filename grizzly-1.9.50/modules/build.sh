set -e

export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
echo "Using Java 6 Home:" $JAVA_HOME

cd utils
mvn clean install -Dmaven.test.skip
cd ..

cd grizzly
mvn clean install -Dmaven.test.skip
cd ..

cd http
mvn clean install -Dmaven.test.skip
cd ..

cd http-ajp
mvn clean install -Dmaven.test.skip
cd ..

cd config
mvn clean install -Dmaven.test.skip
cd ..

D=grizzly-build-`git rev-parse HEAD |head -c 6`
rm -fr $D
mkdir $D
cp grizzly/target/grizzly-framework-1.9.50.jar $D/grizzly-framework.jar
cp utils/target/grizzly-utils-1.9.50.jar  $D/grizzly-utils.jar
cp http/target/grizzly-http-1.9.50.jar $D/grizzly-http.jar
cp http-ajp/target/grizzly-http-ajp-1.9.50.jar $D/grizzly-http-ajp.jar
cp config/target/grizzly-config.jar $D/grizzly-config.jar

