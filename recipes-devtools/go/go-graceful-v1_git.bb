SUMMARY = "Graceful is a Go package enabling graceful shutdown of an http.Handler server."
HOMEPAGE = "https://github.com/tylerb/graceful/"
SECTION = "devel/go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbfc11751fc656016325b178b82069d"

PKG_NAME = "github.com/tylerb/graceful"
SRC_URI = "git://${PKG_NAME};protocol=https"

SRCREV = "50a48b6e73fcc75b45e22c05b79629a67c79e938"
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
