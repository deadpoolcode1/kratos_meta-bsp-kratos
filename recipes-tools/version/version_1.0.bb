SUMMARY = "Version indicator"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://version.sh"

S = "${WORKDIR}"

do_compile() {
    :
}

do_install() {
    install -d ${D}/usr/bin
    install -m 0755 ${WORKDIR}/version.sh ${D}/usr/bin
}
