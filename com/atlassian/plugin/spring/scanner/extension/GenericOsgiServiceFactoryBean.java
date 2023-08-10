// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import org.osgi.framework.ServiceRegistration;

public interface GenericOsgiServiceFactoryBean
{
    ServiceRegistration getObject() throws Exception;
    
    void destroy();
}
