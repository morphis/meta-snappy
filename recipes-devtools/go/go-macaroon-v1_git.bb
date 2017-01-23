SUMMARY = "A native Go implementation of macaroons"
HOMEPAGE = "https://github.com/go-macaroon/macaroon"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f2ade4e207e03454ad3e610a00c0ef15"

PKG_NAME = "gopkg.in/macaroon.v1"
SRC_URI = "git://github.com/go-macaroon/macaroon;protocol=https;branch=v1"

SRCREV = "ab3940c6c16510a850e1c2dd628b919f0f3f1464"
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
