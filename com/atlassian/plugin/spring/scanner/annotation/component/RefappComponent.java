// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.annotation.component;

import com.atlassian.plugin.spring.scanner.ProductFilter;
import com.atlassian.plugin.spring.scanner.annotation.OnlyInProduct;
import org.springframework.stereotype.Component;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Component
@OnlyInProduct(ProductFilter.REFAPP)
public @interface RefappComponent {
    String value() default "";
}
