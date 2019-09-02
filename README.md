"# luxoft-demo-2" 
mvnw spring-boot:run -Dspring-boot.run.arguments=--generateData=true,--filePath=D:\\temp\\hede.json,--sampleDataCount=10,--useSummaryStatistics=false

--generateData --> Boolean, true if you want to generate a file from scratch, false if you already have a file
--sampleDataCount --> Integer, sample data count
--useSummaryStatistics --> Boolean, true if you want to use groupingBy and summarizingLong (Collectors), false if you want to use HashMap to calculate the duration (putIfAbsent strategy)
