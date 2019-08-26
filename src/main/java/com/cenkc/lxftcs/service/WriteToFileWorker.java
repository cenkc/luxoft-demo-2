package com.cenkc.lxftcs.service;

import com.cenkc.lxftcs.model.LogEventModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * created by cenkc on 8/26/2019
 */
public class WriteToFileWorker implements Runnable {

    private static final Logger logger = LogManager.getLogger(WriteToFileWorker.class);
    public static final String LINE_SEPARATOR = System.lineSeparator();

    private LogEventModel logEventModel;
    private BufferedWriter bufferedWriter;
    private ObjectMapper objectMapper;

    public WriteToFileWorker(LogEventModel logEventModel, BufferedWriter bufferedWriter, ObjectMapper objectMapper) {
        this.logEventModel = logEventModel;
        this.bufferedWriter = bufferedWriter;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        try {
            writeToFile();
        } catch (IOException e) {
            logger.error("Can't write to file {}", logEventModel);
        }
    }

    private void writeToFile() throws IOException {
        String strLine = objectMapper.writeValueAsString(logEventModel);
        bufferedWriter.write(strLine + LINE_SEPARATOR);
//        logger.info("line: {}", logEventModel);
    }
}
