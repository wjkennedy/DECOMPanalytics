// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao;

import se.fiftyfivedegrees.Constants;
import javax.inject.Inject;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;

@JiraComponent
@Scanned
public class GlobalConfigurationService
{
    @ComponentImport
    private final GroupManager groupManager;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    
    @Inject
    public GlobalConfigurationService(final GroupManager groupManager, final PluginSettingsFactory pluginSettingsFactory) {
        this.groupManager = groupManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }
    
    private String getNameSpacedKey(final String key) {
        return "aa.analytics." + key;
    }
    
    public Integer getNumberOfDataLoaderThreads() {
        return (Integer)this.pluginSettingsFactory.createGlobalSettings().get(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_THREADS));
    }
    
    public void setNumberOfDataLoaderThreads(final Integer threads) {
        if (threads == null) {
            this.pluginSettingsFactory.createGlobalSettings().remove(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_THREADS));
        }
        else {
            this.pluginSettingsFactory.createGlobalSettings().put(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_THREADS), (Object)threads);
        }
    }
    
    public Integer getDataLoaderDelayMS() {
        return (Integer)this.pluginSettingsFactory.createGlobalSettings().get(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_REQUEST_DELAY));
    }
    
    public void setDataLoaderDelayMS(final Integer delay) {
        if (delay == null) {
            this.pluginSettingsFactory.createGlobalSettings().remove(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_REQUEST_DELAY));
        }
        else {
            this.pluginSettingsFactory.createGlobalSettings().put(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_REQUEST_DELAY), (Object)delay);
        }
    }
    
    public Integer getDataLoaderNumberOfIssues() {
        return (Integer)this.pluginSettingsFactory.createGlobalSettings().get(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_ISSUES));
    }
    
    public void setDataLoaderNumberOfIssues(final Integer numberOfIssues) {
        if (numberOfIssues == null) {
            this.pluginSettingsFactory.createGlobalSettings().remove(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_ISSUES));
        }
        else {
            this.pluginSettingsFactory.createGlobalSettings().put(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_NUMBER_OF_ISSUES), (Object)numberOfIssues);
        }
    }
    
    public Boolean getDataLoaderLoadFields() {
        return (Boolean)this.pluginSettingsFactory.createGlobalSettings().get(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_FETCH_CUSTOM_FIELDS));
    }
    
    public void setDataLoaderLoadFields(final Boolean fetch_custom_fields) {
        if (fetch_custom_fields) {
            this.pluginSettingsFactory.createGlobalSettings().remove(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_FETCH_CUSTOM_FIELDS));
        }
        else {
            this.pluginSettingsFactory.createGlobalSettings().put(this.getNameSpacedKey(Constants.STORAGE_KEY_DATA_LOADER_FETCH_CUSTOM_FIELDS), (Object)fetch_custom_fields);
        }
    }
}
