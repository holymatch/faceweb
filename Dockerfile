FROM gradle:4.2.1-jdk8-alpine as build_faceweb
WORKDIR /app
COPY . /app
RUN whoami
RUN gradle build \
  && cd /app/build/distributions \
  && tar -xf /app/build/distributions/faceweb.tar

FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Match Chan "matchhwc@connect.hku.hk"
COPY --from=build_faceweb /app/build/distributions/faceweb /app/faceweb
ENTRYPOINT ["/app/faceweb/bin/faceweb"]
