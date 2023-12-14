#! /bin/sh

set --

if [ -e /dev/gpio/digital_input_1 ]; then
    set -- /dev/gpio/digital_input_*
fi

if [ -d /run/dioext ]; then
    for f in $(find /run/dioext -name pins.conf); do
        set -- "$@" --conf $f
    done
fi

if [ -z "$*" ]; then
    svd -d .
    exit 0
fi

exec $(dirname $0)/dbus_digitalinputs.py "$@"
