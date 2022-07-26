
mkdir -p jar

rm pom.xml; ln -s xml/lexjava.pom.xml pom.xml
mvn package
mv target/LexJava-1.0.jar jar

rm pom.xml; ln -s xml/lexc.pom.xml pom.xml
mvn package
mv target/LexC-1.0.jar jar

rm pom.xml; ln -s xml/tagjava.pom.xml pom.xml
mvn package
mv target/TagJava-1.0.jar jar

rm pom.xml; ln -s xml/tagc.pom.xml pom.xml
mvn package
mv target/TagC-1.0.jar jar


rm pom.xml; ln -s xml/lexjavadir.pom.xml pom.xml
mvn package
mv target/LexJavaDir-1.0.jar jar

rm pom.xml; ln -s xml/lexcdir.pom.xml pom.xml
mvn package
mv target/LexCDir-1.0.jar jar

rm pom.xml; ln -s xml/tagjavadir.pom.xml pom.xml
mvn package
mv target/TagJavaDir-1.0.jar jar

rm pom.xml; ln -s xml/tagcdir.pom.xml pom.xml
mvn package
mv target/TagCDir-1.0.jar jar

rm -r target dependency-reduced-pom.xml
