#
# Build stage
#
FROM gradle:6.3-jdk11 as compiler

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

# moving jar file
COPY /build/libs/rent_app-0.0.1-SNAPSHOT.jar /build/libs/rent-app-1.0-SNAPSHOT.jar
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# move application jar
RUN mv /build/libs/rent-app-1.0-SNAPSHOT.jar service.jar

#
# Run stage
#
FROM adoptopenjdk/openjdk11:jre-11.0.4_11-alpine

ENV SERVER_PORT=8080
ENV APP_HOME=/usr/app/
ENV SECURITY_OPTS="-Dnetworkaddress.cache.negative.ttl=0 -Dnetworkaddress.cache.ttl=0"
ENV CONTAINER_SUPPORT="-XX:+UseContainerSupport"
ENV MAX_RAM_PERCENTAGE="-XX:MaxRAMPercentage=80"
ENV MIN_RAM_PERCENTAGE="-XX:MinRAMPercentage=80"
ENV MAX_HEAP_SIZE="-XX:MaxHeapSize=1024m"
ENV INITIAL_HEAP_SIZE="-XX:InitialHeapSize=512m"
ENV HEAP_NEW_SIZE="-XX:NewSize=32m"
ENV GC="-XX:+UseG1GC"
ENV G1_RESERVE_PERCENT="-XX:G1ReservePercent=10"
ENV STRING_DEDUPLICATION="-XX:+UseStringDeduplication"
ENV VERIFY_NONE="-Xverify:none"
ENV TIERED_STOP_AT_LEVEL="-XX:TieredStopAtLevel=1"

ENV ENV=""
ENV DB_DNS=""

COPY --from=compiler $APP_HOME/service.jar $APP_HOME/service.jar
COPY --from=compiler $APP_HOME/wait-for-it.sh $APP_HOME/wait-for-it.sh

RUN apk update && apk add bash
RUN chmod +x $APP_HOME/wait-for-it.sh

WORKDIR $APP_HOME

EXPOSE ${SERVER_PORT}

ENV JAVA_OPTS="$SECURITY_OPTS $MAX_RAM_PERCENTAGE $MIN_RAM_PERCENTAGE $MAX_HEAP_SIZE $INITIAL_HEAP_SIZE $GC $G1_RESERVE_PERCENT $STRING_DEDUPLICATION $MAX_GC_PAUSE_MILLIS $CONTAINER_SUPPORT $HEAP_NEW_SIZE $VERIFY_NONE $TIERED_STOP_AT_LEVEL"
ENTRYPOINT   ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "service.jar"]