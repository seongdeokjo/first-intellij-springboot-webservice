#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PROFILE=$(find_idle_port)

echo "> health check start"
echo "> IDLE_PORT: $IDLE_PORT"
ehco "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then # $up_count >= 1 ('real' 문자열이 있는지 검증)
      echo "> Health Check 성공"
      switch_proxy
      break
  else
      echo "> Health Check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
      echo "> Health Check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health Check 실패"
    ehco "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  ehco "> Health Check 연결 실패. 재시도 ...."
  sleep 10
done

# 엔진엑스와 연결되지 않은 포트로 스프링 부트가 잘 수행되었는지 체크
# 잘 떴는지 확인되어야 엔진엑스 프록시 설정을 변경(switch_proxy) 합니다.
# 엔진엑스 프폭시 설정 변경은 switch.sh에서 수행