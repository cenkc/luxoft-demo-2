package com.cenkc.lxsf.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

/**
 * Created by EXT0208109 on 2019-08-29
 */
public class Test {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        Stream<String> streamData = getStreamData();

        Stream<LogEventModel> logEventModelStream = streamData
                .map(line -> convertModel(objectMapper, line));


        Map<String, LongSummaryStatistics> collect = logEventModelStream
                .collect(
                        groupingBy(
                                LogEventModel::getId,
                                summarizingLong(LogEventModel::getTimestamp)
                        )
                );

        Stream<Map.Entry<String, LongSummaryStatistics>> entryStream = collect.entrySet().stream().filter(entry -> entry.getValue().getMax() - entry.getValue().getMin() > 4);

        entryStream.forEach(
                entry -> System.out.println(
                        entry.getKey() + "->" +
                                entry.getValue().getMax() + "-" + entry.getValue().getMin() + ":" +
                                (entry.getValue().getMax() - entry.getValue().getMin())
                )
        );

    }

    private static LogEventModel convertModel(ObjectMapper objectMapper, String line) {
        LogEventModel logEventModel = null;
        try {
            logEventModel = objectMapper.readValue(line, LogEventModel.class);
        } catch (IOException e) {
            System.out.println("cannot convert line:" + line);
            logEventModel = new Test.LogEventModel();
        }
        return logEventModel;
    }

    static class LogEventModel {

        private String id;
        private String state;
        private String type;
        private String host;
        private Long timestamp;

        public LogEventModel() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public LogEventModel(String id, String state, String type, String host, Long timestamp) {
            this.id = id;
            this.state = state;
            this.type = type;
            this.host = host;
            this.timestamp = timestamp;
        }
    }

    private static Stream<String> getStreamData() {
        String [] data = new String[] {
                "{\"id\":\"aJbLiyfFQB\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567060492132}",
                "{\"id\":\"RcmUDArYga\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567060492132}",
                "{\"id\":\"VHuhatZZMb\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567060492127}",
                "{\"id\":\"AYifVEVxXA\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567060492136}",
                "{\"id\":\"AYifVEVxXA\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567060492132}",
                "{\"id\":\"VHuhatZZMb\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567060492125}",
                "{\"id\":\"RcmUDArYga\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567060492140}",
                "{\"id\":\"lPqgIZbQkq\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567060492140}",
                "{\"id\":\"aJbLiyfFQB\",\"state\":\"FINISHED\",\"type\":null,\"host\":null,\"timestamp\":1567060492140}",
                "{\"id\":\"lPqgIZbQkq\",\"state\":\"STARTED\",\"type\":\"APPLICATION_LOG\",\"host\":\"12345\",\"timestamp\":1567060492133}"
        };
        return Arrays.stream(data);
    }
}
