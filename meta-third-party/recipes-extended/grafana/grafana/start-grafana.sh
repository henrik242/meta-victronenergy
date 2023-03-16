#!/bin/bash
exec 2>&1
echo "*** Starting grafana ***"

PATH="/usr/share/grafana/bin:$PATH" 

HOME="/data/conf/grafana"

GRAFANA_HOME="/usr/share/grafana"

# This might need to change, as /etc is readonly
CONF_DIR=/etc/grafana
CONF_FILE=$CONF_DIR/grafana.ini
WORK_DIR=$GRAFANA_HOME
DATA_DIR=$HOME
PLUGINS_DIR=$HOME/plugins
PROVISIONING_CFG_DIR=$HOME/provisioning

if [ ! -f $HOME/grafana.db ]; then
  echo "Copying inital settings"
  cp -r /usr/share/grafana/initial/* $HOME
  chmod 0640 $HOME/grafana.db
fi


exec grafana-server                                         \
  --homepath="$GRAFANA_HOME"                                \
  --config="$CONF_FILE"                                     \
  "$@"                                                      \
  cfg:default.log.mode="console"                            \
  cfg:default.log.level="warn"                              \
  cfg:default.paths.data="$DATA_DIR"                        \
  cfg:default.paths.plugins="$PLUGINS_DIR"                  \
  cfg:default.paths.provisioning="$PROVISIONING_CFG_DIR"
