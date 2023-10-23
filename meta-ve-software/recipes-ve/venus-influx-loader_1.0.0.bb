SUMMARY = "server to extract data from VE systems via MQTT and save in a DB" 
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=18cf85f0699444c9035983de5aa98dcd"

RDEPENDS:${PN} += "bash nodejs-npm util-linux-setpriv"

SRC_URI = " \
    gitsm://github.com/victronenergy/venus-influx-loader.git;branch=main;protocol=https \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://config.json \
    file://prepare-venus-influx-loader.sh \
"
# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRCREV = "536c20726786788bade1defb536e34fe4fc7b190"
S = "${WORKDIR}/git"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-venus-influx-loader.sh && exec setpriv --init-groups --reuid vserver --regid vserver ${bindir}/venus-influx-loader -c /data/conf/venus-influx-loader"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/venus-influx-loader -r -p '*' -s /bin/false -G vserver vserver"

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"
PACKAGE = "venus-influx-loader"

do_compile() {
    export HOME=${WORKDIR}

    if [ ! -f ${S}/npm-shrinkwrap.json ]; then
        bbfatal "No npm-shrinkwrap.json found for ${PN}"
    fi
    
    npm install \
        --prefix="${S}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        ${S}

    tar=$(npm pack ${S})

    npm install \
        --global \
        --prefix="${NPM_BUILD}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        --production \
        "$tar"
}


do_install:append() {
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/config
#    rm ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-compose-dev.yaml 
#    rm ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-compose-playground.yaml
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-grafana
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-server
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-upnp

#    cp -r ${S}/dist ${D}${nonarch_libdir}/node_modules/venus-docker-grafana-server

    install -d ${DEFAULTS}
    install -m 0644 ${WORKDIR}/config.json ${DEFAULTS}

    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-venus-influx-loader.sh ${D}${bindir}
}
