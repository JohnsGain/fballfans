package com.john.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-13 10:05
 * @since jdk1.8
 */
@Slf4j
public class MyTypeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String className = classMetadata.getClassName();
        String packageName = "com.john.server.test";
        if (className.contains(packageName)) {
            log.info("类名={}",className);
            return true;
        }
        return false;
    }
}
