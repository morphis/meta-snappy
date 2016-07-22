SUMMARY = "The tomb package helps with clean goroutine termination in the Go language."
HOMEPAGE = "https://github.com/gorilla/websocket"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c007b54a1743d596f46b2748d9f8c044"

PKG_NAME = "github.com/gorilla/websocket"
SRC_URI = "git://github.com/gorilla/websocket;protocol=https"

SRCREV = "5e2e56d5dfd46884df1036f828777ee6273f2cff"
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
