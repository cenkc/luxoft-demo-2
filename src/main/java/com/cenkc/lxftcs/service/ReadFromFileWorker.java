package com.cenkc.lxftcs.service;

import com.cenkc.lxftcs.dao.LogEventAlertDao;
import com.cenkc.lxftcs.model.LogEventAlert;
import com.cenkc.lxftcs.model.LogEventModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

/**
 * created by cenkc on 8/26/2019
 */
@Service
public class ReadFromFileWorker {

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    LogEventAlertDao logEventAlertDao;

    @Value("${duration-threshold:4}")
    private Long durationThreshold;

    private static final Logger logger = LogManager.getLogger(ReadFromFileWorker.class);

    public void readAsStreamUsingLongSummaryStatistics(String filePath) {

        /**
         * Used try-with-resources
         * https://stackoverflow.com/a/34073306 by Brian Goetz
         */
        try (Stream<String> stream = Files.lines(new File(filePath).toPath())){
            Instant start = Instant.now();
            stream
                    .parallel()
                    .map(str -> convertModel(str))
                    .collect(
                            groupingBy(
                                    LogEventModel::getId,
                                    summarizingLong(LogEventModel::getTimestamp)
                            )
                    )
                    .entrySet()
                    .stream()
                    .forEach(
                            getEntryConsumer()
                    );
            stream.close();
            Instant finish = Instant.now();
            logger.info("All lines processed on file '{}', using LongSummaryStatistics, duration is : {} ms", filePath, Duration.between(start, finish).toMillis());
        } catch (Exception e) {
            logger.error("Exception occurred While retrieving file {}", filePath, e);
        }
    }

    private Consumer<Map.Entry<String, LongSummaryStatistics>> getEntryConsumer() {
        return entry -> {
            final String id = entry.getKey();
            final long duration = entry.getValue().getMax() - entry.getValue().getMin();
            LogEventAlert logEventAlert = new LogEventAlert(id, duration, null, null, (duration > durationThreshold ? true : false));
            logEventAlertDao.saveLogEventAlert(logEventAlert);
        };
    }

    public void readAsStreamUsingHashMap(String filePath) {

        Map<String, LogEventModel> hashMap = new HashMap<>();

        /**
         * Used try-with-resources
         * https://stackoverflow.com/a/34073306 by Brian Goetz
         */
        try (Stream<String> stream = Files.lines(new File(filePath).toPath())){
            Instant start = Instant.now();
            stream
                    .parallel()
                    .map(str -> convertModel(str))
                    .forEach(
                            getLogEventModelConsumer(hashMap)
                    );
            stream.close();
            Instant finish = Instant.now();
            logger.info("All lines processed on file '{}', using HashMap, duration is : {} ms", filePath, Duration.between(start, finish).toMillis());
        } catch (Exception e) {
            logger.error("Exception occurred While retrieving file {}", filePath, e);
        }
    }

    private Consumer<LogEventModel> getLogEventModelConsumer(Map<String, LogEventModel> hashMap) {
        return logEventModel -> {
            final LogEventModel existingRecord = hashMap.putIfAbsent(logEventModel.getId(), logEventModel);
            if (existingRecord != null) {
                long duration = Math.abs(existingRecord.getTimestamp() - logEventModel.getTimestamp());
                LogEventAlert logEventAlert = new LogEventAlert(
                        logEventModel.getId(),
                        duration,
                        logEventModel.getType(),
                        logEventModel.getHost(),
                        (duration > durationThreshold ? true : false)
                );
                logEventAlertDao.saveLogEventAlert(logEventAlert);
                hashMap.remove(logEventModel);
            }
        };
    }

    private LogEventModel convertModel(String line) {
        LogEventModel logEventModel = null;
        try {
            logEventModel = objectMapper.readValue(line, LogEventModel.class);
        } catch (IOException e) {
            logger.error("cannot convert line:'{}'", line);
            logEventModel = new LogEventModel();
        }
        return logEventModel;
    }
}
