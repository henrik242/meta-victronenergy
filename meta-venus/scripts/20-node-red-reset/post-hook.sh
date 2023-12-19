#!/bin/sh

script="Node-RED reset"

function notify () {
  if [ -d /service/dbus-characterdisplay ]; then
      cd /service && svc -d dbus-characterdisplay
  fi

  if [ -f /opt/victronenergy/prodtest/notify.py ]; then
    NOTIFY="/opt/victronenergy/prodtest/notify.py"
  else
    NOTIFY="echo notify "
  fi

  ${NOTIFY} --message "Node-RED reset done" --count 3

  if [ -d /service/dbus-characterdisplay ]; then
      cd /service && svc -u dbus-characterdisplay
  fi
}

echo "### ${script} starting"

for dir in /data/home/nodered/{.node-red,.npm}
do
  if [ -d ${dir} ]
  then
    echo "Removing ${dir}"
    rm -rf ${dir}
  fi
done

sync

# Play notification to let the user know the script is done
notify

echo "### ${script} done"

exit 0
