// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.resources;

import org.slf4j.LoggerFactory;
import javax.ws.rs.DELETE;
import se.fiftyfivedegrees.ao.shareddataset.models.StarredModel;
import javax.ws.rs.PUT;
import java.util.Iterator;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerModel;
import se.fiftyfivedegrees.ao.shareddataset.entities.Config;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.models.ListRestResourceModel;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.SharedDataSetModel;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import se.fiftyfivedegrees.ao.shareddataset.services.ConfigService;
import se.fiftyfivedegrees.ao.shareddataset.services.OwnerService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import se.fiftyfivedegrees.ao.shareddataset.services.SharedDatasetService;
import javax.ws.rs.Path;

@Path("/shared-dataset")
public class SharedDatasetRestResource
{
    @ComponentImport
    private final SharedDatasetService sharedDatasetService;
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final OwnerService ownerService;
    @ComponentImport
    private final ConfigService configService;
    private static final Logger logger;
    
    public SharedDatasetRestResource(final SharedDatasetService sharedDatasetService, final JiraAuthenticationContext jiraAuthenticationContext, final OwnerService ownerService, final ConfigService configService) {
        this.sharedDatasetService = (SharedDatasetService)Preconditions.checkNotNull((Object)sharedDatasetService);
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.ownerService = ownerService;
        this.configService = configService;
    }
    
    @POST
    @Produces({ "application/json" })
    @Consumes({ "application/json" })
    public Response createSharedDataset(final SharedDataSetModel sharedDataSet) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        final SharedDataset newSharedDataset = this.sharedDatasetService.add(sharedDataSet);
        final Config config = this.configService.addConfig(newSharedDataset, Boolean.TRUE.equals(sharedDataSet.isStarred()));
        final List<OwnerModel> ownerModelList = this.ownerService.getOwnerList(newSharedDataset);
        final SharedDataSetModel sharedDataSetObj = new SharedDataSetModel(newSharedDataset);
        sharedDataSetObj.setAdmin(true);
        sharedDataSetObj.setOwners(ownerModelList);
        sharedDataSetObj.setStarred(config.isStarred());
        final List<SharedDataSetModel> sharedDatasetList = new ArrayList<SharedDataSetModel>();
        sharedDatasetList.add(sharedDataSetObj);
        return Response.ok((Object)new ListRestResourceModel("create", sharedDatasetList)).build();
    }
    
    @GET
    @Produces({ "application/json" })
    public Response getSharedDatasets() {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        return Response.ok((Object)new ListRestResourceModel("all", this.sharedDatasetService.all())).build();
    }
    
    @GET
    @Produces({ "application/json" })
    @Path("/{id}")
    public Response getSharedDatasetFromPath(@PathParam("id") final int id) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            SharedDatasetRestResource.logger.warn("Data set is not found");
            return Response.status(404).build();
        }
        final Config config = this.configService.getConfig(sharedDataset);
        final List<OwnerModel> ownerModelList = this.ownerService.getOwnerList(sharedDataset);
        final SharedDataSetModel sharedDataSetObj = new SharedDataSetModel(sharedDataset);
        sharedDataSetObj.setAdmin(false);
        sharedDataSetObj.setStarred(config.isStarred());
        final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
        for (final OwnerModel owner : ownerModelList) {
            if (owner.getKey().equals(userKey)) {
                sharedDataSetObj.setAdmin(true);
                break;
            }
        }
        sharedDataSetObj.setOwners(ownerModelList);
        final List<SharedDataSetModel> sharedDatasetList = new ArrayList<SharedDataSetModel>();
        sharedDatasetList.add(sharedDataSetObj);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), sharedDatasetList)).build();
    }
    
    @PUT
    @Produces({ "application/json" })
    @Consumes({ "application/json" })
    @Path("/{id}")
    public Response updateSharedDatasetFromPath(@PathParam("id") final int id, final SharedDataSetModel params) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        final SharedDataset mySharedDataSet = this.sharedDatasetService.getFromId(id);
        if (mySharedDataSet == null) {
            SharedDatasetRestResource.logger.warn("Data set is not found");
            return Response.status(404).build();
        }
        final boolean isAdmin = this.sharedDatasetService.isAdminForDataSet(mySharedDataSet);
        if (!isAdmin) {
            final String userKey = this.jiraAuthenticationContext.getLoggedInUser().getKey();
            SharedDatasetRestResource.logger.warn("Logged in user: " + userKey + " is not admin for data set with id: " + mySharedDataSet.getID());
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.update(id, params);
        final List<OwnerModel> ownerModelList = this.ownerService.getOwnerList(sharedDataset);
        final SharedDataSetModel sharedDataSetObj = new SharedDataSetModel(sharedDataset);
        sharedDataSetObj.setAdmin(true);
        sharedDataSetObj.setOwners(ownerModelList);
        if (params.isStarred() == null) {
            final Config config = this.configService.getConfig(sharedDataset);
            sharedDataSetObj.setStarred(config.isStarred());
        }
        else {
            final Config config = this.configService.updateConfig(sharedDataset, params.isStarred());
            sharedDataSetObj.setStarred(config.isStarred());
        }
        final List<SharedDataSetModel> sharedDatasetList = new ArrayList<SharedDataSetModel>();
        sharedDatasetList.add(sharedDataSetObj);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), sharedDatasetList)).build();
    }
    
    @PUT
    @Produces({ "application/json" })
    @Consumes({ "application/json" })
    @Path("/{id}/starred")
    public Response updateStarredSharedDatasetFromPath(@PathParam("id") final int id, final StarredModel starredParam) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        final SharedDataset sharedDataSet = this.sharedDatasetService.getFromId(id);
        if (sharedDataSet == null) {
            SharedDatasetRestResource.logger.warn("Data set is not found");
            return Response.status(404).build();
        }
        final List<OwnerModel> ownerModelList = this.ownerService.getOwnerList(sharedDataSet);
        final SharedDataSetModel sharedDataSetObj = new SharedDataSetModel(sharedDataSet);
        sharedDataSetObj.setAdmin(this.sharedDatasetService.isAdminForDataSet(sharedDataSet));
        sharedDataSetObj.setOwners(ownerModelList);
        final Config config = this.configService.updateConfig(sharedDataSet, starredParam.isStarred());
        sharedDataSetObj.setStarred(config.isStarred());
        final List<SharedDataSetModel> sharedDatasetList = new ArrayList<SharedDataSetModel>();
        sharedDatasetList.add(sharedDataSetObj);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), sharedDatasetList)).build();
    }
    
    @DELETE
    @Produces({ "application/json" })
    @Path("/{id}")
    public Response removeSharedDatasetFromPath(@PathParam("id") final int id) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            SharedDatasetRestResource.logger.warn("Is not logged in");
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            SharedDatasetRestResource.logger.warn("Data set is not found");
            return Response.status(410).build();
        }
        this.sharedDatasetService.removeSharedDataSet(sharedDataset);
        return Response.ok((Object)true).build();
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)SharedDatasetRestResource.class);
    }
}
