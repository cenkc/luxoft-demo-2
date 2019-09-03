package com.cenkc.lxftcs.dao;

import com.cenkc.lxftcs.model.LogEventAlert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by cenkc on 2019-09-02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogEventAlertDaoImplTest {

    @Autowired
    LogEventAlertDaoImpl logEventAlertDao;

    private Long logEventAlertRecordCount;
    private static LogEventAlert singleEventAlertRecord ;

    @BeforeClass
    public static void beforeAll() {
        // avoiding unique constraint violation with using an eight character long id
        singleEventAlertRecord = new LogEventAlert(RandomStringUtils.randomAlphabetic(8), 7L, "APPLICATION_LOG", "12345", true);
    }

    @Test
    public void saveLogEventAlert() {
        System.out.println("running 1");
        Long before = logEventAlertDao.getLogEventAlertRecordCount();
        logEventAlertDao.saveLogEventAlert(singleEventAlertRecord);
        Long after = logEventAlertDao.getLogEventAlertRecordCount();
        Assert.assertEquals(java.util.Optional.of(before + 1L).get(), java.util.Optional.ofNullable(after).get());
        Assert.assertNotNull(logEventAlertDao.findLogEventAlertById(singleEventAlertRecord.getId()));
        //logEventAlertDao.deleteLogEventAlertById(singleEventAlertRecord.getId())
    }

    @Test
    public void getAllLogEventAlerts() {
        System.out.println("running 2");
        List<LogEventAlert> allLogEventAlerts = logEventAlertDao.getAllLogEventAlerts();
        Long logEventAlertRecordCount = logEventAlertDao.getLogEventAlertRecordCount();
        Assert.assertNotNull(allLogEventAlerts);
        Assert.assertEquals(allLogEventAlerts.size(), logEventAlertRecordCount.intValue());
    }

    @Test
    public void findLogEventAlertById() {
        LogEventAlert firstRec = logEventAlertDao.getAllLogEventAlerts().get(0);
        LogEventAlert found = logEventAlertDao.findLogEventAlertById(firstRec.getId());
        Assert.assertEquals(found.getId(), firstRec.getId());
    }

    @Test
    public void getLogEventAlertRecordCount() {
        Long logEventAlertRecordCount = logEventAlertDao.getLogEventAlertRecordCount();
        Assert.assertTrue(logEventAlertRecordCount > 0);
    }

    @Test
    public void getLogEventAlertRecordCountByAlert() {
        Long logEventAlertRecordCountByAlert = logEventAlertDao.getLogEventAlertRecordCountByAlert();
        Assert.assertTrue(logEventAlertRecordCountByAlert > 0);
    }

    @Test
    public void deleteLogEventAlertById() {
        Long beforeCount = logEventAlertDao.getLogEventAlertRecordCount();
        String id = logEventAlertDao.getAllLogEventAlerts().get(0).getId();
        logEventAlertDao.deleteLogEventAlertById(id);
        Long afterCount = logEventAlertDao.getLogEventAlertRecordCount();
        Assert.assertEquals(afterCount + 1, beforeCount.intValue());
    }

}