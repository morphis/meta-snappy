SUMMARY = "Golang (Go) bindings for GNU's gettext"
HOMEPAGE = "https://github.com/jessevdk/go-flags"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7eb88a85dd46ad8cc80347c7085c6551"

PKG_NAME = "github.com/gosexy/gettext"
SRC_URI = "git://github.com/gosexy/gettext;protocol=https"

SRCREV = "305f360aee30243660f32600b87c3c1eaa947187"
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
