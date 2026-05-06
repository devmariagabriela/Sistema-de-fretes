#!/usr/bin/env bash
set -euo pipefail

TOMCAT_BASE="${TOMCAT_BASE:-/tmp/gw-frete-tomcat9}"
JAVA_HOME="${JAVA_HOME:-/home/maria.gabriela/.sdkman/candidates/java/25.0.3-tem}"

if [ ! -x "$JAVA_HOME/bin/java" ]; then
    JAVA_HOME="/home/maria.gabriela/.sdkman/candidates/java/25.0.3-tem"
fi

export JAVA_HOME
export CATALINA_HOME="$TOMCAT_BASE"
export CATALINA_BASE="$TOMCAT_BASE"

exec "$TOMCAT_BASE/bin/catalina.sh" run
