package com.cenkc.lxftcs.config;

import com.cenkc.lxftcs.hsqlfileserver.HSQLFileDatabaseServer;
import com.cenkc.lxftcs.hsqlfileserver.HSQLFileDatabaseServerImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by cenkc on 8/28/2019
 */
//https://github.com/Springjunky/spring-boot-multi-hsql
@Configuration
public class FileDatabaseConfig {

    @ConfigurationProperties(prefix = "lxftcs.database")
    @Bean("LxftcsDB")
    public HSQLFileDatabaseServer sourceDataBase() {
        // Just return the Bean the @PostConstruct starts it for you
        return new HSQLFileDatabaseServerImpl();
    }
}
