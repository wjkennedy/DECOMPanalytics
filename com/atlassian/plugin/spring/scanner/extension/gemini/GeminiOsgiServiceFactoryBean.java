// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension.gemini;

import org.osgi.framework.ServiceRegistration;
import org.eclipse.gemini.blueprint.service.exporter.support.OsgiServiceFactoryBean;
import com.atlassian.plugin.spring.scanner.extension.GenericOsgiServiceFactoryBean;

public class GeminiOsgiServiceFactoryBean implements GenericOsgiServiceFactoryBean
{
    private final OsgiServiceFactoryBean exporter;
    
    public GeminiOsgiServiceFactoryBean(final OsgiServiceFactoryBean exporter) {
        this.exporter = exporter;
    }
    
    @Override
    public ServiceRegistration getObject() throws Exception {
        return this.exporter.getObject();
    }
    
    @Override
    public void destroy() {
        this.exporter.destroy();
    }
}
