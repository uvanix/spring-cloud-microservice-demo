#!/bin/sh

APP_NAME=APP
APP_DIR=`pwd`
APP_LOG_DIR=$APP_DIR/logs

JAR_NAME=$APP_NAME\.jar
PID_FILE=$APP_NAME\.pid

APP_ENV=" --spring.config.location=application.yml --spring.profiles.active=test "

JAVA_OPTS=" -Duser.timezone=Asia/Shanghai -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_GC_OPTS=" -verbose:gc -Xloggc:$APP_LOG_DIR/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps "
JAVA_DUMP_OPTS=" -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$APP_LOG_DIR/logs/java.hprof -XX:+DisableExplicitGC "
JAVA_MEM_OPTS=" -server "
#JAVA_MEM_OPTS=" -server -XX:MetaspaceSize=1024M -XX:MaxMetaspaceSize=8192m "

# 检查日志目录
check_logdir() {
  if [ ! -d "$APP_LOG_DIR" ]; then
    mkdir "$APP_LOG_DIR"
  fi
}

# 检查程序是否正在运行
check_running() {
  if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE"); then
    return 1
  else
    return 0
  fi
}

# 启动程序
start() {
  check_running
  if [ $? -eq "1" ]; then
    echo "$APP_NAME is already running. Pid is $(cat "$PID_FILE") "
  else
    check_logdir
    nohup java $JAVA_OPTS $JAVA_GC_OPTS $JAVA_DUMP_OPTS $JAVA_MEM_OPTS -jar $JAR_NAME $APP_ENV > /dev/null 2>&1 &
    echo $! > $PID_FILE
    echo "start $APP_NAME success ... "
  fi
}

# 停止程序
stop() {
  check_running
  if [ $? -eq "1" ]; then
    echo "stopping $APP_NAME ... "
    PID="$(cat "$PID_FILE")"
    kill -9 $PID
    rm "$PID_FILE"
    echo "... $APP_NAME stoppped"
  else
    echo "$APP_NAME not running ... "
  fi
}

# 重启程序
restart() {
  stop
  sleep 2
  start
}

# 运行状态
status() {
  check_running
  if [ $? -eq "1" ]; then
    echo "$APP_NAME is already running. pid is $(cat "$PID_FILE") "
  else
    echo "$APP_NAME is not running. "
  fi
}

# 使用说明
usage() {
  echo "usage: sh $APP_NAME.sh [start|stop|restart|status]"
  exit 1
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "restart")
    restart
    ;;
  "status")
    status
    ;;
  *)
    usage
    ;;
esac
