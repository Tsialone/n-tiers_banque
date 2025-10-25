FROM quay.io/wildfly/wildfly:29.0.0.Final-jdk17

# Add application user for EJB remoting
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u ejbuser -p ejbpass -g users

COPY target/change-ejb.jar /opt/jboss/wildfly/standalone/deployments/

EXPOSE 9090 8880

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]