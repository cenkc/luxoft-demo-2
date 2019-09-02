package com.cenkc.lxftcs.dao;

import com.cenkc.lxftcs.model.LogEventAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * created by cenkc on 9/2/2019
 */
@Repository
public class LogEventAlertDaoImpl implements LogEventAlertDao {

    @Autowired
    @Qualifier("LxftcsDB-JdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public static final String INSERT_ALERT_SQL = "INSERT INTO LOG_EVENT_ALERT (ID, DURATION, TYPE, HOST, ALERT) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void saveLogEventAlert(LogEventAlert logEventAlert) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_ALERT_SQL);
                ps.setString(1, logEventAlert.getId());
                ps.setLong(2, logEventAlert.getDuration());
                ps.setString(3, logEventAlert.getType());
                ps.setString(4, logEventAlert.getHost());
                ps.setBoolean(5, logEventAlert.getAlert());
                return ps;
            }
        });
    }
}
