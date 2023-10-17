#!/bin/sh

script="backup all data to usb stick"

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

rsync --archive --info=progress2 --human-readable /data /media/sda/backup

sync

# Play notification to let the user know the script is done
beep

echo "### ${script} done"

exit 0
