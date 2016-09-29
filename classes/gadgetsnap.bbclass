inherit python3native

#generate gadget snap
do_gadget_snap() {
    snapcraft snap ${WORKDIR}/gadget --output gadget.snap
}

addtask gadget_snap after do_patch before do_compile

