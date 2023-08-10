// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.services;

import se.fiftyfivedegrees.ao.shareddataset.entities.Owner;
import com.atlassian.jira.user.ApplicationUser;
import java.util.ArrayList;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerModel;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import javax.inject.Inject;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import javax.inject.Named;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.activeobjects.tx.Transactional;

@Transactional
@Scanned
@Named
public class OwnerService
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final AvatarService avatarService;
    
    @Inject
    public OwnerService(final JiraAuthenticationContext jiraAuthenticationContext, final UserManager userManager, final AvatarService avatarService) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.userManager = userManager;
        this.avatarService = avatarService;
    }
    
    public List<OwnerModel> getOwnerList(final SharedDataset sharedDataset) {
        final ApplicationUser currentLoginUser = this.jiraAuthenticationContext.getLoggedInUser();
        final Owner[] owners = sharedDataset.getOwners();
        final List<OwnerModel> ownerList = new ArrayList<OwnerModel>();
        for (final Owner owner : owners) {
            final ApplicationUser user = this.userManager.getUserByKey(owner.getOwnerId());
            if (user != null) {
                final String avatarString = this.avatarService.getAvatarURL(currentLoginUser, user).toString();
                ownerList.add(new OwnerModel(owner.getOwnerId(), user.getDisplayName(), user.getKey().equals(sharedDataset.getOwner()), avatarString));
            }
        }
        return ownerList;
    }
}
