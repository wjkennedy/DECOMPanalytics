// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension.gemini;

import org.apache.commons.logging.LogFactory;
import org.eclipse.gemini.blueprint.context.ConfigurableOsgiBundleApplicationContext;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.ResourceLoader;
import org.apache.commons.logging.Log;
import com.atlassian.plugin.spring.scanner.extension.OsgiBundleContextAccessor;

public class GeminiOsgiBundleContextAccessor implements OsgiBundleContextAccessor
{
    private static final Log log;
    
    @Override
    public BundleContext getBundleContext(final ResourceLoader resourceLoader) {
        if (!(resourceLoader instanceof ConfigurableOsgiBundleApplicationContext)) {
            GeminiOsgiBundleContextAccessor.log.warn((Object)("Could not access BundleContext from ResourceLoader: expected resourceLoader to be an instance of " + ConfigurableOsgiBundleApplicationContext.class.getName() + ": got " + resourceLoader.getClass().getName()));
            return null;
        }
        return ((ConfigurableOsgiBundleApplicationContext)resourceLoader).getBundleContext();
    }
    
    static {
        log = LogFactory.getLog((Class)GeminiOsgiBundleContextAccessor.class);
    }
}
