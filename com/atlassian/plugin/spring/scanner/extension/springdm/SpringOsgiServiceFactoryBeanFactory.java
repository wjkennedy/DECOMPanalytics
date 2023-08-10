// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension.springdm;

import org.springframework.osgi.service.importer.support.OsgiServiceProxyFactoryBean;
import org.springframework.osgi.service.exporter.support.ExportContextClassLoader;
import org.springframework.osgi.service.exporter.support.AutoExport;
import org.springframework.osgi.service.exporter.support.OsgiServiceFactoryBean;
import com.atlassian.plugin.spring.scanner.extension.GenericOsgiServiceFactoryBean;
import java.util.Map;
import org.osgi.framework.BundleContext;
import com.atlassian.plugin.spring.scanner.extension.OsgiServiceFactoryBeanFactory;

public class SpringOsgiServiceFactoryBeanFactory implements OsgiServiceFactoryBeanFactory
{
    @Override
    public GenericOsgiServiceFactoryBean createExporter(final BundleContext bundleContext, final Object bean, final String beanName, final Map<String, Object> serviceProps, final Class<?>[] interfaces) throws Exception {
        serviceProps.put("org.springframework.osgi.bean.name", beanName);
        final OsgiServiceFactoryBean exporter = new OsgiServiceFactoryBean();
        exporter.setAutoExport(AutoExport.DISABLED);
        exporter.setBeanClassLoader(bean.getClass().getClassLoader());
        exporter.setBeanName(beanName);
        exporter.setBundleContext(bundleContext);
        exporter.setContextClassLoader(ExportContextClassLoader.UNMANAGED);
        exporter.setInterfaces((Class[])interfaces);
        exporter.setServiceProperties((Map)serviceProps);
        exporter.setTarget(bean);
        exporter.afterPropertiesSet();
        return new SpringOsgiServiceFactoryBean(exporter);
    }
    
    @Override
    public Class getProxyClass() {
        return OsgiServiceProxyFactoryBean.class;
    }
}
