DESCRIPTION = "Maintenance service"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=6f2e58f9db926d1d557f69da36ac4b7c"

SRC_URI = "file://maintenance.c \
           file://maintenance.service \
           file://maintenance.sh"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "maintenance.service"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o maintenance maintenance.c
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 maintenance ${D}${bindir}

    install -d ${D}${bindir}
    install -m 0755 maintenance.sh ${D}${bindir}/maintenance.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/maintenance.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += "${systemd_system_unitdir}/maintenance.service"

