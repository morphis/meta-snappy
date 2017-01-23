OpenEmbedded/Yocto layer for Ubuntu Snappy support
==================================================

This meta layer adds support for Ubuntu Snappy for all OpenEmbedded/
Yocto based devices.

This meta layer mainly contains the following components which are
required for Snappy:

 * snapd
 * snap-confine

# Try it!

1. Follow the Yocto Quickstart guide to get your build host properly
   setup: https://www.yoctoproject.org/docs/2.2/yocto-project-qs/yocto-project-qs.html

2. Download latest yocto release

```
 $ git clone git://git.yoctoproject.org/poky
 $ cd poky
 $ git checkout morty
```

3. Fetch meta-snappy layer

```
 $ git clone https://github.com/morphis/meta-snappy.git
```

4. Prepare the build environment

```
 $ source oe-init-build-env
```
 Now add meta-snappy to your conf/bblayers.conf so that it looks similar to this

```
 BBLAYERS ?= " \
   ...
   /tmp/poky/meta-snappy \
 "
```

5. Modify your conf/local.conf to enable support for systemd which is
   mandatory for snapd. See https://www.yoctoproject.org/docs/2.2/dev-manual/dev-manual.html#using-systemd-exclusively
   for more details.

```
cat<<EOF >> conf/local.conf
DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"
VIRTUAL-RUNTIME_initscripts = ""
EOF
```

6. Finally you can now build the Snappy demo image via

```
 $ bitbake snapd-demo-image
```

 Depending on your host system the build will take a while.

7. Once the build is done you can boot the image with Qemu with the following
   command:

```
 $ runqemu qemux86
```

8. When the system has fully booted login with root and no password. Afterwards
   you can use the the snap system as normal.

```
 $ snap install hello-world
 $ /snap/bin/hello-world.shell
```

# Contributions

Please submit any issues or pull requests on out github project at
http://github.com/morphis/meta-snappy
