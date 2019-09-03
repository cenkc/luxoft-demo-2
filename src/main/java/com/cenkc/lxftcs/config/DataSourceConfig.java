package com.cenkc.lxftcs.config;

import com.cenkc.lxftcs.hsqlfileserver.HSQLFileDatabaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * created by cenkc on 8/31/2019
 */
//https://github.com/Springjunky/spring-boot-multi-hsql
@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    @Qualifier("LxftcsDB")
    private HSQLFileDatabaseServer databaseServer;

    @Bean("LxftcsDB-DataSource")
    @Primary
    public DataSource lxftcsDbDatasource() {
        logger.info("creating DataSource of LxftcsDB");
        return databaseServer.getBasicDataSourceForFileDatabaseServer();
    }

    @Bean("LxftcsDB-JdbcTemplate")
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(databaseServer.getBasicDataSourceForFileDatabaseServer());
    }
}
