FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://hostname"

do_install:append() {
    install -m 0644 ${WORKDIR}/hostname ${D}${sysconfdir}/hostname
}
