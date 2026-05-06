#!/usr/bin/env bash
set -euo pipefail

PROJETO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
TOMCAT_ORIGEM="${TOMCAT_ORIGEM:-/home/maria.gabriela/Downloads/apache-tomcat-9.0.116 GWEB}"
TOMCAT_BASE="${TOMCAT_BASE:-/tmp/gw-frete-tomcat9}"
JAVA_HOME="${JAVA_HOME:-/home/maria.gabriela/.sdkman/candidates/java/25.0.3-tem}"

if [ ! -x "$JAVA_HOME/bin/java" ]; then
    JAVA_HOME="/home/maria.gabriela/.sdkman/candidates/java/25.0.3-tem"
fi

export JAVA_HOME

cd "$PROJETO_DIR"
mvn -q -DskipTests package

if [ ! -d "$TOMCAT_BASE" ]; then
    cp -R "$TOMCAT_ORIGEM" "$TOMCAT_BASE"
fi

perl -0pi -e 's/<Server port="[^"]+"/<Server port="18088"/; s/<Connector port="[^"]+" protocol="HTTP\/1\.1"/<Connector port="18087" protocol="HTTP\/1.1"/' "$TOMCAT_BASE/conf/server.xml"

find "$TOMCAT_BASE/webapps" -mindepth 1 -maxdepth 1 -exec rm -rf {} +
cp "$PROJETO_DIR/target/gw-frete.war" "$TOMCAT_BASE/webapps/gw-frete.war"

echo "WAR implantado em $TOMCAT_BASE/webapps/gw-frete.war"
echo "Porta HTTP configurada: 18087"
