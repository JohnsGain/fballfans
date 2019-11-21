package com.john.server.controller;

import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-13 23:53
 * @since jdk1.8
 */
public class LogFile {

    public static final String FILE_PROPERTY = "logging.file";
    public static final String PATH_PROPERTY = "logging.path";
    private final String file;
    private final String path;

    LogFile(String file) {
        this(file, (String) null);
    }

    LogFile(String file, String path) {
        Assert.isTrue(StringUtils.hasLength(file) || StringUtils.hasLength(path), "File or Path must not be empty");
        this.file = file;
        this.path = path;
    }

    public void applyToSystemProperties() {
        this.applyTo(System.getProperties());
    }

    public void applyTo(Properties properties) {
        this.put(properties, "LOG_PATH", this.path);
        this.put(properties, "LOG_FILE", this.toString());
    }

    private void put(Properties properties, String key, String value) {
        if (StringUtils.hasLength(value)) {
            properties.put(key, value);
        }

    }

    public String toString() {
        if (StringUtils.hasLength(this.file)) {
            return this.file;
        } else {
            String path = this.path;
            if (!path.endsWith("/")) {
                path = path + "/";
            }

            return StringUtils.applyRelativePath(path, "spring.log");
        }
    }

    public static LogFile get(PropertyResolver propertyResolver) {
        String file = propertyResolver.getProperty("logging.file");
        String path = propertyResolver.getProperty("logging.path");
        return !StringUtils.hasLength(file) && !StringUtils.hasLength(path) ? null : new LogFile(file, path);
    }

}
