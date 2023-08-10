// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.rest.user;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.project.Project;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import com.atlassian.jira.permission.ProjectPermissions;
import java.util.HashSet;
import java.util.HashMap;
import javax.ws.rs.core.Response;
import java.util.List;
import javax.inject.Inject;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import javax.ws.rs.Path;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

@Scanned
@Path("/user")
public class UserEndPoint
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final PermissionManager permissionManager;
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final ProjectManager projectManager;
    
    @Inject
    public UserEndPoint(final JiraAuthenticationContext jiraAuthenticationContext, final PermissionManager permissionManager, final UserManager userManager, final ProjectManager projectManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
        this.userManager = userManager;
        this.projectManager = projectManager;
    }
    
    @POST
    @Path("/convert")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @AnonymousAllowed
    public Response convertUsers(final List<String> userKeys) {
        final Map<String, UserEntity> userList = new HashMap<String, UserEntity>();
        final Set<String> validProjectIds = new HashSet<String>();
        for (final String userProject : userKeys) {
            final String[] parts = userProject.split("\\|");
            if (parts.length == 2 && !validProjectIds.contains(parts[1])) {
                final Project project = this.projectManager.getProjectObjByKeyIgnoreCase(parts[1]);
                if (project == null || !this.permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS, project, this.jiraAuthenticationContext.getLoggedInUser())) {
                    continue;
                }
                validProjectIds.add(parts[1]);
            }
        }
        for (final String userProject : userKeys) {
            final String[] parts = userProject.split("\\|");
            final String userKey = parts[0];
            final String projectId = parts[1];
            final Project project2 = this.projectManager.getProjectObjByKeyIgnoreCase(parts[1]);
            if (project2 != null && validProjectIds.contains(projectId)) {
                final ApplicationUser user = this.userManager.getUserByKey(userKey);
                if (user == null || (!this.permissionManager.hasPermission(ProjectPermissions.ASSIGNABLE_USER, project2, user) && !this.permissionManager.hasPermission(ProjectPermissions.CREATE_ISSUES, project2, user))) {
                    continue;
                }
                userList.put(userProject, new UserEntity(user.getName(), user.getDisplayName()));
            }
        }
        return Response.ok((Object)userList).build();
    }
}
