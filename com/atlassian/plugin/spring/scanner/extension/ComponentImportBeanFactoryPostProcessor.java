// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import org.springframework.beans.factory.config.BeanDefinition;
import com.atlassian.plugin.osgi.factory.OsgiPlugin;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import com.atlassian.plugin.spring.scanner.util.SpringDMUtil;
import java.util.Properties;
import java.net.URL;
import org.springframework.beans.BeansException;
import java.util.Iterator;
import java.util.Set;
import org.osgi.framework.Bundle;
import org.apache.commons.lang.StringUtils;
import java.util.Collection;
import java.util.TreeSet;
import com.atlassian.plugin.spring.scanner.util.AnnotationIndexReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class ComponentImportBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    private final BundleContext bundleContext;
    private boolean autoImports;
    private String profileName;
    private final Log log;
    
    public ComponentImportBeanFactoryPostProcessor(final BundleContext bundleContext) {
        this.log = LogFactory.getLog((Class)this.getClass());
        this.bundleContext = bundleContext;
        this.autoImports = false;
        this.profileName = null;
    }
    
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
        final Bundle bundle = this.bundleContext.getBundle();
        final String[] profileNames = AnnotationIndexReader.splitProfiles(this.profileName);
        final Set<String> classNames = new TreeSet<String>();
        for (final String fileToRead : AnnotationIndexReader.getIndexFilesForProfiles(profileNames, "imports")) {
            classNames.addAll(AnnotationIndexReader.readAllIndexFilesForProduct(fileToRead, bundle));
        }
        for (final String className : classNames) {
            final String[] typeAndName = StringUtils.split(className, "#");
            final String beanType = typeAndName[0];
            final String beanName = (typeAndName.length > 1) ? typeAndName[1] : "";
            try {
                final Class beanClass = beanFactory.getBeanClassLoader().loadClass(beanType);
                this.registerComponentImportBean(registry, beanClass, beanName);
            }
            catch (final ClassNotFoundException e) {
                this.log.error((Object)("Unable to load class '" + beanType + "' for component importation purposes.  Skipping..."));
            }
        }
        this.processMetaData(registry, profileNames);
    }
    
    private void processMetaData(final BeanDefinitionRegistry registry, final String[] profileNames) {
        for (final String fileToRead : AnnotationIndexReader.getIndexFilesForProfiles(profileNames, "metadata.properties")) {
            final URL url = this.bundleContext.getBundle().getResource(fileToRead);
            this.processMetaDataProperties(registry, AnnotationIndexReader.readPropertiesFile(url));
        }
    }
    
    private void processMetaDataProperties(final BeanDefinitionRegistry registry, final Properties properties) {
    }
    
    private void registerComponentImportBean(final BeanDefinitionRegistry registry, final Class beanClass, final String beanName) {
        String serviceBeanName = beanName;
        if ("".equals(serviceBeanName)) {
            serviceBeanName = StringUtils.uncapitalize(beanClass.getSimpleName());
        }
        this.registerBeanDefinition(registry, serviceBeanName, "(objectClass=" + beanClass.getName() + ")", beanClass);
    }
    
    private void registerBeanDefinition(final BeanDefinitionRegistry registry, final String beanName, final String filter, final Class interfaces) {
        final Class osgiServiceProxyFactoryBeanClass = SpringDMUtil.getInstance().getOsgiServiceFactoryBeanFactory().getProxyClass();
        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(osgiServiceProxyFactoryBeanClass);
        builder.setAutowireMode(3);
        builder.setRole(2);
        if (StringUtils.isNotBlank(filter)) {
            builder.addPropertyValue("filter", (Object)filter);
        }
        builder.addPropertyValue("interfaces", (Object)new Class[] { interfaces });
        builder.addPropertyValue("beanClassLoader", (Object)OsgiPlugin.class.getClassLoader());
        final BeanDefinition newDefinition = (BeanDefinition)builder.getBeanDefinition();
        registry.registerBeanDefinition(beanName, newDefinition);
    }
    
    public void setAutoImports(final boolean autoImports) {
        this.autoImports = autoImports;
    }
    
    public void setProfileName(final String profileName) {
        this.profileName = profileName;
    }
}
