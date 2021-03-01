FROM java:latest
LABEL description="KubeEdge Honeypot Web App"
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir -p /usr
RUN mkdir -p /usr/local
COPY . /usr/local/
WORKDIR /usr/local
EXPOSE 8089
ENTRYPOINT ["java","-jar","honeypot-v1.0.0.jar"]
