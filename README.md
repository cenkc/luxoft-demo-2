"# luxoft-demo-2" 

mvnw spring-boot:run -Dspring-boot.run.arguments=--generateData=true,--filePath=D:\\temp\\hede.json,--sampleDataCount=10,--useSummaryStatistics=false

Extract zip file to a directory.
While in this directory;
1) Execute this command to create jara file (without double quotes)
	"mvnw package -DskipTests"

2) And then issue this command to run the program 
	"mvnw spring-boot:run -Dspring-boot.run.arguments=--generateData=true,--filePath=D:\tmp\hede.json,--sampleDataCount=10,--useSummaryStatistics=false"
	
Arguments are :
	a) "filePath" (optional, default:sampleData.json) = Application will try to find and read the file from this location. If not provided, it will try to locate a file named "sampleData.json" in project directory.
	b) "generateData" (optional, default:false) = If true, a new file will be created. Of course you shold have to provide "filePath" and "sampleDataCount" parameters. If false, application will try to find and read a file whose path was given in "filePath" argument.
	c) "sampleDataCount" (optional, default:10) = Application will generate record pairs (STARTED & FINISHED). So, with a value of "10", application will create 20 records in file.
	d) "useSummaryStatistics" (optional, default:false) = There are 2 ways to process the file. One is using Collectors.groupingBy and Collectors.summarizingLong via Streams API. The other way is using Map.putIfAbsent method, which is default.
	
Multi-threaded solution acquired by using parallel streams.
	
Database connection string may be found in console output. It was "jdbc:hsqldb:hsql://TCNTSCONS078:4000/LXFTCS_DB" on my PC. You may check the table "LOG_EVENT_ALERT" for examining the records. HSQLDB files located under "target\HSQLDatabase" folder.
	
Created file includes following lines. Also you may find a screenshot file (all-records.JPG) of "LOG_EVENT_ALERT" records as attached. 
	
{"id":"dGcEZvJEDZ","state":"STARTED","type":null,"host":null,"timestamp":1567551126241}
{"id":"FTRYRBGtrY","state":"FINISHED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126236}
{"id":"paMIRTRhii","state":"FINISHED","type":null,"host":null,"timestamp":1567551126251}
{"id":"RzokHhXhiW","state":"STARTED","type":null,"host":null,"timestamp":1567551126241}
{"id":"cGQvcmHULG","state":"STARTED","type":null,"host":null,"timestamp":1567551126242}
{"id":"cGQvcmHULG","state":"FINISHED","type":null,"host":null,"timestamp":1567551126247}
{"id":"lEMxwlaBLz","state":"FINISHED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126246}
{"id":"UjIHBLPkJN","state":"STARTED","type":null,"host":null,"timestamp":1567551126242}
{"id":"WsrmAUbzDr","state":"STARTED","type":null,"host":null,"timestamp":1567551126242}
{"id":"paMIRTRhii","state":"STARTED","type":null,"host":null,"timestamp":1567551126243}
{"id":"UjIHBLPkJN","state":"FINISHED","type":null,"host":null,"timestamp":1567551126244}
{"id":"dGcEZvJEDZ","state":"FINISHED","type":null,"host":null,"timestamp":1567551126245}
{"id":"SoEnaLzBzw","state":"STARTED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126241}
{"id":"KFCJsrWkLg","state":"FINISHED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126245}
{"id":"WsrmAUbzDr","state":"FINISHED","type":null,"host":null,"timestamp":1567551126250}
{"id":"lEMxwlaBLz","state":"STARTED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126243}
{"id":"KFCJsrWkLg","state":"STARTED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126242}
{"id":"FTRYRBGtrY","state":"STARTED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126235}
{"id":"SoEnaLzBzw","state":"FINISHED","type":"APPLICATION_LOG","host":"12345","timestamp":1567551126243}
{"id":"RzokHhXhiW","state":"FINISHED","type":null,"host":null,"timestamp":1567551126243}
