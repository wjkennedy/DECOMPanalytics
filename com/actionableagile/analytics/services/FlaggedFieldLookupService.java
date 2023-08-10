// 
// Decompiled by Procyon v0.6.0
// 

package com.actionableagile.analytics.services;

import com.atlassian.jira.issue.fields.CustomField;
import com.opensymphony.module.propertyset.PropertySet;
import javax.inject.Inject;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.propertyset.JiraPropertySetFactory;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.sal.api.lifecycle.LifecycleAware;

@JiraComponent
@Scanned
@ExportAsService
public class FlaggedFieldLookupService implements LifecycleAware
{
    @ComponentImport
    private final JiraPropertySetFactory jiraPropertySetFactory;
    @ComponentImport
    private final CustomFieldManager customFieldManager;
    private String customFieldName;
    
    @Inject
    public FlaggedFieldLookupService(final JiraPropertySetFactory jiraPropertySetFactory, final CustomFieldManager customFieldManager) {
        this.customFieldName = null;
        this.jiraPropertySetFactory = jiraPropertySetFactory;
        this.customFieldManager = customFieldManager;
        this.customFieldName = null;
    }
    
    public void onStart() {
        this.customFieldName = null;
    }
    
    public void onStop() {
    }
    
    public String getFlaggedFieldName() {
        if (this.customFieldName == null) {
            final PropertySet propertySet = this.jiraPropertySetFactory.buildCachingDefaultPropertySet("GreenHopper.properties");
            Long customfieldId = null;
            if (propertySet.exists("GreenHopper.Flag.Default.customfield.id")) {
                customfieldId = propertySet.getLong("GreenHopper.Flag.Default.customfield.id");
            }
            else if (propertySet.exists("gh.issue.flaggingfield")) {
                customfieldId = propertySet.getLong("gh.issue.flaggingfield");
            }
            if (customfieldId != null) {
                final CustomField customField = this.customFieldManager.getCustomFieldObject(customfieldId);
                if (customField != null) {
                    this.customFieldName = customField.getFieldName();
                }
            }
            else {
                this.customFieldName = "Flagged";
            }
        }
        return this.customFieldName;
    }
}
