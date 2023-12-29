DESCRIPTION = "Create a release list"
SECTION = "tools"
LICENSE = "CLOSED"
DEPENDS += "virtual/kernel"
DEPENDS += "notes"
DEPENDS += "python3-native"

TRACKED_RECIPES = "version notes"
TRACKED_MODULES = ""
SRC_URI = ""

S = "${WORKDIR}"

inherit allarch

do_compile() {
    local TRACKED_RECIPES="${TRACKED_RECIPES}"
    # Extract the kernel version
    KERNEL_VERSION=$(find ${TOPDIR}/tmp/work/imx8mqevk-poky-linux/linux-imx/ -maxdepth 1 -type d -name '5.15.71*' | sed 's/.*\///')

    # Validate the extracted kernel version
    if [ -z "${KERNEL_VERSION}" ] || [[ ! "${KERNEL_VERSION}" == *"5.15.71+"* ]]; then
        bbfatal "Kernel version does not match the expected pattern (5.15.71+): ${KERNEL_VERSION}"
    fi

    # Create the version list file with the kernel version
    echo "kernel version=$KERNEL_VERSION" > ${S}/release_list.conf

    for recipe in $TRACKED_RECIPES; do
        # Corrected the RECIPE_INSTALL_DIR path
        RECIPE_INSTALL_DIR="${TOPDIR}/tmp/work/cortexa53-crypto-poky-linux/$recipe/1.0-r0/image"

        # Check the existence of the directory
        if [ ! -d "$RECIPE_INSTALL_DIR" ]; then
            bbwarn "The identified install directory does not exist: $RECIPE_INSTALL_DIR"
            continue
        fi

        # Traverse the installed files
        find "$RECIPE_INSTALL_DIR" -type f | while read fullpath; do
            # Get the relative path
            relative_path=${fullpath#${RECIPE_INSTALL_DIR}}
            
            bbnote "Processing file: $relative_path"
            checksum=$(/usr/bin/xxh32sum "$fullpath" | awk '{print $1}')
            echo "$relative_path=$checksum" >> ${S}/release_list.conf
        done
    done

    # Process TRACKED_MODULES
    local TMP_KERNEL_MOD_DIR="${WORKDIR}/tmp_kernel_modules"
    mkdir -p ${TMP_KERNEL_MOD_DIR}
    tar -xzf ${TOPDIR}/tmp/deploy/images/imx8mqevk/modules-imx8mqevk.tgz -C ${TMP_KERNEL_MOD_DIR}

    for module_name in ${TRACKED_MODULES}; do
        local module_path=$(find ${TMP_KERNEL_MOD_DIR}/lib/modules/${KERNEL_VERSION}/ -name "${module_name}")
        if [[ -f "${module_path}" ]]; then
            checksum=$(/usr/bin/xxh32sum "${module_path}" | awk '{print $1}')
            echo "/etc/modules/${module_name}=${checksum}" >> ${S}/release_list.conf
        else
            bbwarn "Module ${module_name} not found in ${TMP_KERNEL_MOD_DIR}"
        fi
    done

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

    # Create and set permissions for /etc/modules directory
    install -d ${D}/etc/modules
    # Copy each tracked module to /etc/modules
    local TMP_KERNEL_MOD_DIR="${WORKDIR}/tmp_kernel_modules"
    bbnote "Looking for modules in ${TMP_KERNEL_MOD_DIR}"

    # Debugging: List contents of TMP_KERNEL_MOD_DIR
    bbnote "Contents of TMP_KERNEL_MOD_DIR:"
    ls -lR ${TMP_KERNEL_MOD_DIR} || bbwarn "Failed to list contents of ${TMP_KERNEL_MOD_DIR}"

    for module_name in ${TRACKED_MODULES}; do
        bbnote "Searching for module: ${module_name}"
        local module_path=$(find ${TMP_KERNEL_MOD_DIR} -name "${module_name}")
        bbnote "Module path resolved to: ${module_path}"

        if [[ -f "${module_path}" ]]; then
            bbnote "Installing module ${module_name} to /etc/modules"
            install -m 0644 "${module_path}" "${D}/etc/modules/${module_name}"
        else
            bbwarn "Module ${module_name} not found at ${module_path}, skipping installation"
        fi
    done
    rm -rf ${TMP_KERNEL_MOD_DIR}
}


FILES_${PN} += "/etc/release_list.conf"


FILES_${PN} += "/etc/release_list.conf"
