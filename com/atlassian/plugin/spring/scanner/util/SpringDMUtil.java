// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.util;

import com.atlassian.plugin.spring.scanner.extension.gemini.GeminiOsgiBundleContextAccessor;
import com.atlassian.plugin.spring.scanner.extension.gemini.GeminiOsgiServiceFactoryBeanFactory;
import com.atlassian.plugin.spring.scanner.extension.springdm.SpringOsgiBundleContextAccessor;
import com.atlassian.plugin.spring.scanner.extension.springdm.SpringOsgiServiceFactoryBeanFactory;
import com.atlassian.plugin.spring.scanner.extension.OsgiBundleContextAccessor;
import com.atlassian.plugin.spring.scanner.extension.OsgiServiceFactoryBeanFactory;

public class SpringDMUtil
{
    private static SpringDMUtil INSTANCE;
    public static final String SPRING_DM_CLASS = "org.springframework.osgi.service.importer.support.OsgiServiceProxyFactoryBean";
    public static final String GEMINI_DM_CLASS = "org.eclipse.gemini.blueprint.service.importer.support.OsgiServiceProxyFactoryBean";
    private OsgiServiceFactoryBeanFactory osgiServiceFactoryBeanFactory;
    private OsgiBundleContextAccessor osgiBundleContextAccessor;
    
    private SpringDMUtil() {
        try {
            Class.forName("org.springframework.osgi.service.importer.support.OsgiServiceProxyFactoryBean");
            this.osgiServiceFactoryBeanFactory = new SpringOsgiServiceFactoryBeanFactory();
            this.osgiBundleContextAccessor = new SpringOsgiBundleContextAccessor();
        }
        catch (final ClassNotFoundException e) {
            try {
                Class.forName("org.eclipse.gemini.blueprint.service.importer.support.OsgiServiceProxyFactoryBean");
                this.osgiServiceFactoryBeanFactory = new GeminiOsgiServiceFactoryBeanFactory();
                this.osgiBundleContextAccessor = new GeminiOsgiBundleContextAccessor();
            }
            catch (final ClassNotFoundException cnfe) {
                throw new RuntimeException("Couldn't load spring dm nor gemini blueprint osgi service proxy factory bean class! Check you are running on a supported version of atlassian-plugins", cnfe);
            }
        }
    }
    
    public static SpringDMUtil getInstance() {
        if (SpringDMUtil.INSTANCE == null) {
            SpringDMUtil.INSTANCE = new SpringDMUtil();
        }
        return SpringDMUtil.INSTANCE;
    }
    
    public OsgiServiceFactoryBeanFactory getOsgiServiceFactoryBeanFactory() {
        return this.osgiServiceFactoryBeanFactory;
    }
    
    public OsgiBundleContextAccessor getOsgiBundleContextAccessor() {
        return this.osgiBundleContextAccessor;
    }
}
