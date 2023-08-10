// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;
import com.atlassian.jira.util.JiraUrlCodec;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.inject.Inject;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.actionableagile.analytics.services.LicenseService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import javax.servlet.http.HttpServlet;

@Scanned
public class ActionableAgileServlet extends HttpServlet
{
    @ComponentImport
    private final TemplateRenderer templateRenderer;
    private final LicenseService licenseService;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    
    @Inject
    public ActionableAgileServlet(final TemplateRenderer templateRenderer, final LicenseService licenseService, final JiraAuthenticationContext jiraAuthenticationContext) {
        this.templateRenderer = templateRenderer;
        this.licenseService = licenseService;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }
    
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (!this.jiraAuthenticationContext.isLoggedInUser()) {
            final String returnURL = "/plugins/servlet/fiftyfivedegrees/actionableagile/main";
            String contextPath = "";
            if (req.getContextPath() != null) {
                contextPath = req.getContextPath();
            }
            resp.sendRedirect(contextPath + "/login.jsp?os_destination=" + JiraUrlCodec.encode(returnURL));
        }
        else {
            final Map<String, Object> context = new HashMap<String, Object>();
            if (this.licenseService.isValidLicense()) {
                context.put("userKey", this.jiraAuthenticationContext.getLoggedInUser().getKey());
                this.templateRenderer.render("templates/v4/main.vm", (Map)context, (Writer)resp.getWriter());
            }
            else {
                this.templateRenderer.render("templates/v4/license-expired.vm", (Writer)resp.getWriter());
            }
        }
    }
}
