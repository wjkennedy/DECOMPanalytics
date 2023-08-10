// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class AtlassianScannerNamespaceHandler extends NamespaceHandlerSupport
{
    public void init() {
        this.registerBeanDefinitionParser("scan-indexes", (BeanDefinitionParser)new AtlassianScannerBeanDefinitionParser());
    }
}
