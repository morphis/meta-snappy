SUMMARY = "Support executable to apply confinement for snappy apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/snapd-${PV}/COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "https://github.com/snapcore/snapd/releases/download/${PV}/snapd_${PV}.tar.xz"

SRC_URI[md5sum] = "e0b136667007ffee4efab0628d27d34f"
SRC_URI[sha256sum] = "14c9f95cea4f4a8b5dda8b6a2710b8b756e4caf373d51b1155199813860a5e0b"

BASE_DIR = "${WORKDIR}/snapd-${PV}"
S = "${BASE_DIR}/cmd"

DEPENDS += "udev glib-2.0 xfsprogs"

EXTRA_OECONF += " \
	--disable-apparmor \
	--disable-seccomp \
	--libexecdir=/usr/lib/snapd \
"

inherit autotools pkgconfig

do_configure_prepend() {
	(cd ${BASE_DIR} ; ./mkversion.sh)
}

FILES_${PN} += " \
	${baselib}/udev/snappy-app-dev \
	/usr/lib/snapd/snap-confine \
	/usr/lib/snapd/snap-discard-ns \
"
