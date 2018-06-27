package com.giophub.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/*
 * For property loader details see:
 * http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html?page=1
 * http://www.koders.com/java/fid5580570D56CD03F7E7ACF8C7968285B8673EA3D9.aspx
 */

public class Loader { // todo : change the logic of this class
    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);


    private static final boolean THROW_ON_LOAD_FAILURE = true;    // when false if failed to load properties null is
    // returned instead of throwing exception.
    private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
    private static final String SUFFIX = ".properties";


    public void Loader(String name, ClassLoader classLoader) {
        // check input params
        if (name == null) {
            throw new IllegalArgumentException("null input: name");
        }
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (name.endsWith(SUFFIX)) {
            name = name.substring(0, name.length() - SUFFIX.length());
        }

    }

    public BufferedReader loadAsBufferedReader(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        name = classLoader.getResource(name).getFile();

        // Get an efficient reader for the file
        FileReader reader = null;
        try {
            reader = new FileReader(name);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error on loading file: " + name + "\n" + e.getMessage());
        }

        BufferedReader bufferedReader = new BufferedReader(reader);
        return bufferedReader;
    }

    public void loadAsResource(String name, ClassLoader classLoader) {
        name = name.replace('.', '/');
        /*if (!name.endsWith(SUFFIX)) {
            name = name.concat(SUFFIX);
        }*/

    }

    public ResourceBundle loadAsResourceBundle(String name, ClassLoader classLoader) {
        name = name.replace('/', '.');
        // Throws MissingResourceException on lookup failures:
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(name, Locale.getDefault(), classLoader);
        return resourceBundle;
    }

    public InputStream loadAsStream(String name, ClassLoader classLoader) {
        InputStream is = classLoader.getResourceAsStream(name);
        return is;
    }


    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     */
    public static Properties loadProperties(final String name) {
        return loadProperties(name, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extention. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     *
     * @param name   classpath resource name [may not be null]
     * @param loader classloader through which to load the resource [null
     *               is equivalent to the application loader]
     * @return resource converted to java.util.Properties [may be null if the
     *         resource was not found and THROW_ON_LOAD_FAILURE is false]
     * @throws IllegalArgumentException if the resource was not found and
     *                                  THROW_ON_LOAD_FAILURE is true
     */
    public static Properties loadProperties(String name, ClassLoader loader) {
        if (name == null) {
            throw new IllegalArgumentException("null input: name");
        }

        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        if (name.endsWith(SUFFIX)) {
            name = name.substring(0, name.length() - SUFFIX.length());
        }

        Properties result = null;

        InputStream in = null;
        try {
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }

            if (LOAD_AS_RESOURCE_BUNDLE) {
                name = name.replace('/', '.');
                // Throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);

                result = new Properties();
                for (Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
                    final String key = (String) keys.nextElement();
                    final String value = rb.getString(key);

                    result.put(key, value);
                }
            } else {
                name = name.replace('.', '/');

                if (!name.endsWith(SUFFIX)) {
                    name = name.concat(SUFFIX);
                }

                // Returns null on lookup failures:
                in = loader.getResourceAsStream(name);
                if (in != null) {
                    result = new Properties();
                    result.load(in); // Can throw IOException
                }
            }
        } catch (Exception e) {
            result = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable ignore) {
                }
            }
        }

        if (THROW_ON_LOAD_FAILURE && (result == null)) {
            throw new IllegalArgumentException("could not load [" + name + "]" +
                    " as " + (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
        }

        return result;
    }


}
