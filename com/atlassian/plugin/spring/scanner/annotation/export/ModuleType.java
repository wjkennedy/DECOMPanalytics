// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.annotation.export;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExportAsService
public @interface ModuleType {
    Class<?>[] value() default {};
}
