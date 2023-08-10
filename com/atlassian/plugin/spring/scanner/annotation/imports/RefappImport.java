// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.annotation.imports;

import com.atlassian.plugin.spring.scanner.ProductFilter;
import com.atlassian.plugin.spring.scanner.annotation.OnlyInProduct;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ComponentImport
@OnlyInProduct(ProductFilter.REFAPP)
public @interface RefappImport {
    String value() default "";
}
