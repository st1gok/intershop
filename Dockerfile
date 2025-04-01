ARG RUNTIME_IMAGE=openjdk:21-bookworm

FROM ${RUNTIME_IMAGE}
ARG APP_NAME=blog
WORKDIR /app
ADD target/${APP_NAME}.jar ${APP_NAME}.jar
RUN groupadd -r st1g && \
    useradd -r -g  st1g st1g && \
    chown -R st1g:st1g /app
USER st1g
CMD [ "java", "-jar", "intershop.jar" ]