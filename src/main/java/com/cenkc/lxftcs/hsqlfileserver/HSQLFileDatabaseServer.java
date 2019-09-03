package com.cenkc.lxftcs.hsqlfileserver;

import javax.sql.DataSource;

/**
 * created by cenkc on 8/28/2019
 */
//https://github.com/Springjunky/spring-boot-multi-hsql
public interface HSQLFileDatabaseServer {

    public String getJdbcConnectionString();

    public DataSource getBasicDataSourceForFileDatabaseServer();
}
