package com.cenkc.lxftcs.dao;

import com.cenkc.lxftcs.model.LogEventAlert;

import java.util.List;

/**
 * created by cenkc on 9/2/2019
 */
public interface LogEventAlertDao {
    void saveLogEventAlert(LogEventAlert logEventAlert);
    List<LogEventAlert> getAllLogEventAlerts();
    LogEventAlert findLogEventAlertById(String id);
    Boolean deleteLogEventAlertById(String id);
    Long getLogEventAlertRecordCount();
    Long getLogEventAlertRecordCountByAlert();
}
