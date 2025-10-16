@echo off
echo Currency Converter Application
echo =============================
echo.
echo This Spring Boot application provides currency conversion functionality
echo using real-time exchange rates from Magyar Nemzeti Bank.
echo.
echo To run this application, you need:
echo 1. Java 11 or higher
echo 2. Maven 3.6 or higher
echo.
echo Commands to run the application:
echo.
echo 1. Build the project:
echo    mvn clean compile
echo.
echo 2. Run the application:
echo    mvn spring-boot:run
echo.
echo 3. Access the web interface:
echo    http://localhost:8080
echo.
echo 4. Test the REST API:
echo    curl -X POST http://localhost:8080/api/convert ^
echo         -H "Content-Type: application/json" ^
echo         -d "{\"fromCurrency\":\"EUR\",\"toCurrency\":\"USD\",\"amount\":100.00}"
echo.
echo Project Structure:
echo - src/main/java: Java source code
echo - src/main/resources: Configuration and templates
echo - pom.xml: Maven configuration
echo - README.md: Documentation
echo.
pause
