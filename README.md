# Currency Converter Application

A Spring Boot web application that provides currency conversion functionality using real-time exchange rates from the Magyar Nemzeti Bank (MNB).

## Features

- **Real-time Exchange Rates**: Fetches current exchange rates from Magyar Nemzeti Bank
- **REST API**: Provides RESTful endpoints for currency conversion
- **Modern Web UI**: Clean, responsive interface built with Bootstrap and Thymeleaf
- **Multiple Currencies**: Supports major world currencies including EUR, USD, GBP, CHF, JPY, CAD, AUD, HUF, RON, CZK, PLN, BGN, HRK, RSD, UAH, CNY, INR, BRL, MXN, KRW, SGD, NZD, NOK, SEK, DKK and more
- **Input Validation**: Comprehensive validation for all user inputs
- **Error Handling**: Robust error handling with user-friendly messages

## Technology Stack

- **Java 21**: Latest LTS version of Java
- **Spring Boot 3.2.0**: Modern Spring framework
- **Spring Web**: REST API development
- **Spring WebFlux**: Reactive web client for external API calls
- **Thymeleaf**: Server-side template engine
- **Bootstrap 5**: Frontend CSS framework
- **Maven**: Build and dependency management

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone or Download the Project

```bash
# If you have the project files, navigate to the project directory
cd currency-converter-app
```

### 2. Build the Application

```bash
mvn clean compile
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

### 4. Access the Application

- **Web Interface**: http://localhost:8080
- **REST API**: http://localhost:8080/api

## API Endpoints

### Convert Currency
```
POST /api/convert
Content-Type: application/json

{
    "fromCurrency": "EUR",
    "toCurrency": "USD",
    "amount": 100.00
}
```

### Get Available Currencies
```
GET /api/currencies
```

### Get Exchange Rates
```
GET /api/rates
```

## Usage

### Web Interface

1. Open http://localhost:8080 in your browser
2. Select the source currency from the dropdown
3. Enter the amount to convert
4. Select the target currency
5. Click "Convert" to see the result
6. View current exchange rates in the table below

### REST API

You can use the REST API endpoints programmatically:

```bash
# Convert 100 EUR to USD
curl -X POST http://localhost:8080/api/convert \
  -H "Content-Type: application/json" \
  -d '{"fromCurrency":"EUR","toCurrency":"USD","amount":100.00}'

# Get available currencies
curl http://localhost:8080/api/currencies
```

## Exchange Rate Source

The application fetches exchange rates from the Magyar Nemzeti Bank (MNB) SOAP API. If the MNB API is not accessible, the application falls back to mock exchange rates for demonstration purposes.

## Project Structure

```
src/
├── main/
│   ├── java/com/example/currencyconverter/
│   │   ├── CurrencyConverterApplication.java    # Main Spring Boot application
│   │   ├── controller/
│   │   │   ├── CurrencyController.java         # REST API controller
│   │   │   └── WebController.java              # Web UI controller
│   │   ├── model/
│   │   │   ├── CurrencyConversionRequest.java  # Request model
│   │   │   ├── CurrencyConversionResponse.java # Response model
│   │   │   └── ExchangeRate.java               # Exchange rate model
│   │   └── service/
│   │       ├── CurrencyConversionService.java  # Business logic
│   │       └── MnbExchangeRateService.java     # MNB API integration
│   └── resources/
│       ├── application.properties               # Configuration
│       ├── static/css/style.css                # Custom styles
│       └── templates/index.html                 # Main UI template
└── pom.xml                                     # Maven configuration
```

## Configuration

The application can be configured through `application.properties`:

```properties
server.port=8080
spring.application.name=currency-converter
mnb.api.url=https://www.mnb.hu/arfolyamok.asmx
mnb.api.timeout=10000
```

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package
java -jar target/currency-converter-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, please create an issue in the project repository.
