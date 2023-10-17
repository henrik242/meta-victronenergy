#!/bin/sh

script="SignalK delete data logs"

function beep () {
  if [ -f /opt/victronenergy/prodtest/buzzer.sh ]; then
    BEEP="/opt/victronenergy/prodtest/buzzer.sh"
  elif [ -f /opt/victronenergy/prodtest/buzzer.py ]; then
    BEEP="/opt/victronenergy/prodtest/buzzer.py"
  else
    BEEP="echo beep "
  fi

  for i in 1 2 3
  do
    ${BEEP} 1
    sleep 0.2
    ${BEEP} 0
  done
}

echo "### ${script} starting"

# Also remove hidden files
shopt -s dotglob
for dir in /data/log/signalk-server
do
  if [ -d ${dir} ]
  then
    echo "Removing all files from ${dir}"
    rm -r ${dir}/*
  fi
done

sync

# Play notification to let the user know the script is done
beep

echo "### ${script} done"

exit 0
