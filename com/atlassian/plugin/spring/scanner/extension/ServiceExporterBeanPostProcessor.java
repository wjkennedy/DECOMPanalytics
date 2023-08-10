// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import org.osgi.framework.ServiceRegistration;
import java.util.Map;
import java.util.Hashtable;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsDevService;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import java.lang.annotation.Annotation;
import com.atlassian.plugin.spring.scanner.annotation.export.ModuleType;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import java.util.List;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import com.atlassian.plugin.spring.scanner.util.AnnotationIndexReader;
import org.apache.commons.logging.LogFactory;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.osgi.framework.BundleContext;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class ServiceExporterBeanPostProcessor implements DestructionAwareBeanPostProcessor, InitializingBean
{
    public static final String OSGI_SERVICE_SUFFIX = "_osgiService";
    static final String ATLASSIAN_DEV_MODE_PROP = "atlassian.dev.mode";
    private final Log log;
    private final boolean isDevMode;
    private final BundleContext bundleContext;
    private ConfigurableListableBeanFactory beanFactory;
    private String profileName;
    private final ExportedSeviceManager serviceManager;
    private ImmutableMap<String, String[]> exports;
    
    public ServiceExporterBeanPostProcessor(final BundleContext bundleContext, final ConfigurableListableBeanFactory beanFactory) {
        this(bundleContext, beanFactory, new ExportedSeviceManager());
    }
    
    ServiceExporterBeanPostProcessor(final BundleContext bundleContext, final ConfigurableListableBeanFactory beanFactory, final ExportedSeviceManager serviceManager) {
        this.log = LogFactory.getLog((Class)this.getClass());
        this.isDevMode = Boolean.parseBoolean(System.getProperty("atlassian.dev.mode", "false"));
        this.bundleContext = bundleContext;
        this.beanFactory = beanFactory;
        this.profileName = null;
        this.serviceManager = serviceManager;
    }
    
    public void setProfileName(final String profileName) {
        this.profileName = profileName;
    }
    
    public void afterPropertiesSet() throws Exception {
        final ImmutableMap.Builder<String, String[]> exportBuilder = (ImmutableMap.Builder<String, String[]>)ImmutableMap.builder();
        final String[] profileNames = AnnotationIndexReader.splitProfiles(this.profileName);
        this.parseExportsForExportFile(exportBuilder, "exports", profileNames);
        if (this.isDevMode) {
            this.parseExportsForExportFile(exportBuilder, "dev-exports", profileNames);
        }
        this.exports = (ImmutableMap<String, String[]>)exportBuilder.build();
    }
    
    private void parseExportsForExportFile(final ImmutableMap.Builder<String, String[]> exportBuilder, final String exportFileName, final String[] profileNames) {
        final String[] defaultInterfaces = new String[0];
        for (final String fileToRead : AnnotationIndexReader.getIndexFilesForProfiles(profileNames, exportFileName)) {
            final List<String> exportData = AnnotationIndexReader.readAllIndexFilesForProduct(fileToRead, this.bundleContext.getBundle());
            for (final String export : exportData) {
                final String[] targetAndInterfaces = StringUtils.split(export, '#');
                final String target = targetAndInterfaces[0];
                final String[] interfaces = (targetAndInterfaces.length > 1) ? StringUtils.split(targetAndInterfaces[1], ',') : defaultInterfaces;
                exportBuilder.put((Object)target, (Object)interfaces);
            }
        }
    }
    
    public void postProcessBeforeDestruction(final Object bean, final String beanName) throws BeansException {
        if (this.serviceManager.hasService(bean)) {
            this.serviceManager.unregisterService(this.bundleContext, bean);
            final String serviceName = this.getServiceName(beanName);
            if (this.beanFactory.containsBean(serviceName)) {
                final Object serviceBean = this.beanFactory.getBean(serviceName);
                if (null != serviceBean) {
                    this.beanFactory.destroyBean(serviceName, serviceBean);
                }
            }
        }
    }
    
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
    
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Class<?>[] interfaces = new Class[0];
        final Class<?> beanTargetClass = AopUtils.getTargetClass(bean);
        final String beanClassName = beanTargetClass.getName();
        if (this.exports.containsKey((Object)beanClassName) || this.isPublicComponent(beanTargetClass)) {
            if (this.exports.containsKey((Object)beanClassName)) {
                final ImmutableList.Builder<Class<?>> interfaceBuilder = (ImmutableList.Builder<Class<?>>)ImmutableList.builder();
                final ClassLoader beanClassLoader = bean.getClass().getClassLoader();
                final String[] arr$;
                final String[] interfaceNames = arr$ = (String[])this.exports.get((Object)beanClassName);
                for (final String interfaceName : arr$) {
                    try {
                        final Class interfaceClass = beanClassLoader.loadClass(interfaceName);
                        interfaceBuilder.add((Object)interfaceClass);
                    }
                    catch (final ClassNotFoundException ecnf) {
                        this.log.warn((Object)("Cannot find class for export '" + interfaceName + "' of bean '" + beanName + "': " + ecnf));
                    }
                }
                interfaces = (Class[])Iterables.toArray((Iterable)interfaceBuilder.build(), (Class)Class.class);
            }
            else if (this.hasAnnotation(beanTargetClass, ModuleType.class)) {
                interfaces = beanTargetClass.getAnnotation(ModuleType.class).value();
            }
            else if (this.hasAnnotation(beanTargetClass, ExportAsService.class)) {
                interfaces = beanTargetClass.getAnnotation(ExportAsService.class).value();
            }
            else if (this.hasAnnotation(beanTargetClass, ExportAsDevService.class)) {
                interfaces = beanTargetClass.getAnnotation(ExportAsDevService.class).value();
            }
            if (interfaces.length < 1) {
                interfaces = beanTargetClass.getInterfaces();
                if (interfaces.length < 1) {
                    interfaces = new Class[] { beanTargetClass };
                }
            }
            try {
                final ServiceRegistration serviceRegistration = this.serviceManager.registerService(this.bundleContext, bean, beanName, new Hashtable<String, Object>(), interfaces);
                final String serviceName = this.getServiceName(beanName);
                this.beanFactory.initializeBean((Object)serviceRegistration, serviceName);
            }
            catch (final Exception e) {
                this.log.error((Object)("Unable to register bean '" + beanName + "' as an OSGi exported service"), (Throwable)e);
            }
        }
        return bean;
    }
    
    private boolean isPublicComponent(final Class beanTargetClass) {
        return this.hasAnnotation(beanTargetClass, ModuleType.class) || this.hasAnnotation(beanTargetClass, ExportAsService.class) || (this.hasAnnotation(beanTargetClass, ExportAsDevService.class) && this.isDevMode);
    }
    
    private boolean hasAnnotation(final Class beanTargetClass, final Class<? extends Annotation> annotationClass) {
        return beanTargetClass.isAnnotationPresent(annotationClass);
    }
    
    private String getServiceName(final String beanName) {
        return beanName + "_osgiService";
    }
}
