SRCBRANCH = "lf-5.15.y"
KERNEL_SRC ?= "git://github.com/nxp-imx/linux-imx.git;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://0001-removed-promisc-check-on-eth1.patch \
            file://0002-named-gpio.patch \
	    file://0003-embedded-definitions.patch"
