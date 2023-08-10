// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerAddModel;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerModel;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.models.ListRestResourceModel;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import se.fiftyfivedegrees.ao.shareddataset.services.OwnerService;
import se.fiftyfivedegrees.ao.shareddataset.services.SharedDatasetService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import javax.ws.rs.Path;

@Path("/shared-dataset/{id}/owner")
public class OwnerRestResource
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final SharedDatasetService sharedDatasetService;
    @ComponentImport
    private final OwnerService ownerService;
    
    public OwnerRestResource(final JiraAuthenticationContext jiraAuthenticationContext, final SharedDatasetService sharedDatasetService, final OwnerService ownerService) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.sharedDatasetService = sharedDatasetService;
        this.ownerService = ownerService;
    }
    
    @GET
    @Produces({ "application/json" })
    public Response getOwnersFromId(@PathParam("id") final int id) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        final List<OwnerModel> ownerList = this.ownerService.getOwnerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), ownerList)).build();
    }
    
    @POST
    @Produces({ "application/json" })
    @Consumes({ "application/json" })
    public Response assignOwnerToSharedDataset(@PathParam("id") final int id, final OwnerAddModel params) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        if (params.getOwnerId() == null) {
            return Response.status(406).build();
        }
        final String ownerID = params.getOwnerId();
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        this.sharedDatasetService.assignOwnerById(sharedDataset, ownerID);
        final List<OwnerModel> ownerList = this.ownerService.getOwnerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), ownerList)).build();
    }
    
    @DELETE
    @Produces({ "application/json" })
    @Path("{ownerKey}")
    public Response unassignOwnerToSharedDataset(@PathParam("id") final int id, @PathParam("ownerKey") final String ownerID) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        this.sharedDatasetService.unassignOwnerById(sharedDataset, ownerID);
        final List<OwnerModel> ownerList = this.ownerService.getOwnerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), ownerList)).build();
    }
}
