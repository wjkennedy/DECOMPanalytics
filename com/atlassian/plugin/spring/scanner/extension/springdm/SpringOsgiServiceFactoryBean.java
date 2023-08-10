// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension.springdm;

import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.service.exporter.support.OsgiServiceFactoryBean;
import com.atlassian.plugin.spring.scanner.extension.GenericOsgiServiceFactoryBean;

public class SpringOsgiServiceFactoryBean implements GenericOsgiServiceFactoryBean
{
    private final OsgiServiceFactoryBean osgiServiceFactoryBean;
    
    public SpringOsgiServiceFactoryBean(final OsgiServiceFactoryBean osgiServiceFactoryBean) {
        this.osgiServiceFactoryBean = osgiServiceFactoryBean;
    }
    
    @Override
    public ServiceRegistration getObject() throws Exception {
        return (ServiceRegistration)this.osgiServiceFactoryBean.getObject();
    }
    
    @Override
    public void destroy() {
        this.osgiServiceFactoryBean.destroy();
    }
}
