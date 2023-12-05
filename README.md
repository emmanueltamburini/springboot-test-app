# SpringBoot-test

## SpringBoot README

## JUNIT5 app

This application is an example app when it uses big part of the tools of spring boot test offers to test

### Getting started

You must configure your environment to handle java, you can find [here](https://www.oracle.com/java/technologies/downloads/) or [here](https://jdk.java.net/). Java 21 is the version used in this app.

You must also install maven, you can find [here](https://maven.apache.org/download.cgi). Apache Maven 3.6.3 is the version used in this app

Before that you can run `mvn clean dependency:resolve` to install all dependencies of the app.

And finally you can run `mvn clean spring-boot:run`, You are able to see the documentation in [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

And also you can run the app and `mvn clean test -Dgroups='!Integration_rest_template'` or `mvn clean test -Dgroups='!Integration_web_client'`  to run all the tests of this example app, there are two way to
run this tests, because in this project there are two file with integrations test (different way) and both cannot run at the same time
to avoid write in the h2 db twice

It is recommended to install an IDE to try this app for more comfortability, It is recommended IntelliJ IDEA, you can download [here](https://www.jetbrains.com/idea/download/)
