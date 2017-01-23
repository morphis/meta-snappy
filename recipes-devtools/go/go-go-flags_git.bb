DESCRIPTION = "Go library for parsing command line arguments"
HOMEPAGE = "https://github.com/jessevdk/go-flags"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=808b70f61299cac3989a0b3f6162c93d"

PKG_NAME = "github.com/jessevdk/go-flags"
SRC_URI = "git://github.com/jessevdk/go-flags;protocol=https"

SRCREV = "6b9493b3cb60367edd942144879646604089e3f7"
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
