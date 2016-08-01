SUMMARY = "A small demo image to demo Snappy running on OpenEmbedded/Yocto"

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    ${ROOTFS_PKGMANAGE_BOOTSTRAP} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    snapd \
    snap-confine \
    connman \
    ca-certificates \
    kernel-module-squashfs \
    bash \
    rpm \
"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "819200"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
