# Currency-Converter

API to convert one currency to another, ex. BRL to USD.



### Installation

First to start this project is necessary install the Java 11, Maven 3.6.3 and Docker. After that, it is necessary make the download of the mongoDB image.

```
docker pull mongo:latest
```

After pulling the mongo image, run the following command to run it. 
```
docker run --name mongoDB -d mongo:lastes
```

#### Getting Started

After install the frameworks and database it's time to clone the project.

```
git clone https://github.com/malbarbosa/currency-converter.git
```

#### Starting up API

At terminal or prompt command, access the project folder and execute the following commands.

Step 1:
```
mvn clean package
```
Step 2:
```
java -jar currency-converter-1.0.0.jar
```

### Swagger

The documentation of API can find it at link: ```http://localhost:8080/swagger-ui.html```