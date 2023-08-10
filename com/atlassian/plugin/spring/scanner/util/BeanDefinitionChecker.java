// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.util;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.config.BeanDefinition;

public class BeanDefinitionChecker
{
    public static boolean needToRegister(final String beanName, final BeanDefinition beanDefinition, final BeanDefinitionRegistry registry) throws IllegalStateException {
        if (!registry.containsBeanDefinition(beanName)) {
            return true;
        }
        BeanDefinition existingDef = registry.getBeanDefinition(beanName);
        final BeanDefinition originatingDef = existingDef.getOriginatingBeanDefinition();
        if (originatingDef != null) {
            existingDef = originatingDef;
        }
        if (isCompatible(beanDefinition, existingDef)) {
            return false;
        }
        throw new IllegalStateException("Annotation-specified bean name '" + beanName + "' for bean class [" + beanDefinition.getBeanClassName() + "] conflicts with existing, " + "non-compatible bean definition of same name and class [" + existingDef.getBeanClassName() + "]");
    }
    
    public static boolean isCompatible(final BeanDefinition newDefinition, final BeanDefinition existingDefinition) {
        return newDefinition.getBeanClassName().equals(existingDefinition.getBeanClassName()) || newDefinition.equals(existingDefinition);
    }
}
