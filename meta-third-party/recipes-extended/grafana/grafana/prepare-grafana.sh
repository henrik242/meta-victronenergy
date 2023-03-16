#!/bin/bash

HOME="/data/conf/grafana"

if [ ! -d $HOME ]; then
    mkdir -p $HOME
fi

if [ ! -f $HOME/grafana.db ]; then
  cp /usr/share/grafana/initial/grafana.db $HOME
  chmod 0640 $HOME/grafana.db
fi

for d in access-control alerting dashboards datasources notifiers plugins
do
  if [ ! -d $HOME/provisioning/${d} ]; then
    cp -r /usr/share/grafana/initial/provisioning/${d} $HOME/provisioning
  fi
done

if [ ! -d $HOME/plugins ]; then
    mkdir -p $HOME/plugins
fi


# Note: -h is needed to prevent errors when trying to change dangling symlinks
chown -Rh grafana:grafana $HOME


