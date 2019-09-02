package com.cenkc.lxftcs.data;

import com.cenkc.lxftcs.model.LogEventModel;
import com.cenkc.lxftcs.model.StateEnum;
import com.cenkc.lxftcs.service.WriteToFileWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

/**
 * created by cenkc on 8/26/2019
 */
@Service
public class SampleLogGenerator {

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper objectMapper;

    public static final Logger logger = LogManager.getLogger(SampleLogGenerator.class);
    private static final Random random = new Random();

    public static final int ID_FIELD_COUNT = 10;
    public static final String APPLICATION_LOG = "APPLICATION_LOG";
    public static final String HOST = "12345";
    private static final int MIN_RANDOM_LAG = 1;
    private static final int MAX_RANDOM_LAG = 10;

    public void generate(String filePath, long dataCount) {
        Thread t1 = null, t2 = null;
        BufferedWriter bufferedWriter = null;
        final Path path = Paths.get(filePath);

        try {
            bufferedWriter = Files.newBufferedWriter(path);

            Instant start = Instant.now();

            while (dataCount-- > 0) {
                String id = RandomStringUtils.randomAlphabetic(ID_FIELD_COUNT);
                long timeStamp = System.currentTimeMillis();
                String applicationLog = getApplicationLog(dataCount);
                String host = getHost(dataCount);
                LogEventModel lem = new LogEventModel(id, StateEnum.STARTED.name(), applicationLog, host, timeStamp);
                t1 = new Thread(new WriteToFileWorker(lem, bufferedWriter, objectMapper));
                t1.start();

                lem = new LogEventModel(id, StateEnum.FINISHED.name(), applicationLog, host, timeStamp + randomLag());
                t2 = new Thread(new WriteToFileWorker(lem, bufferedWriter, objectMapper));
                t2.start();
            }
            while(t1.isAlive() || t2.isAlive()) {
                Thread.sleep(100);
            }

            Instant finish = Instant.now();
            logger.info("Sample file '{}' generated, duration is : {} ms", filePath, Duration.between(start, finish).toMillis());
        } catch (Exception e) {
            logger.error("Writer error", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    logger.error("Couldn't close bufferedWriter", e);
                }
            }
        }
    }

    private String getHost(long dataCount) {
        return (dataCount % 3 == 0 ? HOST : null);
    }

    private String getApplicationLog(long dataCount) {
        return (dataCount % 3 == 0 ? APPLICATION_LOG : null);
    }

    private int randomLag() {
        return random.ints(MIN_RANDOM_LAG, MAX_RANDOM_LAG).findFirst().getAsInt();
    }
}
