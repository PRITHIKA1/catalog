# Catalog - Discount API

A Kotlin-based discount management service built with Ktor framework and MongoDB.

## Prerequisites

- JDK 11 or higher
- MongoDB instance running
- Gradle (included via wrapper)

## Environment Variables

Set the following environment variables before running the application:

```bash
CATALOG_DB_NAME=catalog_db
MONGO_DB_CONNECTION_STRING=mongodb://localhost:27017
MAX_DB_CONNECTION_IDLE_TIME=60000
MIN_DB_CONNECTIONS=5
MAX_DB_CONNECTIONS=20
```

## Build Instructions

### Windows

```powershell
cd discount
.\gradlew.bat build
```

### Linux/macOS

```bash
cd discount
./gradlew build
```

## Run Instructions

### Windows

```powershell
cd discount
.\gradlew.bat run
```

### Linux/macOS

```bash
cd discount
./gradlew run
```

The application will start on `http://localhost:8080`

## Run Tests

### Windows

```powershell
cd discount
.\gradlew.bat test
```

### Linux/macOS

```bash
cd discount
./gradlew test
```

## API Endpoints

### Products

#### Get All Products

```bash
curl http://localhost:8080/api/discount/products
```

#### Get Products by Country

```bash
# Sweden
curl http://localhost:8080/api/discount/products?country=Sweden

# French
curl http://localhost:8080/api/discount/products?country=French

# Italian
curl http://localhost:8080/api/discount/products?country=Italian
```

### Discounts

#### Apply Discount to Product

```bash
# Apply 15% discount to product PROD001
curl -X PUT http://localhost:8080/api/discount/products/PROD001/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC005",
    "percent": 15.0
  }'
```

#### Apply Higher Discount

```bash
# Apply 25% discount to product PROD003
curl -X PUT http://localhost:8080/api/discount/products/PROD003/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC006",
    "percent": 25.0
  }'
```

#### Apply Multiple Discounts

```bash
# Apply additional discount to product that already has one
curl -X PUT http://localhost:8080/api/discount/products/PROD002/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC007",
    "percent": 10.0
  }'
```

#### Apply Edge Case Discounts

```bash
# Apply 0% discount (edge case)
curl -X PUT http://localhost:8080/api/discount/products/PROD005/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC008",
    "percent": 0.0
  }'

# Apply 100% discount (free product)
curl -X PUT http://localhost:8080/api/discount/products/PROD007/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC009",
    "percent": 100.0
  }'
```

## Error Testing

### Invalid Country

```bash
curl http://localhost:8080/api/discount/products?country=Germany
```

### Apply Duplicate Discount

```bash
curl -X PUT http://localhost:8080/api/discount/products/PROD002/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC001",
    "percent": 20.0
  }'
```

### Apply Discount to Non-existent Product

```bash
curl -X PUT http://localhost:8080/api/discount/products/PROD999/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC999",
    "percent": 30.0
  }'
```

## Project Structure

```
discount/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/io/nexure/discount/
│   │   │   │   ├── Application.kt
│   │   │   │   ├── config/
│   │   │   │   └── ...
│   │   │   └── resources/
│   │   │       ├── application.conf
│   │   │       └── logback.xml
│   │   └── test/
│   │       └── kotlin/io/nexure/discount/
│   └── build.gradle.kts
├── gradle/
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```
