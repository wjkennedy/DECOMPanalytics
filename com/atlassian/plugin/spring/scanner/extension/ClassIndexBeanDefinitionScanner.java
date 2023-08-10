// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import java.beans.Introspector;
import org.springframework.util.ClassUtils;
import org.apache.commons.lang.StringUtils;
import java.util.Collection;
import com.atlassian.plugin.spring.scanner.util.AnnotationIndexReader;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Iterator;
import com.atlassian.plugin.spring.scanner.util.BeanDefinitionChecker;
import org.springframework.beans.factory.config.BeanDefinition;
import java.util.Map;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import java.util.Set;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.apache.commons.logging.Log;

public class ClassIndexBeanDefinitionScanner
{
    protected final Log log;
    private final BeanDefinitionRegistry registry;
    private final String profileName;
    private final Integer autowireDefault;
    private final BundleContext bundleContext;
    
    public ClassIndexBeanDefinitionScanner(final BeanDefinitionRegistry registry, final String profileName, final Integer autowireDefault, final BundleContext bundleContext) {
        this.log = LogFactory.getLog((Class)this.getClass());
        this.registry = registry;
        this.profileName = profileName;
        this.autowireDefault = autowireDefault;
        this.bundleContext = bundleContext;
    }
    
    protected Set<BeanDefinitionHolder> doScan() {
        final Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
        final Map<String, BeanDefinition> namesAndDefinitions = this.findCandidateComponents();
        for (final Map.Entry<String, BeanDefinition> nameAndDefinition : namesAndDefinitions.entrySet()) {
            if (BeanDefinitionChecker.needToRegister(nameAndDefinition.getKey(), nameAndDefinition.getValue(), this.registry)) {
                final BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder((BeanDefinition)nameAndDefinition.getValue(), (String)nameAndDefinition.getKey());
                beanDefinitions.add(definitionHolder);
                this.registerBeanDefinition(definitionHolder, this.registry);
            }
        }
        return beanDefinitions;
    }
    
    public Map<String, BeanDefinition> findCandidateComponents() {
        final Map<String, BeanDefinition> candidates = new HashMap<String, BeanDefinition>();
        final Set<String> beanTypeAndNames = new TreeSet<String>();
        final String[] profileNames = AnnotationIndexReader.splitProfiles(this.profileName);
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (final String fileToRead : AnnotationIndexReader.getIndexFilesForProfiles(profileNames, "component")) {
            beanTypeAndNames.addAll(AnnotationIndexReader.readAllIndexFilesForProduct(fileToRead, contextClassLoader, this.bundleContext));
        }
        for (final String beanTypeAndName : beanTypeAndNames) {
            final String[] typeAndName = StringUtils.split(beanTypeAndName, "#");
            final String beanClassName = typeAndName[0];
            String beanName = "";
            if (typeAndName.length > 1) {
                beanName = typeAndName[1];
            }
            if (StringUtils.isBlank(beanName)) {
                beanName = Introspector.decapitalize(ClassUtils.getShortName(beanClassName));
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug((Object)String.format("Found candidate bean '%s' from class '%s'", beanName, beanClassName));
            }
            final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClassName);
            if (null != this.autowireDefault) {
                beanDefinitionBuilder.setAutowireMode((int)this.autowireDefault);
            }
            candidates.put(beanName, (BeanDefinition)beanDefinitionBuilder.getBeanDefinition());
        }
        return candidates;
    }
    
    protected void registerBeanDefinition(final BeanDefinitionHolder definitionHolder, final BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }
}
