SUMMARY = "The snapd and snap tools enable systems to work with .snap files."
HOMEPAGE = "https://www.snapcraft.io"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}/COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "									\
	https://${GO_IMPORT}/releases/download/${PV}/snapd_${PV}.vendor.tar.xz	\
"

SRC_URI[md5sum] = "4271c51bb2f0619f9e29d5b438a22196"
SRC_URI[sha256sum] = "16bf669bfb13eda5a4b3a36787696e630102de5b891ed37aeb7caf42ec508f4d"

GO_IMPORT = "github.com/snapcore/snapd"

SHARED_GO_INSTALL = "				\
	${GO_IMPORT}/cmd/snap		\
	${GO_IMPORT}/cmd/snapd		\
	${GO_IMPORT}/cmd/snapctl	\
	${GO_IMPORT}/cmd/snap-seccomp	\
	"

STATIC_GO_INSTALL = " \
	${GO_IMPORT}/cmd/snap-exec		\
	${GO_IMPORT}/cmd/snap-update-ns		\
"

GO_INSTALL = "${SHARED_GO_INSTALL}"

DEPENDS += "			\
	go-cross-${TARGET_ARCH}	\
	glib-2.0		\
	udev			\
	xfsprogs		\
	libseccomp \
"

RDEPENDS_${PN} += "		\
	ca-certificates		\
	kernel-module-squashfs	\
"

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECONF += "			\
	--disable-apparmor		\
	--enable-seccomp		\
	--libexecdir=${libdir}/snapd	\
	--with-snap-mount-dir=/snap \
"

inherit systemd autotools pkgconfig go

# Our tools build with autotools are inside the cmd subdirectory
# and we need to tell the autotools class to look in there.
AUTOTOOLS_SCRIPT_PATH = "${S}/cmd"

SYSTEMD_SERVICE_${PN} = "snapd.service"

do_configure_prepend() {
	(cd ${S} ; ./mkversion.sh ${PV})
}

# The go class does export a do_configure function, of which we need
# to change the symlink set-up, to target snapd's environment.
do_configure() {
	mkdir -p ${B}/src/github.com/snapcore
	ln -snf ${S} ${B}/src/${GO_IMPORT}
	autotools_do_configure
}

do_compile() {
	go_do_compile
	# these *must* be built statically
	${GO} install -v \
		-ldflags="${GO_RPATH} -extldflags '${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} ${GO_RPATH_LINK} ${LDFLAGS} -static'" \
		${GO_IMPORT}/cmd/snap-update-ns \
		${GO_IMPORT}/cmd/snap-exec

  # build the rest
  autotools_do_compile
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
	install -d ${D}${sysconfdir}/profile.d

	oe_runmake -C ${B} install DESTDIR=${D}
	oe_runmake -C ${S}/data/systemd install \
		DESTDIR=${D} \
		BINDIR=${bindir} \
		LIBEXECDIR=${libdir} \
		SYSTEMDSYSTEMUNITDIR=${systemd_system_unitdir} \
		SNAP_MOUNT_DIR=/snap \
		SNAPD_ENVIRONMENT_FILE=${sysconfdir}/default/snapd

	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snapd ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-exec ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-seccomp ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-update-ns ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap ${D}${bindir}
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snapctl ${D}${bindir}

	echo "PATH=\$PATH:/snap/bin" > ${D}${sysconfdir}/profile.d/20-snap.sh
}

RDEPENDS_${PN} += "squashfs-tools"
FILES_${PN} += "			\
	${systemd_unitdir}/system/	\
	/var/lib/snapd			\
	/var/snap			\
	${baselib}/udev/snappy-app-dev	\
"

# ERROR: snapd-2.23.5-r0 do_package_qa: QA Issue: No GNU_HASH in the elf binary:
# '.../snapd/usr/lib/snapd/snap-exec' [ldflags]
INSANE_SKIP_${PN} = "ldflags"
