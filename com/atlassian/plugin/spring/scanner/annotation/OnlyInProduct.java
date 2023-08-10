// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.annotation;

import com.atlassian.plugin.spring.scanner.ProductFilter;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyInProduct {
    ProductFilter value();
}
