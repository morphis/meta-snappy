SUMMARY = "YAML support for the Go language."
HOMEPAGE = "https://github.com/go-yaml/yaml/"
SECTION = "devel/go"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6964839e54f4fefcdae13f22b92d0fbb"

PKG_NAME = "gopkg.in/yaml.v2"
SRC_URI = "git://github.com/go-yaml/yaml;protocol=https;branch=v2"

SRCREV = "e4d366fc3c7938e2958e662b4258c7a89e1f0e3e"
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
