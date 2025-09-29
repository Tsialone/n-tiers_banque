# Base officielle WildFly avec Java 17
FROM quay.io/wildfly/wildfly:29.0.0.Final-jdk17

# Create PostgreSQL module directory
USER root
RUN mkdir -p /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main
USER jboss

# Copy pre-downloaded driver and module.xml
COPY postgresql-driver.jar /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/postgresql-42.6.0.jar
COPY module.xml /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/

# Copy configuration and deployment
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/
COPY target/*.war /opt/jboss/wildfly/standalone/deployments/

# Expose ports
EXPOSE 8080 9990

# Start WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]