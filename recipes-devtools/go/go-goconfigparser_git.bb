SUMMARY = "Golang implementation of Python ConfigParser"
HOMEPAGE = "https://github.com/mvo5/goconfigparser"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=a64983c752e68e915cb4ec5ab6c2cc75"

PKG_NAME = "github.com/mvo5/goconfigparser"
SRC_URI = "git://github.com/mvo5/goconfigparser;protocol=https"

SRCREV = "efba97014b80c1d34ead4f3bbafc5d8effe4d2b0"
PV = "0.0+git${SRCREV}"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "go_check_sysroot_preprocess"

go_check_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
