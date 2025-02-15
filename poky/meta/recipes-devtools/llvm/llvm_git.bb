# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "The LLVM Compiler Infrastructure"
HOMEPAGE = "http://llvm.org"
LICENSE = "Apache-2.0-with-LLVM-exception"
SECTION = "devel"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=8a15a0759ef07f2682d2ba4b893c9afe"

DEPENDS = "libffi libxml2 zlib libedit ninja-native llvm-native"

COMPATIBLE_HOST:riscv64 = "null"
COMPATIBLE_HOST:riscv32 = "null"

RDEPENDS:${PN}:append:class-target = " ncurses-terminfo"

inherit cmake pkgconfig

PROVIDES += "llvm${PV}"

PV = "14.0.4"

MAJOR_VERSION = "${@oe.utils.trim_version("${PV}", 1)}"

LLVM_RELEASE = "${PV}"

BRANCH = "release/${MAJOR_VERSION}.x"
SRCREV = "29f1039a7285a5c3a9c353d054140bf2556d4c4d"
SRC_URI = "git://github.com/llvm/llvm-project.git;branch=${BRANCH};protocol=https \
           file://0006-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch;striplevel=2 \
           file://0007-llvm-allow-env-override-of-exe-path.patch;striplevel=2 \
           file://0001-AsmMatcherEmitter-sort-ClassInfo-lists-by-name-as-we.patch;striplevel=2 \
           "

UPSTREAM_CHECK_GITTAGREGEX = "llvmorg-(?P<pver>\d+(\.\d+)+)"

S = "${WORKDIR}/git/llvm"

LLVM_INSTALL_DIR = "${WORKDIR}/llvm-install"

def get_llvm_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var)
    if   re.match(r'(i.86|athlon|x86.64)$', a):         return 'X86'
    elif re.match(r'arm$', a):                          return 'ARM'
    elif re.match(r'armeb$', a):                        return 'ARM'
    elif re.match(r'aarch64$', a):                      return 'AArch64'
    elif re.match(r'aarch64_be$', a):                   return 'AArch64'
    elif re.match(r'mips(isa|)(32|64|)(r6|)(el|)$', a): return 'Mips'
    elif re.match(r'riscv(32|64)(eb|)$', a):            return 'RISCV'
    elif re.match(r'p(pc|owerpc)(|64)', a):             return 'PowerPC'
    else:
        raise bb.parse.SkipRecipe("Cannot map '%s' to a supported LLVM architecture" % a)

def get_llvm_host_arch(bb, d):
    return get_llvm_arch(bb, d, 'HOST_ARCH')

#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS ?= "AMDGPU;${@get_llvm_host_arch(bb, d)}"

ARM_INSTRUCTION_SET:armv5 = "arm"
ARM_INSTRUCTION_SET:armv4t = "arm"

EXTRA_OECMAKE += "-DLLVM_ENABLE_ASSERTIONS=OFF \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DLLVM_ENABLE_PIC=ON \
                  -DLLVM_BINDINGS_LIST='' \
                  -DLLVM_LINK_LLVM_DYLIB=ON \
                  -DLLVM_ENABLE_FFI=ON \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DFFI_INCLUDE_DIR=$(pkg-config --variable=includedir libffi) \
                  -DLLVM_OPTIMIZED_TABLEGEN=ON \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS}' \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
                  -DPYTHON_EXECUTABLE=${HOSTTOOLS_DIR}/python3 \
                  -G Ninja"

EXTRA_OECMAKE:append:class-target = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen${PV} \
                  -DLLVM_CONFIG_PATH=${STAGING_BINDIR_NATIVE}/llvm-config${PV} \
                 "

EXTRA_OECMAKE:append:class-nativesdk = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen${PV} \
                  -DLLVM_CONFIG_PATH=${STAGING_BINDIR_NATIVE}/llvm-config${PV} \
                 "

# patch out build host paths for reproducibility
do_compile:prepend:class-target() {
        sed -i -e "s,${WORKDIR},,g" ${B}/tools/llvm-config/BuildVariables.inc
}

do_compile() {
	ninja -v ${PARALLEL_MAKE}
}

do_compile:class-native() {
	ninja -v ${PARALLEL_MAKE} llvm-config llvm-tblgen
}

do_install() {
	DESTDIR=${D} ninja -v install

        # llvm harcodes usr/lib as install path, so this corrects it to actual libdir
        mv -T -n ${D}/${prefix}/lib ${D}/${libdir} || true

        # Remove opt-viewer: https://llvm.org/docs/Remarks.html
        rm -rf ${D}${datadir}/opt-viewer
        rmdir ${D}${datadir}

        # reproducibility
        sed -i -e 's,${WORKDIR},,g' ${D}/${libdir}/cmake/llvm/LLVMConfig.cmake
}

do_install:class-native() {
	install -D -m 0755 ${B}/bin/llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
	install -D -m 0755 ${B}/bin/llvm-config ${D}${bindir}/llvm-config${PV}
}

PACKAGES =+ "${PN}-bugpointpasses ${PN}-llvmhello ${PN}-libllvm ${PN}-liboptremarks ${PN}-liblto"

RRECOMMENDS:${PN}-dev += "${PN}-bugpointpasses ${PN}-llvmhello ${PN}-liboptremarks"

FILES:${PN}-bugpointpasses = "\
    ${libdir}/BugpointPasses.so \
"

FILES:${PN}-libllvm = "\
    ${libdir}/libLLVM-${MAJOR_VERSION}.so \
"

FILES:${PN}-liblto += "\
    ${libdir}/libLTO.so.* \
"

FILES:${PN}-liboptremarks += "\
    ${libdir}/libRemarks.so.* \
"

FILES:${PN}-llvmhello = "\
    ${libdir}/LLVMHello.so \
"

FILES:${PN}-dev += " \
    ${libdir}/llvm-config \
    ${libdir}/libRemarks.so \
    ${libdir}/libLLVM-${PV}.so \
"

FILES:${PN}-staticdev += "\
    ${libdir}/*.a \
"

INSANE_SKIP:${PN}-libllvm += "dev-so"

BBCLASSEXTEND = "native nativesdk"
