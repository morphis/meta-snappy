FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://0001-UBUNTU-SAUCE-no-up-apparmor-remove-security-apparmor.patch \
	file://0002-UBUNTU-SAUCE-no-up-apparmor-Sync-to-apparmor3-RC1-sn.patch \
	file://0003-UBUNTU-SAUCE-apparmor-backport-setup-base-backport-f.patch \
	file://0004-UBUNTU-SAUCE-apparmor-3.12-backport-kvfree-reintrodu.patch \
	file://0005-UBUNTU-SAUCE-apparmor-3.12-backport-mtd-Move-major-n.patch \
	file://0006-UBUNTU-SAUCE-apparmor-3.11-backport-revert-module-ls.patch \
	file://0007-UBUNTU-SAUCE-Revert-fix-only-allow-a-single-threaded.patch \
	file://0008-UBUNTU-SAUCE-no-up-fix-bad-unix_addr_fs-macro.patch \
	file://0009-UBUNTU-apparmor-follow-change-to-this_cpu_ptr.patch \
	file://0010-UBUNTU-SAUCE-no-up-apparmor-fix-mediation-of-fs-unix.patch \
	file://0011-UBUNTU-SAUCE-no-up-apparmor-fix-mount-not-handling-d.patch \
	file://0012-sched-move-no_new_privs-into-new-atomic-flags.patch \
	file://0013-kbuild-trivial-remove-trailing-empty-lines.patch \
	file://0014-kbuild-remove-redundant-clean-files-from-scripts-kco.patch \
	file://0015-kconfig-nconfig-fix-multi-byte-UTF-handling.patch \
	file://0016-x86-platform-kvm-kconfig-Turn-existing-.config-s-int.patch \
	file://0017-x86-platform-Add-kvmconfig-to-the-phony-targets.patch \
	file://0018-x86-platform-Fix-make-O-dir-kvmconfig.patch \
	file://0019-x86-platform-kconfig-move-kvmconfig-functionality-to.patch \
	file://0020-x86-Add-make-tinyconfig-to-configure-the-tiniest-pos.patch \
	file://0021-kbuild-mergeconfig-fix-jobserver-unavailable-warning.patch \
	file://0022-kbuild-mergeconfig-move-an-error-check-to-merge_conf.patch \
	file://0023-kbuild-mergeconfig-remove-redundant-objtree.patch \
	file://0024-kbuild-add-generic-mergeconfig-target-.config.patch \
	file://0025-UBUNTU-Config-snappy-config-fragments-generic-system.patch \
	file://0026-UBUNTU-Config-snappy-snappy-SQUASHFS-m.patch \
	file://0027-UBUNTU-SAUCE-add-a-snapcraft.yaml-to-create-a-snappy.patch \
	file://0028-UBUNTU-SAUCE-add-a-snapcraft.yaml-to-create-a-snappy.patch \
	file://0029-UBUNTU-Config-snappy-containers-lxc.config-was-super.patch \
	file://0030-UBUNTU-Config-snappy-containers-CONFIG_OVERLAY_FS_V1.patch \
    "
include kernel-snap.inc
