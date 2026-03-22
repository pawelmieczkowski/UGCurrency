# UG Currency App
## Technologies & Libraries
### Backend
- **H2 Database**  
- **Maven** 
- **JDK 21**

### Frontend
- **Swing**I  
- **LGoodDatePicker** 
    
## Prerequisites
- **Java 21+**  
- **Maven 3+**  

> **NBP API Note:**  
> To use the NBP API, you may need to add the **Certum Trusted Root CA** certificate to your Java keystore:  
> 1. Download the certificate from [Certum](https://www.certum.pl/pl/cert_wiedza_zaswiadczenia_klucze_certum/)  
> 2. Import it using the keytool:  
```bash
keytool -importcert -trustcacerts -alias certum -file "nameOfFile" -keystore "%JAVA_HOME%\lib\security\cacerts" -storepass changeit
```
## How to Run

### 1. Build the project:
```bash
mvn clean package
```

### 2. Run the executable JAR:
```bash
java -jar target/ug-currency-1.0-SNAPSHOT.jar
```
