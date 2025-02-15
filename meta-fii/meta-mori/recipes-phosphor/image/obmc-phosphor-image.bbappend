# Required for IPMI
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-ipmi-blobs"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-ipmi-blobs-binarystore"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-ipmi-ethstats"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-ipmi-net"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-ipmi-host"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " google-ipmi-sys"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " google-ipmi-i2c"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-sel-logger"

# Required tools and utilities
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " hotswap-power-cycle"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " loadsvf"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " memtester"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " openssl-bin"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " ipmitool"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " ethtool"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " bash"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " i2c-tools"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " libgpiod-tools"

# Required for obmc-bmcweb
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " bmcweb"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-certificate-manager"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-user-manager"

# Required from packagegroup-mori-apps
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " ncsid"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " gbmc-mac-config"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " entity-manager"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " fru-device"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " dbus-sensors"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " estoraged"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-pid-control"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-logging"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " mori-fw"

# Required for phosphor-ipmi-ssif
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " virtual-obmc-host-ipmi-hw"

# Required for some services
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-software-manager-download-mgr"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-software-manager-version"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " phosphor-software-manager-updater"
OBMC_IMAGE_EXTRA_INSTALL:append:mori = " obmc-targets"
