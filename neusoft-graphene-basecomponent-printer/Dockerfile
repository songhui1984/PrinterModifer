FROM 10.253.105.75:5000/env/jdk8
COPY ./target/za-graphene-basecomponent-printer.jar /root/startup/
WORKDIR /root/startup/
EXPOSE 8080
CMD ["java","-Xms1600m","-Xmx1600m","-DAPP_DOMAIN=za-graphene-basecomponent-printer","-jar", "za-graphene-basecomponent-printer.jar"]