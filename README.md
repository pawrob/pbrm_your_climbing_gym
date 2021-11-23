# PerfectBeta - Spring Boot
<img src="https://i.imgur.com/aKSxfR5.png"  width="200px"/>  
## Requirements

* Java 11
* Maven 3.6
* Docker

## Installation and run

### 1. Clone repository
````
git clone https://github.com/pawrob/perfectbeta.git
````
### 2. Set credentials for gmail and cloudinary in [application.properties](https://github.com/pawrob/perfectbeta/blob/main/src/main/resources/application.properties)
### 3. Run database
````
docker build -t perfectbeta_db db
docker run -dp 5432:5432 perfectbeta_db
````
### 4. Run spring boot rest api with tests

````
mvn clean package
java -jar target/perfectbeta.jar
````

