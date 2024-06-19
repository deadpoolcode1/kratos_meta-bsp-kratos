DESCRIPTION = "bit application"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=6f2e58f9db926d1d557f69da36ac4b7c"

SRC_URI = "file://TestClient_evb \
           file://TestServer_evb"

S = "${WORKDIR}"

do_compile() {
    # No compilation needed since we are using precompiled binaries
    :
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/TestClient_evb ${D}${bindir}/TestClient_evb
    install -m 0755 ${WORKDIR}/TestServer_evb ${D}${bindir}/TestServer_evb
}

FILES_${PN} = "${bindir}/TestClient_evb ${bindir}/TestServer_evb"

