// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import com.atlassian.plugin.spring.scanner.util.SpringDMUtil;
import org.osgi.framework.ServiceRegistration;
import java.util.Map;
import org.osgi.framework.BundleContext;
import java.util.Hashtable;

public class ExportedSeviceManager
{
    private final Hashtable<Integer, GenericOsgiServiceFactoryBean> exporters;
    
    public ExportedSeviceManager() {
        this.exporters = new Hashtable<Integer, GenericOsgiServiceFactoryBean>();
    }
    
    public ServiceRegistration registerService(final BundleContext bundleContext, final Object bean, final String beanName, final Map<String, Object> serviceProps, final Class<?>... interfaces) throws Exception {
        final GenericOsgiServiceFactoryBean osgiExporter = this.createExporter(bundleContext, bean, beanName, serviceProps, interfaces);
        final int hashCode = System.identityHashCode(bean);
        this.exporters.put(hashCode, osgiExporter);
        return osgiExporter.getObject();
    }
    
    public boolean hasService(final Object bean) {
        final int hashCode = System.identityHashCode(bean);
        return this.exporters.containsKey(hashCode);
    }
    
    public void unregisterService(final BundleContext bundleContext, final Object bean) {
        final int hashCode = System.identityHashCode(bean);
        final GenericOsgiServiceFactoryBean exporter = this.exporters.get(hashCode);
        if (null != exporter) {
            exporter.destroy();
            this.exporters.remove(hashCode);
        }
    }
    
    private GenericOsgiServiceFactoryBean createExporter(final BundleContext bundleContext, final Object bean, final String beanName, final Map<String, Object> serviceProps, final Class<?>... interfaces) throws Exception {
        final OsgiServiceFactoryBeanFactory osgiServiceFactoryBeanFactory = SpringDMUtil.getInstance().getOsgiServiceFactoryBeanFactory();
        return osgiServiceFactoryBeanFactory.createExporter(bundleContext, bean, beanName, serviceProps, interfaces);
    }
}
