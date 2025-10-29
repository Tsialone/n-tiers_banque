FROM quay.io/wildfly/wildfly:29.0.0.Final-jdk17

# Add application user for EJB remoting
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u ejbuser -p ejbpass -g users

COPY target/change-ejb.war /opt/jboss/wildfly/standalone/deployments/

# Copier ton standalone.xml personnalisé
# COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml

# Supprimer le WAR zippé
# RUN rm /opt/jboss/wildfly/standalone/deployments/change-ejb.war

EXPOSE 9090 8880

# CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
