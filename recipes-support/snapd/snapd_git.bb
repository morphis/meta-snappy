SUMMARY = "The snapd and snap tools enable systems to work with .snap files."
HOMEPAGE = "https://www.snapcraft.io"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "2.21"

SRC_URI = " \
  git://github.com/snapcore/snapd.git;protocol=https;branch=release/${PV} \
"
# Matches the 2.21 tag
SRCREV = "4e8ba630dcc8921db86eb5f3c53d8bab8d139a70"

SNAPD_PKG="github.com/snapcore/snapd"

DEPENDS = " \
	go-cross \
	go-crypto \
	go-check-v1 \
	go-context \
	go-go-flags \
	go-graceful-v1 \
	go-mux \
	go-gettext \
	go-goconfigparser \
	go-liner \
	go-pty \
	go-pb \
	go-systemd \
	go-uboot \
	go-yaml-v2 \
	go-macaroon-v1 \
	go-retry-v1 \
	go-tomb-v2 \
	go-websocket \
	go-x-net \
"

RDEPENDS_${PN} += " \
	ca-certificates \
	kernel-module-squashfs \
"

S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_SERVICE_${PN} = "snapd.service"

do_compile() {
	export GOARCH="${TARGET_ARCH}"
	# supported amd64, 386, arm arm64
	if [ "${TARGET_ARCH}" = "x86_64" ]; then
		export GOARCH="amd64"
	fi
	if [ "${TARGET_ARCH}" = "aarch64" ]; then
		export GOARCH="arm64"
	fi
	if [ "${TARGET_ARCH}" = "i586" ]; then
		export GOARCH="386"
	fi

	# Set GOPATH. See 'PACKAGERS.md'. Don't rely on
	# docker to download its dependencies but rather
	# use dependencies packaged independently.
	cd ${S}
	rm -rf .gopath
	mkdir -p .gopath/src/"$(dirname "${SNAPD_PKG}")"
	ln -sf ../../../.. .gopath/src/"${SNAPD_PKG}"
	export GOPATH="${S}/.gopath:${S}/vendor:${STAGING_DIR_TARGET}/${prefix}/local/go"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"
	cd -

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	rm -rf ${B}/build
	mkdir ${B}/build
	go build -a -v -o ${B}/build/snapd ${SNAPD_PKG}/cmd/snapd
	go build -a -v -o ${B}/build/snap ${SNAPD_PKG}/cmd/snap
	go build -a -v -o ${B}/build/snapctl ${SNAPD_PKG}/cmd/snapctl
	go build -a -v -o ${B}/build/snap-exec ${SNAPD_PKG}/cmd/snap-exec
}

do_install() {
	install -d ${D}${libdir}/snapd
	install -d ${D}${bindir}
	install -d ${D}${systemd_unitdir}/system
	install -d ${D}/var/lib/snapd
	install -d ${D}/var/lib/snapd/snaps
	install -d ${D}/var/lib/snapd/lib/gl
	install -d ${D}/var/lib/snapd/desktop
	install -d ${D}/var/lib/snapd/environment
	install -d ${D}/var/snap
	install -d ${D}/${sysconfdir}

	# NOTE: This file needs to be present to allow snapd's service
	# units to startup.
	touch ${D}/${sysconfdir}/environment

	install -m 0644 ${S}/debian/snapd.refresh.timer ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/debian/snapd.refresh.service ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/debian/snapd.service ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/debian/snapd.socket ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/debian/snapd.autoimport.service ${D}${systemd_unitdir}/system

	install -m 0755 ${B}/build/snapd ${D}${libdir}/snapd/
	install -m 0755 ${B}/build/snap-exec ${D}${libdir}/snapd/
	install -m 0755 ${B}/build/snap ${D}${bindir}
	install -m 0755 ${B}/build/snapctl ${D}${bindir}
}

RDEPENDS_${PN} += "squashfs-tools"
FILES_${PN} += "${systemd_unitdir}/system/ /var/lib/snapd /var/snap"

# ERROR: snapd-2.21-r0 do_package_qa: QA Issue: No GNU_HASH in the elf binary:
# '.../snapd/usr/lib/snapd/snap-exec' [ldflags]
INSANE_SKIP_${PN} = "ldflags"
