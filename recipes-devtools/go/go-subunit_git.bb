SUMMARY = "Go implementation of subunit"
HOMEPAGE = "https://github.com/testing-cabal/subunit-go"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://BSD;md5=a64983c752e68e915cb4ec5ab6c2cc75"

PKG_NAME = "github.com/testing-cabal/subunit-go"
SRC_URI = "git://github.com/testing-cabal/subunit-go;protocol=https"

SRCREV = "00b258565a5cf3adaa24b68d31c9e6ec3d2cdbe7"
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
