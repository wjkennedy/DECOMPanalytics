// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner;

public enum ProductFilter
{
    ALL, 
    JIRA, 
    CONFLUENCE, 
    BAMBOO, 
    BITBUCKET, 
    STASH, 
    CROWD, 
    FECRU, 
    REFAPP;
    
    public static boolean hasProduct(final String productName) {
        try {
            final ProductFilter filter = valueOf(productName);
            return null != filter;
        }
        catch (final IllegalArgumentException e) {
            return false;
        }
    }
    
    public String getPerProductFile(final String fileStem) {
        return fileStem + "-" + this.name().toLowerCase();
    }
}
