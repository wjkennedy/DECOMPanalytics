// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.extension;

import javax.annotation.Nullable;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.ResourceLoader;

public interface OsgiBundleContextAccessor
{
    @Nullable
    BundleContext getBundleContext(final ResourceLoader p0);
}
