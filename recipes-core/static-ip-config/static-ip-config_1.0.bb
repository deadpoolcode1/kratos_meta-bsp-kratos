SUMMARY = "Static IP configuration for eth0"
DESCRIPTION = "Configure eth0 with a static IP address and IgnoreCarrierLoss"
LICENSE = "CLOSED"
PR = "r0"

SRC_URI = "file://10-static-eth0.network \
	   file://10-static-eth1.network \
	   file://10-static-eth2.network"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/10-static-eth0.network ${D}${sysconfdir}/systemd/network/
    
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/10-static-eth1.network ${D}${sysconfdir}/systemd/network/
    
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/10-static-eth2.network ${D}${sysconfdir}/systemd/network/
}

FILES_${PN} += "${sysconfdir}/systemd/network/10-static-eth0.network"
FILES_${PN} += "${sysconfdir}/systemd/network/10-static-eth1.network"
FILES_${PN} += "${sysconfdir}/systemd/network/10-static-eth2.network"

