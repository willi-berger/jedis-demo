jedis-demo
==========

some tests with Jedis

Requirements:

	* maven 3.0 or later
	* Java 1.7 or later
	* A running Redis keystore (http://redis.io/)
	
Build:
	> mvn clean install
	> mvn eclipse:eclipse

	
Contains a Java implmentation of the Redis auto complete example:	

	* mvn package
	* java -jar target\uber-jedis-demo-1.0.0-SNAPSHOT.jar marcelle