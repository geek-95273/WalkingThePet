FROM x91ia1ev81ns6f.xuanyuan.run/library/eclipse-temurin:8-jre-alpine
VOLUME /tmp
COPY walking-pet-order-service/target/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
