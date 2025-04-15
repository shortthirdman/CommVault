# First stage, build the custom JRE
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

RUN mkdir /opt/app
COPY . /opt/app

WORKDIR /opt/app

ENV MAVEN_VERSION 3.8.8
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN apk update && \
    apk add --no-cache tar binutils

RUN wget "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz" && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

RUN mvn package -DskipTests
RUN jar xvf target/commvault-accounts-1.0.0-SNAPSHOT.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 17  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    target/commvault-accounts-1.0.0-SNAPSHOT.jar > modules.txt

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-17

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest AS runner
ENV JAVA_HOME=/opt/jdk/jdk-17
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

# Add app user
ARG APP_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APP_USER &&  adduser --system $APP_USER --ingroup $APP_USER

# Create the application directory
RUN mkdir /app && chown -R $APP_USER /app

COPY --chown=$APP_USER:$APP_USER target/*.jar /app/app.jar

WORKDIR /app

USER $APP_USER

EXPOSE 8080

HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

ENTRYPOINT [ "java", "-XX:MaxRAMPercentage=80.0", "-XX:+UseContainerSupport", "-jar", "/app/app.jar" ]