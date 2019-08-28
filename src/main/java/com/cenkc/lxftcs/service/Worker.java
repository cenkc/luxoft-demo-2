package com.cenkc.lxftcs.service;

import com.cenkc.lxftcs.data.SampleLogGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * created by cenkc on 8/26/2019
 */
@Component
public class Worker implements ApplicationRunner {

    @Value("${generateData:false}")
    private Boolean generateData;

    @Value("${sampleDataCount:10}")
    private String sampleDataCount;

    @Value("${filePath:sampleData.json}")
    private String filePath;

    @Autowired
    private SampleLogGenerator sampleLogGenerator;

    @Autowired
    private ReadFromFileWorker readFromFileWorker;

    private static final Logger logger = LogManager.getLogger(Worker.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (generateData && Long.parseLong(sampleDataCount) > 0L) {
            logger.info("generating Dummy Data with {} lines", generateData);
            sampleLogGenerator.generate(filePath, Long.parseLong(sampleDataCount));
        }
        logger.info("beginning to process file:{}", filePath);
        readFromFileWorker.harala(filePath);
    }
}
