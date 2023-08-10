// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.util;

import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.osgi.framework.BundleContext;
import org.apache.commons.logging.LogFactory;
import java.util.Map;
import com.atlassian.plugin.spring.scanner.ProductFilter;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import com.google.common.annotations.VisibleForTesting;

public class ProductFilterUtil
{
    private static ProductFilterUtil INSTANCE;
    @VisibleForTesting
    static final String CLASS_ON_BAMBOO_CLASSPATH = "com.atlassian.bamboo.build.BuildExecutionManager";
    @VisibleForTesting
    static final String CLASS_ON_BITBUCKET_CLASSPATH = "com.atlassian.bitbucket.repository.RepositoryService";
    @VisibleForTesting
    static final String CLASS_ON_CONFLUENCE_CLASSPATH = "com.atlassian.confluence.core.ContentEntityManager";
    @VisibleForTesting
    static final String CLASS_ON_FECRU_CLASSPATH = "com.atlassian.fisheye.spi.services.RepositoryService";
    @VisibleForTesting
    static final String CLASS_ON_JIRA_CLASSPATH = "com.atlassian.jira.bc.issue.IssueService";
    @VisibleForTesting
    static final String CLASS_ON_REFAPP_CLASSPATH = "com.atlassian.refapp.api.ConnectionProvider";
    @VisibleForTesting
    static final String CLASS_ON_STASH_CLASSPATH = "com.atlassian.stash.repository.RepositoryService";
    private final Log log;
    private AtomicReference<ProductFilter> filterForProduct;
    private static final Map<String, ProductFilter> PRODUCTS_TO_HOSTCLASSES;
    
    private ProductFilterUtil() {
        this.log = LogFactory.getLog((Class)this.getClass());
        this.filterForProduct = new AtomicReference<ProductFilter>();
    }
    
    public static ProductFilter getFilterForCurrentProduct(@Nullable final BundleContext bundleContext) {
        return getInstance().getFilterForProduct(bundleContext);
    }
    
    public ProductFilter getFilterForProduct(@Nullable final BundleContext bundleContext) {
        ProductFilter productFilter = this.filterForProduct.get();
        if (productFilter == null) {
            this.filterForProduct.compareAndSet(productFilter, this.detectProduct(bundleContext));
            productFilter = this.filterForProduct.get();
        }
        return productFilter;
    }
    
    private ProductFilter detectProduct(final BundleContext bundleContext) {
        if (bundleContext == null) {
            this.log.warn((Object)"Couldn't detect product due to null bundleContext: will use ProductFilter.ALL");
            return ProductFilter.ALL;
        }
        for (final Map.Entry<String, ProductFilter> entry : ProductFilterUtil.PRODUCTS_TO_HOSTCLASSES.entrySet()) {
            if (this.detectService(bundleContext, entry.getKey())) {
                this.log.debug((Object)("Detected product: " + entry.getValue().name()));
                return entry.getValue();
            }
        }
        this.log.warn((Object)"Couldn't detect product, no known services found: will use ProductFilter.ALL");
        return ProductFilter.ALL;
    }
    
    private boolean detectService(final BundleContext bundleContext, final String serviceClassName) {
        return null != bundleContext.getServiceReference(serviceClassName);
    }
    
    private static ProductFilterUtil getInstance() {
        if (null == ProductFilterUtil.INSTANCE) {
            ProductFilterUtil.INSTANCE = new ProductFilterUtil();
        }
        return ProductFilterUtil.INSTANCE;
    }
    
    static {
        PRODUCTS_TO_HOSTCLASSES = (Map)ImmutableMap.builder().put((Object)"com.atlassian.bamboo.build.BuildExecutionManager", (Object)ProductFilter.BAMBOO).put((Object)"com.atlassian.bitbucket.repository.RepositoryService", (Object)ProductFilter.BITBUCKET).put((Object)"com.atlassian.confluence.core.ContentEntityManager", (Object)ProductFilter.CONFLUENCE).put((Object)"com.atlassian.fisheye.spi.services.RepositoryService", (Object)ProductFilter.FECRU).put((Object)"com.atlassian.jira.bc.issue.IssueService", (Object)ProductFilter.JIRA).put((Object)"com.atlassian.refapp.api.ConnectionProvider", (Object)ProductFilter.REFAPP).put((Object)"com.atlassian.stash.repository.RepositoryService", (Object)ProductFilter.STASH).build();
    }
}
