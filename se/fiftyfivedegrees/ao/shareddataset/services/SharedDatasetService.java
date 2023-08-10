// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.services;

import se.fiftyfivedegrees.ao.shareddataset.entities.DatasetToViewer;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashMap;
import se.fiftyfivedegrees.ao.shareddataset.entities.Config;
import com.atlassian.jira.user.ApplicationUser;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerModel;
import java.util.ArrayList;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.entities.Viewer;
import net.java.ao.RawEntity;
import net.java.ao.Query;
import se.fiftyfivedegrees.ao.shareddataset.entities.DatasetToOwner;
import se.fiftyfivedegrees.ao.shareddataset.entities.Owner;
import com.google.gson.Gson;
import net.java.ao.DBParam;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.SharedDataSetModel;
import javax.inject.Inject;
import com.google.common.base.Preconditions;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.activeobjects.external.ActiveObjects;
import javax.inject.Named;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.activeobjects.tx.Transactional;

@Transactional
@Scanned
@Named
public class SharedDatasetService
{
    @ComponentImport
    private final ActiveObjects ao;
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final GroupManager groupManager;
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final AvatarService avatarManager;
    
    @Inject
    public SharedDatasetService(final ActiveObjects ao, final JiraAuthenticationContext jiraAuthenticationContext, final GroupManager groupManager, final UserManager userManager, final AvatarService avatarManager) {
        this.ao = (ActiveObjects)Preconditions.checkNotNull((Object)ao);
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.groupManager = groupManager;
        this.userManager = userManager;
        this.avatarManager = avatarManager;
    }
    
    public SharedDataset add(final SharedDataSetModel params) {
        final SharedDataset sharedDataset = (SharedDataset)this.ao.create((Class)SharedDataset.class, new DBParam[0]);
        final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
        final Gson gson = new Gson();
        sharedDataset.setUuid(params.getUuid());
        sharedDataset.setShared(Boolean.TRUE.equals(params.isShared()));
        sharedDataset.setName(params.getName());
        sharedDataset.setDate(params.getDate());
        sharedDataset.setOwner(userKey);
        sharedDataset.setColumnOrder(gson.toJson(params.getColumnOrder()));
        sharedDataset.setColumns(gson.toJson(params.getColumns()));
        sharedDataset.setFieldList(gson.toJson(params.getFieldList()));
        sharedDataset.setFilters(gson.toJson(params.getFilters()));
        sharedDataset.setDataConfigs(gson.toJson(params.getDataConfigs()));
        sharedDataset.save();
        this.assignOwnerById(sharedDataset, userKey);
        return sharedDataset;
    }
    
    public void assignOwnerById(final SharedDataset sharedDataset, final String ownerId) {
        if (((Owner[])this.ao.find((Class)Owner.class, "OWNER_ID = ?", new Object[] { ownerId })).length == 0) {
            final Owner owner = (Owner)this.ao.create((Class)Owner.class, new DBParam[0]);
            owner.setOwnerId(ownerId);
            owner.save();
            this.assignOwner(sharedDataset, owner);
        }
        else {
            final Owner owner = ((Owner[])this.ao.find((Class)Owner.class, "OWNER_ID = ?", new Object[] { ownerId }))[0];
            this.assignOwner(sharedDataset, owner);
        }
    }
    
    public void assignOwner(final SharedDataset sharedDataset, final Owner owner) {
        final DatasetToOwner datasetToOwner = (DatasetToOwner)this.ao.create((Class)DatasetToOwner.class, new DBParam[0]);
        datasetToOwner.setDataset(sharedDataset);
        datasetToOwner.setOwner(owner);
        datasetToOwner.save();
    }
    
    public void unassignOwnerById(final SharedDataset sharedDataset, final String ownerId) {
        final Owner ownerToDelete = ((Owner[])this.ao.find((Class)Owner.class, "OWNER_ID = ?", new Object[] { ownerId }))[0];
        final DatasetToOwner[] datasetToOwners = (DatasetToOwner[])this.ao.find((Class)DatasetToOwner.class, Query.select().where("DATASET_ID = ? AND OWNER_ID = ?", new Object[] { sharedDataset.getID(), ownerToDelete.getID() }));
        this.ao.delete((RawEntity[])datasetToOwners);
    }
    
    public boolean isAdminForDataSet(final SharedDataset sharedDataset) {
        final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
        for (final Owner owner : sharedDataset.getOwners()) {
            if (owner.getOwnerId().equals(userKey)) {
                return true;
            }
        }
        return false;
    }
    
    public SharedDataset update(final int id, final SharedDataSetModel params) {
        final SharedDataset sharedDataset = this.getFromId(id);
        final Gson gson = new Gson();
        if (params.getName() != null) {
            sharedDataset.setName(params.getName());
        }
        if (params.getDate() != null) {
            sharedDataset.setDate(params.getDate());
        }
        if (params.getDataConfigs() != null) {
            sharedDataset.setDataConfigs(gson.toJson(params.getDataConfigs()));
        }
        if (params.getFieldList() != null) {
            sharedDataset.setFieldList(gson.toJson(params.getFieldList()));
        }
        if (params.getColumns() != null) {
            sharedDataset.setColumns(gson.toJson(params.getColumns()));
        }
        if (params.getColumnOrder() != null) {
            sharedDataset.setColumnOrder(gson.toJson(params.getColumnOrder()));
        }
        if (params.getFilters() != null) {
            sharedDataset.setFilters(gson.toJson(params.getFilters()));
        }
        sharedDataset.save();
        return sharedDataset;
    }
    
    public void removeSharedDataSet(final SharedDataset sharedDataset) {
        for (final Owner owner : sharedDataset.getOwners()) {
            this.unassignOwnerById(sharedDataset, owner.getOwnerId());
        }
        for (final Viewer viewer : sharedDataset.getViewers()) {
            this.unassignViewerById(sharedDataset, viewer.getKey());
        }
        this.ao.delete(new RawEntity[] { (RawEntity)sharedDataset });
    }
    
    public SharedDataset getFromId(final int id) {
        try {
            return (SharedDataset)this.ao.get((Class)SharedDataset.class, (Object)id);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    private List<SharedDataSetModel> collectSharedDataSets(final SharedDataset[] sharedDatasets) {
        final ApplicationUser currentUser = this.jiraAuthenticationContext.getLoggedInUser();
        final List<SharedDataSetModel> sharedDatasetList = new ArrayList<SharedDataSetModel>();
        for (final SharedDataset sharedDataset : sharedDatasets) {
            final List<OwnerModel> ownerModelList = new ArrayList<OwnerModel>();
            for (final Owner owner : sharedDataset.getOwners()) {
                final ApplicationUser ownerUser = this.userManager.getUserByKey(owner.getOwnerId());
                final String avatarURL = this.avatarManager.getAvatarURL(currentUser, ownerUser).toString();
                if (ownerUser != null) {
                    ownerModelList.add(new OwnerModel(ownerUser.getKey(), ownerUser.getDisplayName(), ownerUser.getKey().equals(sharedDataset.getOwner()), avatarURL));
                }
            }
            final SharedDataSetModel sharedDataSetAddModel = new SharedDataSetModel(sharedDataset);
            final Config config = this.getConfig(sharedDataset);
            sharedDataSetAddModel.setAdmin(false);
            sharedDataSetAddModel.setOwners(ownerModelList);
            sharedDataSetAddModel.setStarred(config.isStarred());
            sharedDatasetList.add(sharedDataSetAddModel);
        }
        return sharedDatasetList;
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
    
    public List<Object> all() {
        final ApplicationUser currentUser = this.jiraAuthenticationContext.getLoggedInUser();
        final String userKey = currentUser.getKey();
        final Map<Integer, SharedDataSetModel> datasetMap = new HashMap<Integer, SharedDataSetModel>();
        final Viewer[] array;
        final Viewer[] viewerUsers = array = (Viewer[])this.ao.find((Class)Viewer.class, Query.select().where("KEY = ?", new Object[] { userKey }));
        for (final Viewer viewer : array) {
            final List<SharedDataSetModel> sharedDatasetList = this.collectSharedDataSets(viewer.getDatasets());
            for (final SharedDataSetModel sharedDataSetAddModel : sharedDatasetList) {
                datasetMap.put(sharedDataSetAddModel.getId(), sharedDataSetAddModel);
            }
        }
        final String[] groupNames = this.groupManager.getGroupNamesForUser(this.jiraAuthenticationContext.getLoggedInUser()).toArray(new String[0]);
        if (groupNames.length > 0) {
            final String questionmarkString = new String(new char[groupNames.length - 1]).replace("\u0000", ", ?");
            final Viewer[] viewers = (Viewer[])this.ao.find((Class)Viewer.class, Query.select().where("KEY IN (?" + questionmarkString + ")", (Object[])groupNames));
            System.out.println(Arrays.toString(viewers));
            for (final Viewer viewer2 : viewers) {
                final List<SharedDataSetModel> sharedDatasetList2 = this.collectSharedDataSets(viewer2.getDatasets());
                for (final SharedDataSetModel sharedDataSetAddModel2 : sharedDatasetList2) {
                    datasetMap.put(sharedDataSetAddModel2.getId(), sharedDataSetAddModel2);
                }
            }
        }
        final Owner[] owners = (Owner[])this.ao.find((Class)Owner.class, Query.select().where("OWNER_ID = ?", new Object[] { userKey }));
        if (owners.length > 0) {
            final SharedDataset[] ownersDatasets = owners[0].getDatasets();
            final List<SharedDataSetModel> sharedDatasetList3 = this.collectSharedDataSets(ownersDatasets);
            for (final SharedDataSetModel sharedDataSetAddModel3 : sharedDatasetList3) {
                sharedDataSetAddModel3.setAdmin(true);
                datasetMap.put(sharedDataSetAddModel3.getId(), sharedDataSetAddModel3);
            }
        }
        return Lists.newArrayList((Iterable)datasetMap.values());
    }
    
    public void assignViewerById(final SharedDataset sharedDataset, final String key, final String type) {
        if (((Viewer[])this.ao.find((Class)Viewer.class, "KEY = ?", new Object[] { key })).length == 0) {
            final Viewer viewer = (Viewer)this.ao.create((Class)Viewer.class, new DBParam[0]);
            viewer.setKey(key);
            viewer.setType(type);
            viewer.save();
            final DatasetToViewer datasetToViewer = (DatasetToViewer)this.ao.create((Class)DatasetToViewer.class, new DBParam[0]);
            datasetToViewer.setDataset(sharedDataset);
            datasetToViewer.setViewer(viewer);
            datasetToViewer.save();
        }
        else {
            final Viewer viewer = ((Viewer[])this.ao.find((Class)Viewer.class, "KEY = ?", new Object[] { key }))[0];
            final DatasetToViewer datasetToViewer = (DatasetToViewer)this.ao.create((Class)DatasetToViewer.class, new DBParam[0]);
            datasetToViewer.setDataset(sharedDataset);
            datasetToViewer.setViewer(viewer);
            datasetToViewer.save();
        }
    }
    
    public void unassignViewerById(final SharedDataset sharedDataset, final String viewerId) {
        final Viewer viewerToDelete = ((Viewer[])this.ao.find((Class)Viewer.class, "KEY = ?", new Object[] { viewerId }))[0];
        final DatasetToViewer[] datasetToViewers = (DatasetToViewer[])this.ao.find((Class)DatasetToViewer.class, Query.select().where("DATASET_ID = ? AND VIEWER_ID = ?", new Object[] { sharedDataset.getID(), viewerToDelete.getID() }));
        this.ao.delete((RawEntity[])datasetToViewers);
    }
}
