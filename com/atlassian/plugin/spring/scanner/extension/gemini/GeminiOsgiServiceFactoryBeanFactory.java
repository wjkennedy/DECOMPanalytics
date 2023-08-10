// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension.gemini;

import org.eclipse.gemini.blueprint.service.importer.support.OsgiServiceProxyFactoryBean;
import org.eclipse.gemini.blueprint.service.exporter.OsgiServiceRegistrationListener;
import org.eclipse.gemini.blueprint.service.exporter.support.ExportContextClassLoaderEnum;
import org.eclipse.gemini.blueprint.service.exporter.support.InterfaceDetector;
import org.eclipse.gemini.blueprint.service.exporter.support.DefaultInterfaceDetector;
import org.eclipse.gemini.blueprint.service.exporter.support.OsgiServiceFactoryBean;
import com.atlassian.plugin.spring.scanner.extension.GenericOsgiServiceFactoryBean;
import java.util.Map;
import org.osgi.framework.BundleContext;
import com.atlassian.plugin.spring.scanner.extension.OsgiServiceFactoryBeanFactory;

public class GeminiOsgiServiceFactoryBeanFactory implements OsgiServiceFactoryBeanFactory
{
    @Override
    public GenericOsgiServiceFactoryBean createExporter(final BundleContext bundleContext, final Object bean, final String beanName, final Map<String, Object> serviceProps, final Class<?>[] interfaces) throws Exception {
        serviceProps.put("org.eclipse.gemini.blueprint.bean.name", beanName);
        final OsgiServiceFactoryBean exporter = new OsgiServiceFactoryBean();
        exporter.setInterfaceDetector((InterfaceDetector)DefaultInterfaceDetector.DISABLED);
        exporter.setBeanClassLoader(bean.getClass().getClassLoader());
        exporter.setBeanName(beanName);
        exporter.setBundleContext(bundleContext);
        exporter.setExportContextClassLoader(ExportContextClassLoaderEnum.UNMANAGED);
        exporter.setInterfaces((Class[])interfaces);
        exporter.setServiceProperties((Map)serviceProps);
        exporter.setTarget(bean);
        exporter.setListeners(new OsgiServiceRegistrationListener[0]);
        exporter.afterPropertiesSet();
        return new GeminiOsgiServiceFactoryBean(exporter);
    }
    
    @Override
    public Class getProxyClass() {
        return OsgiServiceProxyFactoryBean.class;
    }
}
