package com.mybooks.library.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DozerConfig {

    @Bean
    public Mapper dozerMapper() {
        List<String> mappingFiles = Arrays.asList("dozer-mappings.xml");
        return DozerBeanMapperBuilder.create().withMappingFiles(mappingFiles)
                .build();
    }
}
