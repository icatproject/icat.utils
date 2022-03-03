#!/bin/bash

################################################################################
# WARNING: This script assumes a certain setup on your system as follows:      #
# 1) There must be a ~/downloads dir where the zip file for the new version    #
#    will be downloaded                                                        #
# 2) There is an existing payara installation at ~/payara-<version>            #
# 3) The existing "domains" directory at ~/payara-<version>/glassfish/domains  #
#    has been moved to ~/payara5_domains with a symbolic link to this          #
#    directory put in its place                                                #
# See the README file for this script for more information                     #
################################################################################

# stop if any of the commands fail
set -e

# edit the version numbers as required:
# old_version = currently installed version (at ~/payara-<version>)
# new_version = version to upgrade to
old_version="5.2021.1"
new_version="5.2021.10"

now=$(date '+%Y%m%d%H%M%S')


# first check that payara is not running
num_payaras_running=$(ps -ef | grep ASMain | grep -v 'grep' | wc -l)
if [ "${num_payaras_running}" -gt "0" ]; then
    echo "ERROR: cannot upgrade whilst payara is running."
    echo "Please stop payara then re-run this script."
    exit 1
fi

# download the new version of payara 5
cd ~/downloads
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara/${new_version}/payara-${new_version}.zip
# line below should get it directly from payara if the latest version is not in maven yet
#wget https://s3-eu-west-1.amazonaws.com/payara.fish/Payara+Downloads/${new_version}/payara-${new_version}.zip
# unzip it in the home directory
cd ~
unzip ~/downloads/payara-${new_version}.zip
# rename it to add the version number
mv ~/payara5 ~/payara-${new_version}
# remove the domains directory
rm -rf ~/payara-${new_version}/glassfish/domains
# replace it with a symbolic link to the existing domains directory
ln -s ~/payara5_domains ~/payara-${new_version}/glassfish/domains

# rename the old version of payara 5
mv ~/payara-${old_version} ~/payara-${old_version}_old

# replace the payara symlink with one to the new version
rm payara
ln -s payara-${new_version} ~/payara

# make a backup of the payara5_domains directory
cp -rp ~/payara5_domains ~/payara5_domains_bak_${now}

# remove cached things in the domains directory that
# can cause problems when moving between versions
rm -f ~/payara5_domains/domain1/lib/databases/ejbtimer.*
rm -rf ~/payara5_domains/domain1/generated/*
rm -rf ~/payara5_domains/domain1/osgi-cache/*

