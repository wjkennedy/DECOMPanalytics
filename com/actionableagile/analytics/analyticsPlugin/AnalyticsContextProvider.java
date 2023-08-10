// 
// Decompiled by Procyon v0.6.0
// 

package com.actionableagile.analytics.analyticsPlugin;

import java.time.temporal.Temporal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.atlassian.upm.api.license.entity.LicenseError;
import org.joda.time.DateTime;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.plugin.webresource.UrlMode;
import java.util.HashMap;
import java.util.Map;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import javax.inject.Inject;
import com.actionableagile.analytics.services.FlaggedFieldLookupService;
import se.fiftyfivedegrees.ao.GlobalConfigurationService;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.plugin.osgi.bridge.external.PluginRetrievalService;
import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;

@Scanned
public class AnalyticsContextProvider extends AbstractJiraContextProvider
{
    @ComponentImport
    private final PageBuilderService pageBuilderService;
    @ComponentImport
    private final WebResourceManager webResourceManager;
    @ComponentImport
    private final PluginLicenseManager pluginLicenseManager;
    @ComponentImport
    private final DateTimeFormatter dateTimeFormatter;
    @ComponentImport
    private final PluginRetrievalService pluginRetrievalService;
    @ComponentImport
    private final GroupManager groupManager;
    private final GlobalConfigurationService globalConfigurationService;
    final FlaggedFieldLookupService flaggedFieldLookupService;
    
    @Inject
    public AnalyticsContextProvider(final PluginLicenseManager pluginLicenseManager, final DateTimeFormatter dateTimeFormatter, final WebResourceManager webResourceManager, final PluginRetrievalService pluginRetrievalService, final FlaggedFieldLookupService flaggedFieldLookupService, final GlobalConfigurationService globalConfigurationService, final GroupManager groupManager, final PageBuilderService pageBuilderService) {
        this.pluginLicenseManager = pluginLicenseManager;
        this.webResourceManager = webResourceManager;
        this.dateTimeFormatter = dateTimeFormatter;
        this.pluginRetrievalService = pluginRetrievalService;
        this.flaggedFieldLookupService = flaggedFieldLookupService;
        this.globalConfigurationService = globalConfigurationService;
        this.groupManager = groupManager;
        this.pageBuilderService = pageBuilderService;
    }
    
    public Map getContextMap(final ApplicationUser user, final JiraHelper jiraHelper) {
        final Map<String, Object> context = new HashMap<String, Object>();
        this.pageBuilderService.assembler().resources().requireContext("com.atlassian.jira.jira-core-reports-plugin.report-result");
        this.pageBuilderService.assembler().resources().requireContext("55degrees-actionable-jira");
        context.put("flaggedField", this.flaggedFieldLookupService.getFlaggedFieldName());
        context.put("pluginVersion", this.pluginRetrievalService.getPlugin().getPluginInformation().getVersion());
        context.put("analyticsJSHtml", this.webResourceManager.getResourceTags("aa.analytics:55degrees-actionable-jira", UrlMode.ABSOLUTE));
        if (this.pluginLicenseManager.getLicense().isDefined()) {
            final PluginLicense pluginLicense = (PluginLicense)this.pluginLicenseManager.getLicense().get();
            if (pluginLicense.isValid()) {
                context.put("validLicense", true);
                context.put("userKey", user.getKey());
                if (pluginLicense.isEvaluation()) {
                    context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#FFFAE6;color:##172B4D;padding:10px;padding-left:40px;\">Your evaluation license of ActionableAgile Analytics will expire " + this.getExpirationTime(((DateTime)pluginLicense.getExpiryDate().get()).toDate()) + ". Please consider purchasing before then.</div>");
                }
                else {
                    context.put("licenseMessageHtml", "");
                }
            }
            else if (pluginLicense.getError() != null) {
                switch ((LicenseError)pluginLicense.getError().get()) {
                    case EXPIRED: {
                        context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#DE350B;color:#fff;padding:10px;padding-left:40px;\"><span class=\"aui-icon aui-icon-small aui-iconfont-warning\">Warning</span> Invalid license: Your license of ActionableAgile Analytics has expired. Please purchase a license.</div>");
                        break;
                    }
                    case USER_MISMATCH: {
                        context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#DE350B;color:#fff;padding:10px;padding-left:40px;\"><span class=\"aui-icon aui-icon-small aui-iconfont-warning\">Warning</span> Invalid license: Your license of ActionableAgile Analytics must match the user count of your Jira instance.</div>");
                        break;
                    }
                    case VERSION_MISMATCH: {
                        context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#DE350B;color:#fff;padding:10px;padding-left:40px;\"><span class=\"aui-icon aui-icon-small aui-iconfont-warning\">Warning</span> Invalid license: Your license of ActionableAgile Analytics is not valid for this version. Please contact support@55degrees.se so that they may help you.</div>");
                        break;
                    }
                    case TYPE_MISMATCH: {
                        context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#DE350B;color:#fff;padding:10px;padding-left:40px;\"><span class=\"aui-icon aui-icon-small aui-iconfont-warning\">Warning</span> Invalid license: Your license of ActionableAgile Analytics is not valid for the type license that this Jira instance has installed. Please contact support@55degrees.se so that they may help you.</div>");
                        break;
                    }
                    default: {
                        context.put("licenseMessageHtml", ((LicenseError)pluginLicense.getError().get()).name());
                        break;
                    }
                }
            }
            else {
                context.put("licenseMessageHtml", "");
            }
        }
        else {
            context.put("validLicense", false);
            context.put("licenseMessageHtml", "<div style=\"margin-top:1em;margin-bottom:1em;text-align:center;background-color:#DE350B;color:#fff;padding:10px;padding-left:40px;\">No valid license for ActionableAgile Analytics found. Please purchase a license.</div>");
        }
        return context;
    }
    
    private String getExpirationTime(final Date expiration) {
        final long days = ChronoUnit.DAYS.between(new Date().toInstant(), expiration.toInstant());
        if (days > 1L) {
            return "in " + days + " days";
        }
        if (days == 0L) {
            return "today";
        }
        return "in 1 day";
    }
}
