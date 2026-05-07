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
export GWFRETE_DB_URL="${GWFRETE_DB_URL:-jdbc:postgresql://localhost:55432/gw_frete}"
export GWFRETE_DB_USUARIO="${GWFRETE_DB_USUARIO:-postgres}"
export GWFRETE_DB_SENHA="${GWFRETE_DB_SENHA:-postgres}"

setsid "$TOMCAT_BASE/bin/catalina.sh" run > "$TOMCAT_BASE/logs/codex-console.out" 2>&1 < /dev/null &
echo "Tomcat started. PID: $!"
echo "URL: http://localhost:18087/gw-frete/login"
