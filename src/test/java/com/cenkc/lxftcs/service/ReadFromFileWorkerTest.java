package com.cenkc.lxftcs.service;

import com.cenkc.lxftcs.model.LogEventAlert;
import com.cenkc.lxftcs.model.LogEventModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cenkc on 2019-09-02
 */
@RunWith(SpringRunner.class)
public class ReadFromFileWorkerTest {

    private ObjectMapper objectMapper;
    private String [] rawFileData;
    private Stream<String> fileContentStream;
    private final Long durationThreshold = 4L;

    @Before
    public void setUp() throws Exception {
        fileContentStream = getStreamData();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_fileContentStream_has_data() {
        Assert.assertTrue(fileContentStream.count() > 0);
    }

    @Test
    public void should_fileContentStream_count_match() {
        Assert.assertEquals(rawFileData.length, fileContentStream.count());
    }

    @Test
    public void should_fileContentStream_converted_successfully() {
        Stream<LogEventModel> logEventModelStream = fileContentStream.map(line -> convertModel(line));
        Optional<LogEventModel> first = logEventModelStream.findFirst();
        Assert.assertTrue(first.get() instanceof LogEventModel);
    }

    @Test
    public void should_SummaryStatistics_Count_equals_half_of_the_Stream() {
        Stream<LogEventModel> logEventModelStream = fileContentStream.map(line -> convertModel(line));
        Map<String, LongSummaryStatistics> collect = logEventModelStream
                .collect(
                        Collectors.groupingBy(
                                LogEventModel::getId,
                                Collectors.summarizingLong(LogEventModel::getTimestamp)
                        )
                );
        Assert.assertEquals(rawFileData.length / 2, collect.size());
    }

    @Test
    public void should_Alert_Count_equals_6_using_LongSummaryStatistics() {
        Stream<LogEventModel> logEventModelStream = fileContentStream.map(line -> convertModel(line));
        long count = logEventModelStream
                .collect(
                        Collectors.groupingBy(
                                LogEventModel::getId,
                                Collectors.summarizingLong(LogEventModel::getTimestamp)
                        )
                )
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getMax() - entry.getValue().getMin() > 4)
                .count();

        Assert.assertEquals(6, count);
    }

    @Test
    public void should_Alert_Count_equals_6_using_Hashmap() {
        Map<String, LogEventModel> hashMap = new HashMap<>();
        List<LogEventAlert> alertList = new ArrayList<>();
        Stream<LogEventModel> logEventModelStream = fileContentStream.map(line -> convertModel(line));
        logEventModelStream
                .forEach(
                        logEventModel -> {
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
                                alertList.add(logEventAlert);
                            }
                        }
                );

        long count = alertList.stream().filter(alertEvent -> alertEvent.getAlert() == true).count();
        Assert.assertEquals(6, count);
    }

    private Stream<String> getStreamData() {
        rawFileData = new String[] {

                "{\"id\":\"CNNLxkkHQo\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343275}",
                "{\"id\":\"wrZCpcUpYl\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343279}",
                "{\"id\":\"VFTMDTTVbQ\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343277}",
                "{\"id\":\"YdxqXzBSHQ\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343278}",
                "{\"id\":\"HYhjHcCyuq\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343284}",
                "{\"id\":\"yDLfAhsuup\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343284}",
                "{\"id\":\"uJkubvMkhI\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343280}",
                "{\"id\":\"DfuplXMkyl\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343278}",
                "{\"id\":\"HYhjHcCyuq\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343277}",
                "{\"id\":\"SrwjvrgEyB\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343267}",
                "{\"id\":\"uJkubvMkhI\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343277}",
                "{\"id\":\"VFTMDTTVbQ\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343279}",
                "{\"id\":\"yDLfAhsuup\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343277}",
                "{\"id\":\"DfuplXMkyl\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343284}",
                "{\"id\":\"YdxqXzBSHQ\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343286}",
                "{\"id\":\"CNNLxkkHQo\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343282}",
                "{\"id\":\"wrZCpcUpYl\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343280}",
                "{\"id\":\"SrwjvrgEyB\",\"state\":\"FINISHED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567412343275}",
                "{\"id\":\"VyDuGNlpMR\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567412343276}",
                "{\"id\":\"VyDuGNlpMR\",\"state\":\"STARTED\",\"type\":null,\"host\":null,\"timestamp\":1567412343274}"
        };
        return Arrays.stream(rawFileData);
    }

    private LogEventModel convertModel(String line) {
        LogEventModel logEventModel = null;
        try {
            logEventModel = objectMapper.readValue(line, LogEventModel.class);
        } catch (IOException e) {
            System.out.println("cannot convert line:" + line);
            logEventModel = new LogEventModel();
        }
        return logEventModel;
    }
}
