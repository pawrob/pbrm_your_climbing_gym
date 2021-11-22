# PerfectBeta - Spring Boot

## Requirements

* Java 11
* Maven 3.6
* Docker

## Installation and run

### 1. Clone repository

````
git clone https://github.com/pawrob/perfectbeta.git
````

### 2. Run database

````
docker build -t perfectbeta_db db
docker run -dp 2137:5432 perfectbeta_db
````

### 3. Run spring boot rest api with tests

````
mvn clean package
java -jar target/perfectbeta.jar
````

