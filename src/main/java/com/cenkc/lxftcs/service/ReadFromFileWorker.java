package com.cenkc.lxftcs.service;

import com.cenkc.lxftcs.model.LogEventModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * created by cenkc on 8/26/2019
 */
@Service
public class ReadFromFileWorker {

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(ReadFromFileWorker.class);

    public void harala(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                final LogEventModel logEventModel = objectMapper.readValue(line, LogEventModel.class);
                System.out.println("geldi la :" + logEventModel.toString());
            }
        } catch (IOException e) {
            logger.error("Couldn't read from '{}'", filePath);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("Couldn't close bufferedReader", e);
                }
            }
        }
    }
}
