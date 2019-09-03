package com.cenkc.lxftcs.dao;

import com.cenkc.lxftcs.model.LogEventAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * created by cenkc on 9/2/2019
 */
@Repository
public class LogEventAlertDaoImpl implements LogEventAlertDao {

    @Autowired
    @Qualifier("LxftcsDB-JdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public static final String SQL_INSERT_EVENT_ALERT = "INSERT INTO LOG_EVENT_ALERT (ID, DURATION, TYPE, HOST, ALERT) VALUES (?, ?, ?, ?, ?)";
    public static final String SQL_GET_ALL_EVENT_ALERTS = "SELECT * FROM LOG_EVENT_ALERT";
    public static final String SQL_GET_EVENT_ALERT_BY_ID = "SELECT * FROM LOG_EVENT_ALERT WHERE ID = ?";
    public static final String SQL_DELETE_EVENT_ALERT_BY_ID = "DELETE FROM LOG_EVENT_ALERT WHERE ID = ?";
    public static final String SQL_EVENT_ALERT_COUNT = "SELECT COUNT(*) FROM LOG_EVENT_ALERT";
    public static final String SQL_EVENT_ALERT_COUNT_BY_ALERT = "SELECT COUNT(*) FROM LOG_EVENT_ALERT WHERE ALERT";

    @Override
    public void saveLogEventAlert(LogEventAlert logEventAlert) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_EVENT_ALERT);
                ps.setString(1, logEventAlert.getId());
                ps.setLong(2, logEventAlert.getDuration());
                ps.setString(3, logEventAlert.getType());
                ps.setString(4, logEventAlert.getHost());
                ps.setBoolean(5, logEventAlert.getAlert());
                return ps;
            }
        });
    }

    @Override
    public List<LogEventAlert> getAllLogEventAlerts() {
        return jdbcTemplate.query(SQL_GET_ALL_EVENT_ALERTS, getLogEventAlertRowMapper());
    }

    @Override
    public LogEventAlert findLogEventAlertById(String id) {
        return jdbcTemplate.queryForObject(SQL_GET_EVENT_ALERT_BY_ID, getLogEventAlertRowMapper(),
                id);
    };

    @Override
    public Boolean deleteLogEventAlertById(String id) {
        return jdbcTemplate.execute(SQL_DELETE_EVENT_ALERT_BY_ID, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, id);
                return preparedStatement.execute();
            }
        });
    }

    @Override
    public Long getLogEventAlertRecordCount() {
        Number number = jdbcTemplate.queryForObject(SQL_EVENT_ALERT_COUNT, null, Integer.class);
        return (number != null ? number.longValue() : 0);
    }

    @Override
    public Long getLogEventAlertRecordCountByAlert() {
        Number number = jdbcTemplate.queryForObject(SQL_EVENT_ALERT_COUNT_BY_ALERT, null, Integer.class);
        return (number != null ? number.longValue() : 0);
    }

    private RowMapper<LogEventAlert> getLogEventAlertRowMapper() {
        return new RowMapper<LogEventAlert>() {
            @Override
            public LogEventAlert mapRow(ResultSet resultSet, int i) throws SQLException {
                LogEventAlert logEventAlert = new LogEventAlert();
                logEventAlert.setId(resultSet.getString("ID"));
                logEventAlert.setDuration(resultSet.getLong("DURATION"));
                logEventAlert.setType(resultSet.getString("TYPE"));
                logEventAlert.setHost(resultSet.getString("HOST"));
                logEventAlert.setAlert(resultSet.getBoolean("ALERT"));
                return logEventAlert;
            }
        };
    }

}
