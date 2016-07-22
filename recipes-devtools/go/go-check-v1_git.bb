DESCRIPTION = "Rich testing extension for Go's testing package"
HOMEPAGE = "http://labix.org/gocheck"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=598d6673548efc92a7d6dfb739add1ed"

SRCNAME = "gocheck"

PKG_NAME = "gopkg.in/check.v1"
SRC_URI = "git://github.com/go-check/check;protocol=https;branch=v1"

SRCREV = "4f90aeace3a26ad7021961c297b22c42160c7b25"
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
