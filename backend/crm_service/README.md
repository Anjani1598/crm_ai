# CRM Microservice

This is the backend microservice for the CRM application, built with Spring Boot.

## Features

- Contact management
- Deal management
- Interaction tracking
- RESTful APIs
- Swagger/OpenAPI documentation
- PostgreSQL database
- Docker support

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Docker (optional)

## Setup

1. Clone the repository
2. Create a PostgreSQL database named `crm_db`
3. Update the database configuration in `src/main/resources/application.yml` if needed
4. Build the project:
   ```bash
   mvn clean package
   ```

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Docker

```bash
docker build -t crm-service .
docker run -p 8081:8081 crm-service
```

## API Documentation

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8081/api/v1/swagger-ui.html
```

## API Endpoints

### Contacts

- GET /api/v1/contacts - Get all contacts
- GET /api/v1/contacts/{id} - Get contact by ID
- GET /api/v1/contacts/email/{email} - Get contact by email
- POST /api/v1/contacts - Create new contact
- PUT /api/v1/contacts/{id} - Update contact
- DELETE /api/v1/contacts/{id} - Delete contact

### Deals

- GET /api/v1/deals - Get all deals
- GET /api/v1/deals/{id} - Get deal by ID
- GET /api/v1/deals/contact/{contactId} - Get deals by contact
- GET /api/v1/deals/stage/{stage} - Get deals by stage
- GET /api/v1/deals/status/{status} - Get deals by status
- POST /api/v1/deals - Create new deal
- PUT /api/v1/deals/{id} - Update deal
- PATCH /api/v1/deals/{id}/stage - Update deal stage
- PATCH /api/v1/deals/{id}/status - Update deal status
- DELETE /api/v1/deals/{id} - Delete deal

### Interactions

- GET /api/v1/interactions - Get all interactions
- GET /api/v1/interactions/{id} - Get interaction by ID
- GET /api/v1/interactions/contact/{contactId} - Get interactions by contact
- GET /api/v1/interactions/contact/{contactId}/latest - Get latest interactions by contact
- GET /api/v1/interactions/deal/{dealId} - Get interactions by deal
- POST /api/v1/interactions - Create new interaction
- PUT /api/v1/interactions/{id} - Update interaction
- DELETE /api/v1/interactions/{id} - Delete interaction

## Security

The application uses Spring Security for authentication and authorization. Currently, it's configured to:

- Allow access to Swagger UI and API docs
- Require authentication for all other endpoints
- Use stateless session management

## Development

### Project Structure

```
src/
├── main/
│   ├── java/com/crm/
│   │   ├── config/               # Configuration classes
│   │   ├── controller/           # REST controllers
│   │   ├── domain/              # Domain layer
│   │   │   ├── model/           # Entities
│   │   │   ├── repository/      # Repositories
│   │   │   └── service/         # Services
│   │   ├── dto/                 # Data transfer objects
│   │   ├── mapper/              # Object mappers
│   │   └── CrmApplication.java  # Main class
│   └── resources/
│       └── application.yml      # Configuration
└── test/                        # Test classes
```

### Adding New Features

1. Create entity in `domain/model`
2. Create repository in `domain/repository`
3. Create DTO in `dto`
4. Create mapper in `mapper`
5. Create service in `domain/service`
6. Create controller in `controller`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
