SUMMARY = "Support executable to apply confinement for snappy apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.39+gitr${SRCPV}"

SRC_URI = " \
    git://github.com/snapcore/snap-confine;protocol=https \
    file://0001-Disable-doc-generation.patch \
"
SRCREV = "df11aa5c9dfe2979b08e5ca7c0f7ac50085918af"

S = "${WORKDIR}/git"

DEPENDS += "udev"

EXTRA_OECONF += " \
	--disable-apparmor \
	--disable-seccomp \
"

inherit autotools pkgconfig

FILES_${PN} += "${baselib}/udev/snappy-app-dev"