SUMMARY = "The tomb package helps with clean goroutine termination in the Go language."
HOMEPAGE = "https://github.com/go-tomb/tomb"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95d4102f39f26da9b66fee5d05ac597b"

PKG_NAME = "gopkg.in/tomb.v2"
SRC_URI = "git://github.com/go-tomb/tomb;protocol=https;branch=v2"

SRCREV = "d5d1b5820637886def9eef33e03a27a9f166942c"
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
