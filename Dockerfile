FROM sonatype/nexus3:3.90.1-alpine

COPY ./nexus-repository-license-bypass-agent-1.0.jar /opt/sonatype/nexus/bin/nexus-repository-license-bypass-agent-1.0.jar
COPY ./nexus /opt/sonatype/nexus/bin/nexus
COPY ./nexus.bat /opt/sonatype/nexus/bin/nexus.bat
