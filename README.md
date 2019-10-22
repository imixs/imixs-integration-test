# Imixs-Workflow Integration Tests


This project provides a set of integration test. The project can be run with the help of Docker and starts automatically a set of test cases. This helps to verify the full behavior of the Imixs-Workflow Engine.

## Versioning

The version number of this test suite is coupled directly to the latest version of Imixs-Workflow.


## Build and Run the Integration Test Suite

The project is based on Maven and runs in a Docker container. To build the project and a docker image on the current version run:

	$ mvn clean install -Pdocker
 
To start the rests run:

	$ docker-compose up
	
## Log Level
	
The log level for all org.imixs.worklfow classes is set to FINEST. See the standalone.xml file.