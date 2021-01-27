OpenEmbedded/Yocto layer for Ubuntu Snappy support
==================================================

This meta layer adds support for Ubuntu Snappy for all OpenEmbedded/Yocto based
devices.

This meta layer mainly contains the following components which are
required for supporting snaps in your system:

 * snapd

The layer currently supports Yocto 3.1.x Dunfell release.

The following layers are required:

 - meta-openembedded/meta-oe
 - meta-openembedded/meta-filesystems
 - meta-security (libseccomp & apparmor)

Note that those layers may depend on additional layers you will need to add.


# Try it!

1. Follow the Yocto Quickstart guide to get your build host properly
   setup: https://www.yoctoproject.org/docs/latest/yocto-project-qs/yocto-project-qs.html

2. Download latest yocto release

```
 $ git clone git://git.yoctoproject.org/poky
 $ cd poky
 $ git checkout dunfell
```

3. Fetch meta-openembedded layer:

```
 $ git clone git://git.openembedded.org/meta-openembedded
 $ cd meta-openembedded
 $ git checkout dunfell
```

3. Fetch meta-security layer:

```
 $ git clone git://git.yoctoproject.org/meta-security
 $ cd meta-security
 $ git checkout dunfell
```

5. Fetch meta-snappy layer

```
 $ git clone https://github.com/morphis/meta-snappy.git
```

6. Prepare the build environment

```
 $ source oe-init-build-env
```

 Now add meta-snappy to your conf/bblayers.conf so that it looks similar to this

```
 BBLAYERS ?= " \
   ...
   /tmp/poky/meta-snappy \
   /tmp/meta-openembedded/meta-oe \
   /tmp/meta-openembedded/meta-filesystems \
   /tmp/meta-openembedded/meta-security \
  "
```

7. Modify your conf/local.conf

 Enable support for systemd which is mandatory for snapd. See
 https://www.yoctoproject.org/docs/latest/dev-manual/dev-manual.html#using-systemd-exclusively
 for more details.

```
cat <<EOF >> conf/local.conf
DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"
VIRTUAL-RUNTIME_initscripts = ""
EOF
```

Optionally enable AppArmor support:

```
cat <<EOF >> conf/local.conf
DISTRO_FEATURES_append = " apparmor"
EOF
```

The `snap-confine` tool assumes that the home directory of `root` is `/root`.
Make sure we do not break this assumption, otherwise snaps mount namespace
setup will fail early in the process. To use `/root', set `ROOT_HOME` like
this:`

```
cat <<EOF >> conf/local.conf
ROOT_HOME = "/root"
EOF
```

 (Optional) The build can take up a huge amount of disk space, inheriting
 `rm_work` class will help deal with that (see
 https://www.yoctoproject.org/docs/latest/ref-manual/ref-manual.html#ref-classes-rm-work
 for details):

```
INHERIT += "rm_work"
# exclude snapd in case you want to develop snapd recipes
RM_WORK_EXCLUDE += "snapd"
```

8. Finally you can now build the Snappy demo image via

```
 $ bitbake snapd-demo-image
```

 Depending on your host system the build will take a while.

 The `snapd-demo-image` set `IMAGE_ROOTFS_SIZE ?= "819200"`, this the resulting
 image is > 800MB in size. When building custom images make sure to add room for
 storing snaps by either setting `IMAGE_ROOTFS_EXTRA_SPACE` or tuning
 `IMAGE_ROOTFS_SIZE`

9. Once the build is done you can boot the image with QEMU with the following
   command:

```
 $ runqemu qemux86
```

10. When the system has fully booted login with root and no password. Afterwards
   you can use the the snap system as normal.

```
 $ snap install hello-world
 $ /snap/bin/hello-world.shell
 # path to /snap/bin is automatically addded to user's environment
 $ hello-world
```

# Contributions & support

Please submit any issues or pull requests on out github project at
http://github.com/morphis/meta-snappy

Head out to https://forum.snapcraft.io/ for support and questions about snapd.
