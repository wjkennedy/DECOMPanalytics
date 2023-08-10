// 
// Decompiled by Procyon v0.6.0
// 

package com.actionableagile.analytics.services;

import com.atlassian.upm.api.license.entity.LicenseError;
import com.atlassian.upm.api.license.entity.PluginLicense;
import javax.inject.Inject;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

@Scanned
@JiraComponent
public class LicenseService
{
    @ComponentImport
    private final PluginLicenseManager pluginLicenseManager;
    
    @Inject
    public LicenseService(final PluginLicenseManager pluginLicenseManager) {
        this.pluginLicenseManager = pluginLicenseManager;
    }
    
    public boolean isValidLicense() {
        return this.pluginLicenseManager.getLicense().isDefined() && ((PluginLicense)this.pluginLicenseManager.getLicense().get()).isValid();
    }
    
    public String getLicenseMessage() {
        if (this.pluginLicenseManager.getLicense().isDefined()) {
            if (!((PluginLicense)this.pluginLicenseManager.getLicense().get()).getError().isDefined()) {
                return null;
            }
            final LicenseError licenseError = (LicenseError)((PluginLicense)this.pluginLicenseManager.getLicense().get()).getError().get();
            if (licenseError != null) {
                if (licenseError.equals((Object)LicenseError.EXPIRED) && ((PluginLicense)this.pluginLicenseManager.getLicense().get()).isEvaluation()) {
                    return "The license for this app has expired  (" + licenseError.name() + ")";
                }
                if (licenseError.equals((Object)LicenseError.TYPE_MISMATCH)) {
                    return "The type of license for this app does not match the host application (" + licenseError.name() + ")";
                }
                if (licenseError.equals((Object)LicenseError.USER_MISMATCH)) {
                    return "The number of users licensed for this app does not match the host application (" + licenseError.name() + ")";
                }
                if (licenseError.equals((Object)LicenseError.VERSION_MISMATCH)) {
                    return "This version of the app does not have a valid license. (" + licenseError.name() + ")";
                }
                return "Unknown error with the license: " + licenseError.name();
            }
        }
        return "No license specified";
    }
}
