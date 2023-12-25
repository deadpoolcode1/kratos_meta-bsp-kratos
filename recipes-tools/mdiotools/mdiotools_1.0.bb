DESCRIPTION = "MDIO tool for direct manipulation of MDIO registers"
SECTION = "net"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/wkz/mdio-tools.git;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig autotools-brokensep

# Ensure libmnl and its development headers are included
DEPENDS = "libmnl pkgconfig-native"


do_configure() {
    export PKG_CONFIG="$(which pkg-config)"
    oe_runconf
}

do_compile() {
    oe_runmake
}

do_configure:prepend() {
    autoreconf -vfi
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

FILES_${PN} += "${bindir}/mdio-tool"
