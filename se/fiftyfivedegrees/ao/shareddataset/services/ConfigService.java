// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.services;

import net.java.ao.DBParam;
import se.fiftyfivedegrees.ao.shareddataset.entities.Config;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import javax.inject.Inject;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import javax.inject.Named;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.activeobjects.tx.Transactional;

@Transactional
@Scanned
@Named
public class ConfigService
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final ActiveObjects ao;
    
    @Inject
    public ConfigService(final JiraAuthenticationContext jiraAuthenticationContext, final ActiveObjects ao) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.ao = ao;
    }
    
    public Config addConfig(final SharedDataset sharedDataset, final boolean isStarred) {
        final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
        final Config config = (Config)this.ao.create((Class)Config.class, new DBParam[0]);
        config.setOwnerId(userKey);
        config.setDataSet(sharedDataset);
        config.setStarred(isStarred);
        config.save();
        return config;
    }
    
    public Config getConfig(final SharedDataset sharedDataset) {
        final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
        final Config[] configs2;
        final Config[] configs = configs2 = sharedDataset.getConfigs();
        for (final Config config : configs2) {
            if (config.getOwnerId().equals(userKey)) {
                return config;
            }
        }
        return this.addConfig(sharedDataset, false);
    }
    
    public Config updateConfig(final SharedDataset sharedDataset, final boolean isStarred) {
        final Config config = this.getConfig(sharedDataset);
        config.setStarred(isStarred);
        config.save();
        return config;
    }
}
