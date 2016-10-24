# monty-hall-java
Solution by simulation to the Monty Hall problem

Requirements:

- [JRE 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
- [Maven](https://maven.apache.org/)

If you prefer not install Maven, you can use the wrapper to run the simulator:

```
./mvnw spring-boot:run
```

The default number of simulations is 10. You can pass it explicitly as an argument:

```
./mvnw spring-boot:run -DtotalSimulations=1000
```

If you prefer, you can build the jar file:

```
./mvnw clean install spring-boot:repackage
java -jar target/monty-hall-1.0-SNAPSHOT.jar --totalSimulations=100
```