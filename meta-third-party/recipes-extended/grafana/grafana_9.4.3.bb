DESCRIPTION = "Grafana Go Backend"
HOMEPAGE = "https://grafana.com"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

RDEPENDS:${PN} += "bash expect"
RDEPENDS:${PN}-dev += "bash"

SRC_URI = "\
      https://dl.grafana.com/oss/release/${BPN}-${PV}.linux-armv7.tar.gz \
      file://grafana.ini \
      file://grafana.db \
      file://prepare-grafana.sh \
      file://start-grafana.sh \
"

#      file://0001-fixup-pkg-server-wireexts_oss.go.patch;patchdir=src/${GO_IMPORT} 
SRC_URI[sha256sum]="f1003e88d6d57ec12998ee6dbc13d73015f78734dbe9e9cb63d3b273c6c0f14d"

inherit daemontools useradd pkgconfig 

# Workaround for network access issue during compile step
# this needs to be fixed in the recipes buildsystem to move
# this such that it can be accomplished during do_fetch task
do_compile[network] = "1"

DAEMONTOOLS_SCRIPT = "export HOME=/data/home/grafana && ${bindir}/prepare-grafana.sh && exec setpriv --init-groups --reuid grafana --regid grafana ${bindir}/start-grafana.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/home/grafana -r -p '*' -s /bin/false -G grafana grafana"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-grafana.sh ${D}${bindir}/
    install -m 0755 ${WORKDIR}/start-grafana.sh ${D}${bindir}/
    install -m 0755 ${S}/bin/grafana ${D}${bindir}/grafana
    install -m 0755 ${S}/bin/grafana-cli ${D}${bindir}/grafana-cli
    install -m 0755 ${S}/bin/grafana-server ${D}${bindir}/grafana-server

    # This may need moving if it needs to be written to as /etc is Readonly
    install -d ${D}${sysconfdir}/grafana
    install -m 0644 ${WORKDIR}/grafana.ini ${D}${sysconfdir}/grafana/grafana.ini

    for d in access-control alerting dashboards datasources notifiers plugins
    do
        # This may need moving if it needs to be written to as /etc is Readonly
        install -d ${D}${datadir}/grafana/initial/provisioning/${d}
        install -m 0644 ${S}/conf/provisioning/${d}/sample.yaml ${D}${datadir}/grafana/initial/provisioning/${d}/sample.yaml
    done

    # install frontend
    install -d ${D}${datadir}/grafana

    cp -R --no-dereference --preserve=mode,links -v \
      ${S}/public \
      ${D}${datadir}/grafana/

    cp -R --no-dereference --preserve=mode,links -v \
      ${S}/conf \
      ${D}${datadir}/grafana/

    cp -R --no-dereference --preserve=mode,links -v \
      ${S}/plugins-bundled \
      ${D}${datadir}/grafana/

    cp -R --no-dereference --preserve=mode,links -v \
      ${S}/LICENSE \
      ${D}${datadir}/grafana/

    cp -R --no-dereference --preserve=mode,links -v \
      ${S}/VERSION \
      ${D}${datadir}/grafana/

    # Install initial grafana.db
    install -d ${D}${datadir}/grafana/initial
    install -m 0640 ${WORKDIR}/grafana.db ${D}${datadir}/grafana/initial

}

# explicitly tell do_package not to strip those files
INHIBIT_PACKAGE_STRIP_FILES = "\
     ${PKGD}${bindir}/grafana \
     ${PKGD}${bindir}/grafana-cli \
     ${PKGD}${bindir}/grafana-server \
"
