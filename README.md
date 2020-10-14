# logistics-test-restassured

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Build status](https://travis-ci.org/esign-consulting/logistics-test-restassured.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-restassured)

Project for API testing the [Logistics](https://github.com/esign-consulting/logistics) application. The test is based on [REST-assured](http://rest-assured.io) and is executed through [Maven](https://maven.apache.org) **(installation required)**.

In order to run the test, execute the command `mvn test -Dserver.host=http://<logistics_host>`, replacing *<logistics_host>* by the hostname where the Logistics application is available. The command `mvn test -Dserver.host=http://www.esign.com.br`, for example, executes the test against the instance of the application at <http://www.esign.com.br/logistics>.

If the application is listening through a port other than 80, set the parameter *server.port* as well. If none of the parameters are defined, the test is done against <http://localhost:8080>. It would be the same as executing `mvn test -Dserver.host=http://localhost -Dserver.port=8080`.
