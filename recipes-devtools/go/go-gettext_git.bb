SUMMARY = "Golang (Go) bindings for GNU's gettext"
HOMEPAGE = "https://github.com/jessevdk/go-flags"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=32424f50f6f3c203a94ee4027ce44e1f"

PKG_NAME = "github.com/mvo5/gettext.go"
SRC_URI = "git://${PKG_NAME};protocol=https"

SRCREV = "da4fdf605f1b0e2aa523423db5c0a3f727d62019"
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
