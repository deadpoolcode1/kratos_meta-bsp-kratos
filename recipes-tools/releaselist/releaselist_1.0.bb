DESCRIPTION = "Create a release list"
SECTION = "tools"
LICENSE = "CLOSED"
DEPENDS += "virtual/kernel"
DEPENDS += "notes"
DEPENDS += "python3-native"

TRACKED_RECIPES = "version notes"
SRC_URI = ""

S = "${WORKDIR}"

inherit allarch

do_compile() {
    # Extract the kernel version
    KERNEL_VERSION=$(find ${TOPDIR}/tmp/work/imx8mqevk-poky-linux/linux-imx/ -maxdepth 1 -type d -name '5.15.71*' | sed 's/.*\///')

    # Create the version list file with the kernel version
    echo "kernel version=$KERNEL_VERSION" > ${S}/release_list.conf

    bbnote "Final configuration:\n$(cat ${S}/release_list.conf)"
}

do_print_release_list() {
    local release_list_file="${TOPDIR}/tmp/work/imx8mqevk-poky-linux/releaselist/1.0-r0/release_list.conf"

    if [ -f "${release_list_file}" ]; then
        bbwarn "Contents of release_list.conf:"
        while IFS= read -r line; do
            bbwarn "${line}"
        done < "${release_list_file}"
    else
        bbwarn "release_list.conf not found at ${release_list_file}"
    fi
}

addtask print_release_list after do_compile before do_install

do_install() {
    # Create and set permissions for /etc directory
    install -d ${D}/etc
    # Install the release_list.conf file to /etc
    install -m 0644 ${S}/release_list.conf ${D}/etc/release_list.conf
}

FILES_${PN} += "/etc/release_list.conf"
