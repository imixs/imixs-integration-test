FROM imixs/wildfly:1.2.7

# Imixs-Microservice Version 1.0.2
MAINTAINER ralph.soika@imixs.com

# add configuration files
COPY ./src/docker/configuration/* ${WILDFLY_CONFIG}/

# Copy sample application
COPY ./target/imixs-integration-test.war $WILDFLY_DEPLOYMENT  

