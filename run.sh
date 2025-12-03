#!/usr/bin/env bash
set -e

need_cmd() {
  command -v "$1" >/dev/null 2>&1
}

ensure_java() {
  if need_cmd java; then return; fi
  if need_cmd apt-get; then
    sudo apt-get update -y
    sudo DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-11-jdk ||
      sudo DEBIAN_FRONTEND=noninteractive apt-get install -y default-jdk
  elif need_cmd brew; then
    brew install openjdk@11
    sudo ln -sfn "$(brew --prefix)/opt/openjdk@11/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-11.jdk 2>/dev/null || true
  else
    echo "Instale Java 11 manualmente." >&2
    exit 1
  fi
}

ensure_maven() {
  if need_cmd mvn; then return; fi
  if need_cmd apt-get; then
    sudo apt-get update -y
    sudo DEBIAN_FRONTEND=noninteractive apt-get install -y maven
  elif need_cmd brew; then
    brew install maven
  else
    echo "Instale Maven manualmente." >&2
    exit 1
  fi
}

ensure_mysql() {
  if need_cmd mysql; then return; fi
  if need_cmd apt-get; then
    sudo apt-get update -y
    sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-client ||
      sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server
  elif need_cmd brew; then
    brew install mysql
  else
    echo "Instale MySQL manualmente." >&2
    exit 1
  fi
}

ensure_java
ensure_maven
ensure_mysql

if need_cmd systemctl; then
  sudo systemctl start mysql || true
elif need_cmd service; then
  sudo service mysql start || true
elif need_cmd brew; then
  brew services start mysql || true
fi

mvn -q -DskipTests clean package

DB_PROPS="src/main/resources/application.properties"
DB_USER=$(grep '^database.username=' "$DB_PROPS" | cut -d= -f2-)
DB_PASS=$(grep '^database.password=' "$DB_PROPS" | cut -d= -f2-)

if [ -n "$DB_USER" ]; then
  export MYSQL_PWD="$DB_PASS"
  mysql -u "$DB_USER" < schema.sql || echo "Não foi possível executar schema.sql, verifique MySQL."
  unset MYSQL_PWD
fi

java -jar target/sportcourt-system-1.0.0-jar-with-dependencies.jar
