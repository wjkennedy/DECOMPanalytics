// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import java.util.Map;
import org.osgi.framework.BundleContext;

public interface OsgiServiceFactoryBeanFactory
{
    GenericOsgiServiceFactoryBean createExporter(final BundleContext p0, final Object p1, final String p2, final Map<String, Object> p3, final Class<?>[] p4) throws Exception;
    
    Class getProxyClass();
}
