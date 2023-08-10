// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import java.util.Enumeration;
import java.util.Dictionary;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.osgi.framework.Bundle;
import org.apache.commons.logging.LogFactory;
import java.beans.PropertyDescriptor;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.BeansException;
import org.osgi.framework.BundleContext;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

public class DevModeBeanInitialisationLoggerBeanPostProcessor implements InstantiationAwareBeanPostProcessor, InitializingBean, DestructionAwareBeanPostProcessor, DisposableBean
{
    private static final boolean isDevMode;
    private volatile Log log;
    private final BundleContext bundleContext;
    
    public DevModeBeanInitialisationLoggerBeanPostProcessor(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
    
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
    
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        this.logBeanDetail("AfterInitialisation", bean.getClass(), beanName);
        return bean;
    }
    
    public Object postProcessBeforeInstantiation(final Class beanClass, final String beanName) throws BeansException {
        this.logBeanDetail("BeforeInstantiation", beanClass, beanName);
        return null;
    }
    
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
        return true;
    }
    
    public PropertyValues postProcessPropertyValues(final PropertyValues pvs, final PropertyDescriptor[] pds, final Object bean, final String beanName) throws BeansException {
        return pvs;
    }
    
    public void postProcessBeforeDestruction(final Object bean, final String beanName) throws BeansException {
        this.logBeanDetail("BeforeDestruction", bean.getClass(), beanName);
    }
    
    private void logBeanDetail(final String stage, final Class beanClass, final String beanName) {
        if (this.log.isDebugEnabled()) {
            this.log.debug((Object)String.format("%s [bean=%s, type=%s]", stage, beanName, beanClass.getName()));
        }
    }
    
    public void afterPropertiesSet() throws Exception {
        this.log = LogFactory.getLog(this.getLoggerName());
        final Bundle bundle = this.bundle();
        if (DevModeBeanInitialisationLoggerBeanPostProcessor.isDevMode) {
            final String loggerName = this.getLoggerName();
            final String msg = String.format("\nSpring context started for bundle : %s id(%d) v(%s) %s\n\nIf you want to debug the Spring wiring of your code then set a DEBUG level log level as follows.  [ This is a dev.mode only message. ]\n\tlog4j.logger.%s  = DEBUG, console, filelog\n", bundle.getSymbolicName(), bundle.getBundleId(), bundle.getVersion(), bundle.getLocation(), loggerName);
            this.log.warn((Object)msg);
        }
        this.printBundleDebugInfo(bundle);
    }
    
    public void destroy() throws Exception {
        if (DevModeBeanInitialisationLoggerBeanPostProcessor.isDevMode) {
            final Bundle bundle = this.bundle();
            this.log.warn((Object)String.format("\n\n\tSpring context destroyed : %s id(%d) v(%s) \n", bundle.getSymbolicName(), bundle.getBundleId(), bundle.getVersion()));
        }
    }
    
    private void printBundleDebugInfo(final Bundle bundle) {
        if (this.log.isDebugEnabled()) {
            final StringWriter sw = new StringWriter();
            final PrintWriter out = new PrintWriter(sw);
            out.println();
            out.format("\tBundle Id : %d\n", bundle.getBundleId());
            out.format("\tBundle Name : %s\n", bundle.getSymbolicName());
            out.format("\tBundle Location : %s\n", bundle.getLocation());
            out.format("\tBundle Version : %s\n", bundle.getVersion());
            out.format("\tBundle Headers :\n", new Object[0]);
            final Dictionary headers = bundle.getHeaders();
            final Enumeration keys = headers.keys();
            while (keys.hasMoreElements()) {
                final Object key = keys.nextElement();
                final Object value = headers.get(key);
                out.format("\t\t%s: %s\n", key, value);
            }
            out.println();
            this.log.debug((Object)sw.toString());
        }
    }
    
    private Bundle bundle() {
        return this.bundleContext.getBundle();
    }
    
    private String getLoggerName() {
        return String.format("%s.spring", this.bundle().getSymbolicName());
    }
    
    static {
        isDevMode = Boolean.parseBoolean(System.getProperty("atlassian.dev.mode", "false"));
    }
}
