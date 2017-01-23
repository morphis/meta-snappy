DESCRIPTION = "Simple retry mechanism for Go"
HOMEPAGE = "https://github.com/go-retry/retry/"
SECTION = "devel/go"
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0ea265b928b7cb9d71b994f1d4e1ac21"

PKG_NAME = "gopkg.in/retry.v1"
SRC_URI = "git://github.com/go-retry/retry;branch=v1"

SRCREV = "c09f6b86ba4d5d2cf5bdf0665364aec9fd4815db"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "go_pty_sysroot_preprocess"

go_pty_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
