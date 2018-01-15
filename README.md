OpenEmbedded/Yocto layer for Ubuntu Snappy support
==================================================

This meta layer adds support for Ubuntu Snappy for all OpenEmbedded/Yocto based
devices.

This meta layer mainly contains the following components which are
required for Snappy:

 * snapd
 * libseccomp

The layer currently supports Yocto 2.4.x Rocko release.

Aside from OE-core, `meta-snappy` also depends on `meta-openembedded` and
`meta-filesystems`.

# Try it!

1. Follow the Yocto Quickstart guide to get your build host properly
   setup: https://www.yoctoproject.org/docs/latest/yocto-project-qs/yocto-project-qs.html

2. Download latest yocto release

```
 $ git clone git://git.yoctoproject.org/poky
 $ cd poky
 $ git checkout rocko
```

3. Fetch meta-openembedded layer:

```
 $ git clone git://git.openembedded.org/meta-openembedded
 $ cd meta-openembedded
 $ git checkout rocko
```

4. Fetch meta-snappy layer

```
 $ git clone https://github.com/morphis/meta-snappy.git
```

5. Prepare the build environment

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
  "
```

6. Modify your conf/local.conf to enable support for systemd which is
   mandatory for snapd. See https://www.yoctoproject.org/docs/latest/dev-manual/dev-manual.html#using-systemd-exclusively
   for more details.

```
cat<<EOF >> conf/local.conf
DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"
VIRTUAL-RUNTIME_initscripts = ""
EOF
```

 The build can take up a huge amount of disk space, inheriting `rm_work` class
 will help deal with that (see
 https://www.yoctoproject.org/docs/latest/ref-manual/ref-manual.html#ref-classes-rm-work
 for details):

```
INHERIT += "rm_work"
# exclude snapd in case you want to develop snapd recipes
RM_WORK_EXCLUDE += "snapd"
```

6. Finally you can now build the Snappy demo image via

```
 $ bitbake snapd-demo-image
```

 Depending on your host system the build will take a while.

 The `snapd-demo-image` set `IMAGE_ROOTFS_SIZE ?= "819200"`, this the resulting
 image is > 800MB in size. When building custom images make sure to add room for
 storing snaps by either setting `IMAGE_ROOTFS_EXTRA_SPACE` or tuning
 `IMAGE_ROOTFS_SIZE`

7. Once the build is done you can boot the image with QEMU with the following
   command:

```
 $ runqemu qemux86
```

8. When the system has fully booted login with root and no password. Afterwards
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
