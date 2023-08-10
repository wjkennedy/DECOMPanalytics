// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.annotation.component;

import org.springframework.stereotype.Component;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ClasspathComponent {
    String value() default "";
}
