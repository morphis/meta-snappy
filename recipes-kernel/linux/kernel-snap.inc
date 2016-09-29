inherit kernelsnap

FILES_kernel-base += " \
    /kernel.snap \
    "

do_install_append () {
    install -m 0666 ${S}/kernel.snap ${D}/
}


