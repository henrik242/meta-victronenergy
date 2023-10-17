#!/bin/sh

script="Reset all"

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

# remove everything from /data/ besides venus/ 
echo "removing files from /data"
find /data -mindepth 1 -maxdepth 1 ! -path /data/venus ! -exec rm -rf "{}" \;
sync

# remove this script, it should only run once (and should be done now).
echo "done"
rm /data/rcS.local

sync

# Play notification to let the user know the script is done
beep

echo "### ${script} done - rebooting"

# since also directories etc are removed, which were created earlier
# in the boot process, lets reboot so they will be recreated.
reboot
