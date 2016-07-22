SUMMARY = "Support executable to apply confinement for snappy apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5="

inherit autotools

DEPENDS += "udev"

EXTRA_OECONF += " \
	--disable-apparmor \
	--disable-seccomp \
"
