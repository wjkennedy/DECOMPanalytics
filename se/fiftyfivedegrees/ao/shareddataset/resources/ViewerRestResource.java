// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import se.fiftyfivedegrees.ao.shareddataset.models.ViewerAddModel;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.models.ListRestResourceModel;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import se.fiftyfivedegrees.ao.shareddataset.services.ViewerService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import se.fiftyfivedegrees.ao.shareddataset.services.SharedDatasetService;
import javax.ws.rs.Path;

@Path("/shared-dataset/{id}/viewer")
public class ViewerRestResource
{
    @ComponentImport
    private final SharedDatasetService sharedDatasetService;
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;
    @ComponentImport
    private final ViewerService viewerService;
    
    public ViewerRestResource(final SharedDatasetService sharedDatasetService, final JiraAuthenticationContext jiraAuthenticationContext, final ViewerService viewerService) {
        this.sharedDatasetService = sharedDatasetService;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.viewerService = viewerService;
    }
    
    @GET
    @Produces({ "application/json" })
    public Response getViewersFromId(@PathParam("id") final int id) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        final List<Object> viewerList = this.viewerService.getViewerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), viewerList)).build();
    }
    
    @POST
    @Produces({ "application/json" })
    @Consumes({ "application/json" })
    public Response assignViewerToSharedDataset(@PathParam("id") final int id, final ViewerAddModel params) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        if (params.getKey() == null || params.getType() == null) {
            return Response.status(406).build();
        }
        final String key = params.getKey();
        final String type = params.getType();
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        this.sharedDatasetService.assignViewerById(sharedDataset, key, type);
        final List<Object> viewerList = this.viewerService.getViewerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), viewerList)).build();
    }
    
    @DELETE
    @Produces({ "application/json" })
    @Path("{viewerId}")
    public Response unassignViewerToSharedDataset(@PathParam("id") final int id, @PathParam("viewerId") final String viewerId) {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            return Response.status(403).build();
        }
        final SharedDataset sharedDataset = this.sharedDatasetService.getFromId(id);
        if (sharedDataset == null) {
            return Response.status(404).build();
        }
        this.sharedDatasetService.unassignViewerById(sharedDataset, viewerId);
        final List<Object> viewerList = this.viewerService.getViewerList(sharedDataset);
        return Response.ok((Object)new ListRestResourceModel(Integer.toString(id), viewerList)).build();
    }
}
