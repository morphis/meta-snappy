inherit python3native

#generate kernel snap
do_kernel_snap () {
    set +e
    cd ${S}
    snapcraft --target-arch armhf snap --output kernel.snap
}

addtask kernel_snap after do_patch before do_compile

