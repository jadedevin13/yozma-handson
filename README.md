# Question Platform API

A Spring Boot application that provides a REST API for managing a question-asking platform. The platform supports different types of questions (Poll and Trivia) with voting capabilities.

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle
- IDE (IntelliJ IDEA recommended)

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the application using Gradle:
```bash
./gradlew bootRun
```
Or using your IDE:
- Open the project in IntelliJ IDEA
- Run `Application.java`

The application will start on `http://localhost:8080`

### Database
The application uses H2 in-memory database. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:questiondb`
- Username: `sa`
- Password: `password`

## API Endpoints

### 1. Create Question
Creates a new question (Poll or Trivia)

**Endpoint:** `POST /api/questions`

#### Poll Question Example
```bash
curl -X POST http://localhost:8080/api/questions \
-H "Content-Type: application/json" \
-d '{
    "type": "POLL",
    "text": "Favorite programming language?",
    "answers": ["Java", "Python", "JavaScript"]
}'
```

#### Trivia Question Example
```bash
curl -X POST http://localhost:8080/api/questions \
-H "Content-Type: application/json" \
-d '{
    "type": "TRIVIA",
    "text": "Which of these are programming languages?",
    "answers": ["Java", "Python", "London", "Paris"],
    "correctAnswers": ["Java", "Python"]
}'
```

**Response:**
```json
{
    "id": 1,
    "text": "Which of these are programming languages?",
    "type": "TRIVIA",
    "answers": ["Java", "Python", "London", "Paris"],
    "correctAnswers": ["Java", "Python"],
    "votes": {
        "Java": 0,
        "Python": 0,
        "London": 0,
        "Paris": 0
    }
}
```

### 2. Get Question
Retrieves a question by ID

**Endpoint:** `GET /api/questions/{id}`

```bash
curl -X GET http://localhost:8080/api/questions/1
```

**Response:**
```json
{
    "id": 1,
    "text": "Which of these are programming languages?",
    "type": "TRIVIA",
    "answers": ["Java", "Python", "London", "Paris"],
    "correctAnswers": ["Java", "Python"],
    "votes": {
        "Java": 0,
        "Python": 0,
        "London": 0,
        "Paris": 0
    }
}
```

### 3. Vote on Question
Submit a vote for a question

**Endpoint:** `POST /api/questions/{id}/vote`

```bash
curl -X POST http://localhost:8080/api/questions/1/vote \
-H "Content-Type: application/json" \
-d 'Java'
```

**Response for Poll:**
```json
{
    "votes": {
        "Java": 1,
        "Python": 0,
        "JavaScript": 0
    }
}
```

**Response for Trivia:**
```json
{
    "votes": {
        "Java": 1,
        "Python": 0,
        "London": 0,
        "Paris": 0
    },
    "isCorrect": true,
    "correctAnswers": ["Java", "Python"]
}
```

## Validation Rules

### Poll Questions
- Must have a text
- Must have between 2 and 10 answers
- No correct answers needed

### Trivia Questions
- Must have a text
- Must have between 2 and 10 answers
- Must have at least one correct answer
- All correct answers must be from the provided answers list

## Error Handling

The API returns appropriate HTTP status codes:
- 201: Resource created successfully
- 400: Bad request (validation errors)
- 404: Resource not found
- 500: Internal server error

Example error response:
```json
{
    "timestamp": "2024-11-07T10:30:00.000+00:00",
    "status": 400,
    "error": "Validation Failed",
    "messages": [
        "Trivia questions must have at least one correct answer"
    ]
}
```
