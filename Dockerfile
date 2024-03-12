FROM openjdk:17
WORKDIR /app
COPY opc2aas/target/basyx.opc2aas-3.1.4.jar .
EXPOSE 8081
CMD ["java", "-jar", "basyx.opc2aas-3.1.4.jar"]
