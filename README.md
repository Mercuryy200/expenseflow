# ExpenseFlow - Personal Finance REST API

A RESTful API for tracking personal income and expenses, built with Spring Boot 3.

## Features

- ✅ JWT Authentication & Authorization
- ✅ CRUD operations for transactions
- ✅ Monthly financial summaries
- ✅ Category-based filtering
- ✅ Pagination & sorting
- ✅ 50% test coverage

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.2.2
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT
- **Testing:** JUnit 5, Mockito, MockMvc
- **Build:** Maven

## Setup

1. **Prerequisites:**
   - Java 21
   - PostgreSQL
   - Maven

2. **Database Setup:**
```sql
   CREATE DATABASE expenseflow;
   CREATE USER expenseflow_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE expenseflow TO expenseflow_user;
```

3. **Configure Application:**
```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   # Edit application.properties with your database credentials
```

4. **Run:**
```bash
   mvn spring-boot:run
```

## Testing
```bash
# Run all tests
mvn test

# Generate coverage report
mvn test jacoco:report
open target/site/jacoco/index.html
```

**Test Coverage:** 50% (18+ tests)

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login (returns JWT)
- `GET /api/auth/me` - Get current user

### Transactions (requires JWT)
- `POST /api/transactions` - Create transaction
- `GET /api/transactions` - Get all transactions (with filters)
- `GET /api/transactions/{id}` - Get transaction by ID
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction
- `GET /api/transactions/summary?month=2026-02` - Monthly summary

### Query Parameters
- `type` - Filter by INCOME/EXPENSE
- `category` - Filter by category
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)
- `sortBy` - Sort field (default: transactionDate)
- `direction` - ASC/DESC (default: DESC)

## Example Usage
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'

# Create Transaction (use token from login)
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"amount":50.00,"description":"Groceries","category":"FOOD","type":"EXPENSE","transactionDate":"2026-02-10"}'
```

## Future Enhancements

- [ ] Budget goals and alerts
- [ ] Recurring transactions
- [ ] Data visualization dashboard
- [ ] Export to CSV/PDF
- [ ] Multi-currency support

## Author

Rima - [GitHub](https://github.com/Mercuryy200) | [LinkedIn](https://linkedin.com/in/rima-nafougui)

## License

MIT License
