DESCRIPTION = "kratos bit utility"
SECTION = "examples"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://github.com/deadpoolcode1/kratos_bit.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS += "jsoncpp"

do_compile() {
    oe_runmake CXXFLAGS="${CXXFLAGS} -I${STAGING_INCDIR}/jsoncpp" LDFLAGS="${LDFLAGS} -ljsoncpp"
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 TestServer_host ${D}${bindir}/TestServer
    install -m 0755 TestClient_host ${D}${bindir}/TestClient
}

