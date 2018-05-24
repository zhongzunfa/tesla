FROM ubuntu:16.04

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ENV JRE_HOME /usr/lib/jvm/java-8-oracle/jre
    
ADD server-jre-8u172-linux-x64.tar.gz /tmp

RUN mkdir -p /usr/lib/jvm && mv /tmp/jdk1.8.0_172 "${JAVA_HOME}" && \
    sed -i 's/http:\/\/archive\.ubuntu\.com\/ubuntu\//http:\/\/mirrors\.163\.com\/ubuntu\//g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y curl net-tools && \
	rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
    
RUN update-alternatives --install "/usr/bin/java" "java" "${JRE_HOME}/bin/java" 1 && \
	update-alternatives --install "/usr/bin/javac" "javac" "${JAVA_HOME}/bin/javac" 1 && \
	update-alternatives --set java "${JRE_HOME}/bin/java" && \
	update-alternatives --set javac "${JAVA_HOME}/bin/javac"

ADD target/tesla-gateway-1.0.0.jar /root/app.jar
ADD bin/start.sh /root/
RUN chmod +x /root/*.sh;mkdir /root/logs 
ENV APP_NAME tesla-gateway
ENV JAVA_OPTS ""
WORKDIR /root
EXPOSE 9000
CMD ["./start.sh"]