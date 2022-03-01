# Script to upgrade an installed version of Payara Server 5

## Introduction

Payara server 5 releases are now done on a regular basis with 10 versions being released in 2021.

This script aims to simplify and speed up the process of upgrading to another version of Payara.

## Prerequisites

In order for the upgrade script to work, it expects a certain layout and naming of directories for your payara setup, as follows:

 * Payara is installed in the home directory (eg. for the glassfish or payara user)
 * There is a ```~/downloads``` directory where the zip file for the new version can be downloaded to
 * The existing version of Payara Server is installed at ```~/payara-<version>```
 * There is a symbolic link ```~/payara``` pointing to the current version at ```~/payara-<version>```
 * The existing ```domains``` directory has been moved to ```~/payara5_domains``` with a symbolic link to this directory put in its place

## Rationale

There are a number of reasons for adopting this setup:

 * Keeping the ```domains``` directory separate from the Payara Server installation creates a logical split between the application (which gets upgraded) and your installed applications, configuration etc. (which typically doesn't change between server versions).
 * Using the ```~/payara``` symbolic link allows you to set environment variables (eg. PATH) that don't need to be updated for each version.
 * Both of the above points are suggested in the Payara article: https://blog.payara.fish/how-to-upgrade-payara-server-5-reloaded
 * Rolling back to the previous version is as easy as reverting the ```~/payara``` symbolic link to the server installation and the ```~/payara/glassfish/domains``` symbolic link to the domains directory.

