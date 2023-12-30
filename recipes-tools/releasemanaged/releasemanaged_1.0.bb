DESCRIPTION = "Release Managed Software"
SECTION = "apps"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8212b53396d3c0dbfab7ac8bac577c34"
SRC_URI = "git://github.com/deadpoolcode1/kratos_version_managed.git;protocol=https"
SRCREV = "f74eaf38dbda441df4fcaeb21ca4465957953a2f"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS += "xxhash"

inherit cmake pkgconfig

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/release_check.bin ${D}${bindir}
}

FILES_${PN} = "${bindir}/release_check.bin"
