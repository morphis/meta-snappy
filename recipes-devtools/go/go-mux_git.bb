DESCRIPTION = "A powerful URL router and dispatcher for golang."
HOMEPAGE = "https://github.com/gorilla/mux"
SECTION = "devel/go"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c50f6bd9c1e15ed0bad3bea18e3c1b7f"

SRCNAME = "mux"

PKG_NAME = "github.com/gorilla/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git"

SRCREV = "ee1815431e497d3850809578c93ab6705f1a19f7"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "go_mux_sysroot_preprocess"

go_mux_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
