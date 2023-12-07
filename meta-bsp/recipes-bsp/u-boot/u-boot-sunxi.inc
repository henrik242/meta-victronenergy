LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v17"

GIT_BRANCH = "mans/nucleo"

SRC_URI = "git://github.com/victronenergy/u-boot.git;protocol=https;branch=${GIT_BRANCH}"
SRCREV = "d59423bff24bb03805d23ab4cd5ae2ac70c3ffe3"
S = "${WORKDIR}/git"
