FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://snappy.cfg \
            file://0001-add-use-fns.patch \
            file://0002-v2.x-net-rules-compat.patch \
            file://0003-af_unix-mediation.patch"
