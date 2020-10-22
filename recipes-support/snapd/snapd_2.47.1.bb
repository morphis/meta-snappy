SUMMARY = "The snapd and snap tools enable systems to work with .snap files."
HOMEPAGE = "https://www.snapcraft.io"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}/COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "									\
	https://${GO_IMPORT}/releases/download/${PV}/snapd_${PV}.vendor.tar.xz	\
"

SRC_URI[md5sum] = "d6de9d882b3d3b219d637a028b2c2279"
SRC_URI[sha256sum] = "4f9666cd15d588017d4287aafdb3e7855748509afaa3002e6d149db1424e032f"

GO_IMPORT = "github.com/snapcore/snapd"

SHARED_GO_INSTALL = "				\
	${GO_IMPORT}/cmd/snap		\
	${GO_IMPORT}/cmd/snapd		\
	${GO_IMPORT}/cmd/snap-seccomp	\
	${GO_IMPORT}/cmd/snap-failure	\
	"

STATIC_GO_INSTALL = " \
	${GO_IMPORT}/cmd/snap-exec		\
	${GO_IMPORT}/cmd/snap-update-ns		\
	${GO_IMPORT}/cmd/snapctl		\
"

GO_INSTALL = "${SHARED_GO_INSTALL}"

DEPENDS += "			\
	glib-2.0		\
	udev			\
	xfsprogs		\
	libseccomp \
"

RDEPENDS_${PN} += "		\
	ca-certificates		\
	kernel-module-squashfs	\
	bash \
"

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECONF += "			\
	--disable-apparmor		\
	--libexecdir=${libdir}/snapd	\
	--with-snap-mount-dir=/snap \
"

inherit systemd autotools pkgconfig go

# disable shared runtime for x86
# https://forum.snapcraft.io/t/yocto-rocko-core-snap-panic/3261
# GO_DYNLINK is set with arch overrides in goarch.bbclass
GO_DYNLINK_x86 = ""
GO_DYNLINK_x86-64 = ""
GO_DYNLINK_arm = ""
GO_DYNLINK_aarch64 = ""

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
	mkdir -p ${S}/src/github.com/snapcore
	ln -snf ${S} ${S}/src/${GO_IMPORT}
	go_do_configure
	autotools_do_configure
}

do_compile() {
	go_do_compile
	# these *must* be built statically
	for prog in ${STATIC_GO_INSTALL}; do
		${GO} install -v \
		        -ldflags="${GO_RPATH} -extldflags '${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} ${GO_RPATH_LINK} ${LDFLAGS} -static'" \
		        $prog
	done

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
	install -d ${D}${systemd_unitdir}/system-generators

	oe_runmake -C ${B} install DESTDIR=${D}
	oe_runmake -C ${S}/data/systemd install \
		DESTDIR=${D} \
		BINDIR=${bindir} \
		LIBEXECDIR=${libdir} \
		SYSTEMDSYSTEMUNITDIR=${systemd_system_unitdir} \
		SNAP_MOUNT_DIR=/snap \
		SNAPD_ENVIRONMENT_FILE=${sysconfdir}/default/snapd

	# systemd system-environment-generators directory is not handled with a
	# varaible in systemd.pc so the build code does an educated guess of using
	# ${prefix}/lib/systemd/system-environment-generators which ends up as
	# /usr/lib/systemd/.., but we want /lib/systemd/..
	cp -av ${D}${prefix}${systemd_unitdir}/system-environment-generators \
	   ${D}${systemd_unitdir}
	rm -rf ${D}${prefix}${systemd_unitdir}

	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snapd ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-exec ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-seccomp ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-update-ns ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snapctl ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap-failure ${D}${libdir}/snapd/
	install -m 0755 ${B}/${GO_BUILD_BINDIR}/snap ${D}${bindir}
	ln -s ${libdir}/snapd/snapctl ${D}${bindir}/snapctl

	echo "PATH=\$PATH:/snap/bin" > ${D}${sysconfdir}/profile.d/20-snap.sh

	# ubuntu-core-launcher is dead
	rm -fv ${D}${bindir}/ubuntu-core-launcher
	# drop unnecessary units
	rm -fv ${D}${systemd_unitdir}/system/snapd.system-shutdown.service
	rm -fv ${D}${systemd_unitdir}/system/snapd.snap-repair.*
	rm -fv ${D}${systemd_unitdir}/system/snapd.core-fixup.*
	# and related scripts
	rm -fv ${D}${libdir}/snapd/snapd.core-fixup.sh
}

RDEPENDS_${PN} += "squashfs-tools"
FILES_${PN} += "                                    \
	${systemd_unitdir}/system/                        \
	${systemd_unitdir}/system-generators/             \
	${systemd_unitdir}/system-environment-generators/	\
	/var/lib/snapd                                    \
	/var/snap                                         \
	${baselib}/udev/snappy-app-dev                    \
"
