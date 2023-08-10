// 
// Decompiled by Procyon v0.6.0
// 

package com.atlassian.plugin.spring.scanner.util;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.common.base.Charsets;
import javax.annotation.Nullable;
import org.osgi.framework.BundleContext;
import com.atlassian.plugin.spring.scanner.ProductFilter;
import java.util.Collection;
import java.util.ArrayList;
import java.net.URL;
import java.util.List;
import org.osgi.framework.Bundle;
import org.apache.commons.logging.Log;

public class AnnotationIndexReader
{
    private static final Log log;
    
    public static List<String> readIndexFile(final String resourceFile, final Bundle bundle) {
        final URL url = bundle.getResource(resourceFile);
        return readIndexFile(url);
    }
    
    public static List<String> readIndexFile(final String resourceFile, final ClassLoader classLoader) {
        final URL url = classLoader.getResource(resourceFile);
        final List<String> strings = readIndexFile(url);
        if (AnnotationIndexReader.log.isDebugEnabled()) {
            AnnotationIndexReader.log.debug((Object)("Read annotation index file: " + resourceFile));
            AnnotationIndexReader.log.debug((Object)"Printing out found annotated beans: ");
            AnnotationIndexReader.log.debug((Object)strings);
        }
        return strings;
    }
    
    public static List<String> readAllIndexFilesForProduct(final String resourceFile, final Bundle bundle) {
        final List<String> entries = new ArrayList<String>();
        final URL url = bundle.getResource(resourceFile);
        entries.addAll(readIndexFile(url));
        final ProductFilter filter = ProductFilterUtil.getFilterForCurrentProduct(bundle.getBundleContext());
        if (null != filter) {
            entries.addAll(readIndexFile(filter.getPerProductFile(resourceFile), bundle));
        }
        return entries;
    }
    
    public static List<String> readAllIndexFilesForProduct(final String resourceFile, final ClassLoader classLoader, @Nullable final BundleContext bundleContext) {
        final List<String> entries = new ArrayList<String>();
        final URL url = classLoader.getResource(resourceFile);
        entries.addAll(readIndexFile(url));
        final ProductFilter filter = ProductFilterUtil.getFilterForCurrentProduct(bundleContext);
        if (null != filter) {
            entries.addAll(readIndexFile(filter.getPerProductFile(resourceFile), classLoader));
        }
        return entries;
    }
    
    public static List<String> readIndexFile(final URL url) {
        final List<String> resources = new ArrayList<String>();
        try {
            if (null == url) {
                return resources;
            }
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream(), Charsets.UTF_8));
            }
            catch (final FileNotFoundException e) {
                return resources;
            }
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                resources.add(line);
            }
            reader.close();
        }
        catch (final IOException e2) {
            throw new RuntimeException("Cannot read index file [" + url.toString() + "]", e2);
        }
        return resources;
    }
    
    public static Properties readPropertiesFile(final URL url) {
        final Properties resources = new Properties();
        try {
            if (null == url) {
                return resources;
            }
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream(), Charsets.UTF_8));
            }
            catch (final FileNotFoundException e) {
                return resources;
            }
            resources.load(reader);
        }
        catch (final IOException e2) {
            throw new RuntimeException("Cannot read properties file [" + url.toString() + "]", e2);
        }
        return resources;
    }
    
    public static String[] splitProfiles(final String profiles) {
        return StringUtils.isNotBlank(profiles) ? StringUtils.split(profiles, ',') : new String[0];
    }
    
    public static Iterable<String> getIndexFilesForProfiles(final String[] profileNames, final String indexFileName) {
        final List<String> filesToRead = new ArrayList<String>();
        if (profileNames.length > 0) {
            for (String profileName : profileNames) {
                profileName = StringUtils.defaultIfBlank(profileName, "").trim();
                if (StringUtils.isNotBlank(profileName)) {
                    final String fileToRead = "META-INF/plugin-components/profile-" + profileName + "/" + indexFileName;
                    filesToRead.add(fileToRead);
                }
            }
        }
        else {
            final String fileToRead2 = "META-INF/plugin-components/" + indexFileName;
            filesToRead.add(fileToRead2);
        }
        return filesToRead;
    }
    
    static {
        log = LogFactory.getLog((Class)AnnotationIndexReader.class);
    }
}
