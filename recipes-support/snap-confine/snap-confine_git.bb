SUMMARY = "Support executable to apply confinement for snappy apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "2.21+gitr${SRCPV}"

SRC_URI = "git://github.com/snapcore/snapd;protocol=https"
SRCREV = "4e8ba630dcc8921db86eb5f3c53d8bab8d139a70"

S = "${WORKDIR}/git/cmd"

DEPENDS += "udev glib-2.0"

EXTRA_OECONF += " \
	--disable-apparmor \
	--disable-seccomp \
	--libexecdir=/usr/lib/snapd \
"

inherit autotools pkgconfig

do_configure_prepend() {
	(cd ${WORKDIR}/git ; ./mkversion.sh)
}

FILES_${PN} += " \
	${baselib}/udev/snappy-app-dev \
	/usr/lib/snapd/snap-confine \
	/usr/lib/snapd/snap-discard-ns \
"
