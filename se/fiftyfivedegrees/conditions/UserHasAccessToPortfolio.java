// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.conditions;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.plugin.PluginParseException;
import java.util.Map;
import javax.inject.Inject;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.web.Condition;

@Scanned
public class UserHasAccessToPortfolio implements Condition
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final GlobalPermissionManager globalPermissionManager;
    
    @Inject
    public UserHasAccessToPortfolio(final JiraAuthenticationContext jiraAuthenticationContext, final GlobalPermissionManager globalPermissionManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.globalPermissionManager = globalPermissionManager;
    }
    
    public void init(final Map<String, String> map) throws PluginParseException {
    }
    
    public boolean shouldDisplay(final Map<String, Object> map) {
        return this.jiraAuthenticationContext.isLoggedInUser() && this.globalPermissionManager.hasPermission(GlobalPermissionKey.of("se.ffdegrees.project-portfolio-access"), this.jiraAuthenticationContext.getLoggedInUser());
    }
}
