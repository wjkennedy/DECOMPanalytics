// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.conditions;

import com.atlassian.plugin.PluginParseException;
import java.util.Map;
import javax.inject.Inject;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.web.Condition;

@Scanned
public class UserIsLoggedIn implements Condition
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    
    @Inject
    public UserIsLoggedIn(final JiraAuthenticationContext jiraAuthenticationContext) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }
    
    public void init(final Map<String, String> map) throws PluginParseException {
    }
    
    public boolean shouldDisplay(final Map<String, Object> map) {
        return this.jiraAuthenticationContext.isLoggedInUser();
    }
}
