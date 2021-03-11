inherit gadgetsnap

do_install() {
    install -d  ${D}/
    install -m 0755 ${S}/gadget.snap ${D}/
}

FILES_${PN} = " \
    /gadget.snap \
"

