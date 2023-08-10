// 
// Decompiled by Procyon v0.6.0
// 

package com.actionableagile.analytics.jira.reports;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.util.ParameterUtils;
import java.util.Map;
import com.atlassian.jira.web.action.ProjectActionSupport;
import javax.inject.Inject;
import com.actionableagile.analytics.services.LicenseService;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.jira.plugin.report.impl.AbstractReport;

@Scanned
public class FlowReport extends AbstractReport
{
    @ComponentImport
    private final PageBuilderService pageBuilderService;
    @ComponentImport
    private final ProjectManager projectManager;
    private final LicenseService licenseService;
    
    @Inject
    public FlowReport(final ProjectManager projectManager, final LicenseService licenseService, final PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
        this.projectManager = projectManager;
        this.licenseService = licenseService;
    }
    
    public String generateReportHtml(final ProjectActionSupport projectActionSupport, final Map map) throws Exception {
        this.pageBuilderService.assembler().resources().requireContext("com.atlassian.jira.jira-core-reports-plugin.report-result");
        final Long projectId = ParameterUtils.getLongParam(map, "selectedProjectId");
        final String projectKey = this.projectManager.getProjectObj(projectId).getKey();
        final Map startingParams = EasyMap.build((Object)"projectId", (Object)projectId, (Object)"projectKey", (Object)projectKey, (Object)"portlet", (Object)this);
        if (this.licenseService.isValidLicense()) {
            return this.descriptor.getHtml("view", startingParams);
        }
        return this.descriptor.getHtml("license", startingParams);
    }
}
