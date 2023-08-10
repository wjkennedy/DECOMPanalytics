// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import java.util.Map;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import java.util.HashMap;
import org.springframework.util.ClassUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import javax.annotation.Nullable;
import com.atlassian.plugin.spring.scanner.util.SpringDMUtil;
import java.util.Iterator;
import java.util.Collection;
import org.springframework.context.annotation.AnnotationConfigUtils;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import java.util.Set;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

public class AtlassianScannerBeanDefinitionParser implements BeanDefinitionParser
{
    private static final String AUTO_IMPORTS_ATTRIBUTE = "auto-imports";
    private static final String PROFILE_ATTRIBUTE = "profile";
    public static final String JAVAX_INJECT_CLASSNAME = "javax.inject.Inject";
    private final Log log;
    
    public AtlassianScannerBeanDefinitionParser() {
        this.log = LogFactory.getLog((Class)this.getClass());
    }
    
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        boolean autoImports = false;
        String profileName = null;
        Integer autowireDefault = null;
        if (element.hasAttribute("auto-imports")) {
            autoImports = Boolean.parseBoolean(element.getAttribute("auto-imports"));
        }
        if (element.hasAttribute("profile")) {
            profileName = element.getAttribute("profile");
        }
        if (element.hasAttribute("autowire")) {
            autowireDefault = parserContext.getDelegate().getAutowireMode(element.getAttribute("autowire"));
        }
        final BundleContext bundleContext = this.getBundleContext(parserContext);
        final ClassIndexBeanDefinitionScanner scanner = new ClassIndexBeanDefinitionScanner(parserContext.getReaderContext().getRegistry(), profileName, autowireDefault, bundleContext);
        final Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan();
        this.registerComponents(parserContext.getReaderContext(), beanDefinitions, element, autoImports, profileName);
        return null;
    }
    
    protected void registerComponents(final XmlReaderContext readerContext, final Set<BeanDefinitionHolder> beanDefinitions, final Element element, final boolean autoImports, final String profileName) {
        final Object source = readerContext.extractSource((Object)element);
        final CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
        for (final BeanDefinitionHolder beanDefHolder : beanDefinitions) {
            compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(beanDefHolder));
        }
        final Set<BeanDefinitionHolder> processorDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
        processorDefinitions.addAll(AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source));
        final BeanDefinitionHolder javaxInject = this.getJavaxInjectPostProcessor(readerContext.getRegistry(), source);
        if (null != javaxInject) {
            processorDefinitions.add(javaxInject);
        }
        processorDefinitions.add(this.getComponentImportPostProcessor(readerContext.getRegistry(), source, autoImports, profileName));
        processorDefinitions.add(this.getServiceExportPostProcessor(readerContext.getRegistry(), source, profileName));
        processorDefinitions.add(this.getDevModeBeanInitialisationLoggerPostProcessor(readerContext.getRegistry(), source));
        for (final BeanDefinitionHolder processorDefinition : processorDefinitions) {
            compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(processorDefinition));
        }
        readerContext.fireComponentRegistered((ComponentDefinition)compositeDef);
    }
    
    @Nullable
    private BundleContext getBundleContext(final ParserContext parserContext) {
        return SpringDMUtil.getInstance().getOsgiBundleContextAccessor().getBundleContext(parserContext.getReaderContext().getResourceLoader());
    }
    
    private BeanDefinitionHolder getJavaxInjectPostProcessor(final BeanDefinitionRegistry registry, final Object source) {
        if (ClassUtils.isPresent("javax.inject.Inject", this.getClass().getClassLoader())) {
            try {
                final Class injectClass = this.getClass().getClassLoader().loadClass("javax.inject.Inject");
                final Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("autowiredAnnotationType", injectClass);
                final RootBeanDefinition def = new RootBeanDefinition((Class)AutowiredAnnotationBeanPostProcessor.class);
                def.setSource(source);
                def.setRole(2);
                def.setPropertyValues(new MutablePropertyValues((Map)properties));
                return this.registerBeanPostProcessor(registry, def, "javaxInjectBeanPostProcessor");
            }
            catch (final ClassNotFoundException e) {
                this.log.error((Object)"Unable to load class 'javax.inject.Inject' for javax component purposes.  Not sure how this is possible.  Skipping...");
            }
        }
        return null;
    }
    
    private BeanDefinitionHolder registerBeanPostProcessor(final BeanDefinitionRegistry registry, final RootBeanDefinition definition, final String beanName) {
        definition.setRole(2);
        registry.registerBeanDefinition(beanName, (BeanDefinition)definition);
        return new BeanDefinitionHolder((BeanDefinition)definition, beanName);
    }
    
    private BeanDefinitionHolder getComponentImportPostProcessor(final BeanDefinitionRegistry registry, final Object source, final boolean autoImports, final String profileName) {
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("autoImports", autoImports);
        properties.put("profileName", profileName);
        final RootBeanDefinition rootBeanDefinition = new RootBeanDefinition((Class)ComponentImportBeanFactoryPostProcessor.class);
        rootBeanDefinition.setAutowireMode(3);
        rootBeanDefinition.setSource(source);
        rootBeanDefinition.setPropertyValues(new MutablePropertyValues((Map)properties));
        return this.registerBeanPostProcessor(registry, rootBeanDefinition, "componentImportBeanFactoryPostProcessor");
    }
    
    private BeanDefinitionHolder getServiceExportPostProcessor(final BeanDefinitionRegistry registry, final Object source, final String profileName) {
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("profileName", profileName);
        final RootBeanDefinition rootBeanDefinition = new RootBeanDefinition((Class)ServiceExporterBeanPostProcessor.class);
        rootBeanDefinition.setSource(source);
        rootBeanDefinition.setAutowireMode(3);
        rootBeanDefinition.setPropertyValues(new MutablePropertyValues((Map)properties));
        return this.registerBeanPostProcessor(registry, rootBeanDefinition, "serviceExportBeanPostProcessor");
    }
    
    private BeanDefinitionHolder getDevModeBeanInitialisationLoggerPostProcessor(final BeanDefinitionRegistry registry, final Object source) {
        final RootBeanDefinition def = new RootBeanDefinition((Class)DevModeBeanInitialisationLoggerBeanPostProcessor.class);
        def.setSource(source);
        def.setAutowireMode(3);
        return this.registerBeanPostProcessor(registry, def, "devModeBeanInitialisationLoggerBeanPostProcessor");
    }
}
