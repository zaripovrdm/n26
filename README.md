This project uses Spring Web Flux and Netty server.
#### How to build and run
Use gradle to build this project: 
```
gradle jar test
```
It will create artifact located in **build/libs/n26.jar**. You can start it using command line: 
```
java -jar n26.jar
```
Test results can be found in **build/test-results/test/**
Application entry point is **com.n26.challenge.Main**

#### About implementing O(1) requirement
It's obvious that creating aggregated summary requires iterating over all the data that belong to 60 second time interval. But it will violate O(1) requirement, because iteration has O(n) complexity. So we cannot iterate over all transactions during /statistics service invocation. But we can do preliminary computations in /transactions service because task has no restrictions about this endpoint. Main idea is to create intermediate statistics with per second basis. Then 60-seconds statistics will always be formed from last 60 entries (maximum) from these intermediate results. To get last entries we also need sorting during preliminary computations, because even best sorting hasn't O(1) complexity. Iterating with 60 elements will meet constant time and space requirement. See more in **com.n26.challenge.service.TransactionService**.