// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.services;

import com.atlassian.jira.user.ApplicationUser;
import se.fiftyfivedegrees.ao.shareddataset.entities.Viewer;
import se.fiftyfivedegrees.ao.shareddataset.models.ViewerModel;
import java.util.ArrayList;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import javax.inject.Inject;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.user.util.UserManager;
import javax.inject.Named;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.activeobjects.tx.Transactional;

@Transactional
@Scanned
@Named
public class ViewerService
{
    @ComponentImport
    private final UserManager userManager;
    
    @Inject
    public ViewerService(final UserManager userManager) {
        this.userManager = userManager;
    }
    
    public List<Object> getViewerList(final SharedDataset sharedDataset) {
        final Viewer[] viewers = sharedDataset.getViewers();
        final List<Object> viewerList = new ArrayList<Object>();
        for (final Viewer viewer : viewers) {
            if (viewer.getType().equals("user")) {
                final ApplicationUser user = this.userManager.getUserByKey(viewer.getKey());
                if (user != null) {
                    viewerList.add(new ViewerModel(viewer.getKey(), user.getDisplayName(), viewer.getType()));
                }
            }
            else {
                viewerList.add(new ViewerModel(viewer.getKey(), viewer.getKey(), viewer.getType()));
            }
        }
        return viewerList;
    }
}
