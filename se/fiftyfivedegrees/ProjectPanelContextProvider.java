// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees;

import org.slf4j.LoggerFactory;
import java.util.WeakHashMap;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import java.util.Map;
import javax.inject.Inject;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.groups.GroupManager;
import se.fiftyfivedegrees.ao.GlobalConfigurationService;
import com.actionableagile.analytics.services.LicenseService;
import org.slf4j.Logger;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;

@Scanned
public class ProjectPanelContextProvider extends AbstractJiraContextProvider
{
    private static Logger logger;
    private final LicenseService licenseService;
    private final GlobalConfigurationService globalConfigurationService;
    @ComponentImport
    private final GroupManager groupManager;
    
    @Inject
    public ProjectPanelContextProvider(final LicenseService licenseService, final GlobalConfigurationService globalConfigurationService, final GroupManager groupManager) {
        this.licenseService = licenseService;
        this.globalConfigurationService = globalConfigurationService;
        this.groupManager = groupManager;
    }
    
    public Map getContextMap(final Map context) {
        context.put("licenseMessage", this.licenseService.getLicenseMessage());
        return context;
    }
    
    public Map getContextMap(final ApplicationUser applicationUser, final JiraHelper jiraHelper) {
        final Map<String, String> context = new WeakHashMap<String, String>();
        context.put("pid", jiraHelper.getProject().getId() + "");
        context.put("pKey", jiraHelper.getProject().getKey() + "");
        context.put("userKey", applicationUser.getKey());
        context.put("licenseMessage", this.licenseService.getLicenseMessage());
        return context;
    }
    
    static {
        ProjectPanelContextProvider.logger = LoggerFactory.getLogger((Class)ProjectPanelContextProvider.class);
    }
}
