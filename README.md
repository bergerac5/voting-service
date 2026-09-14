# Voting Service

The voting service is the core voting engine of the online voting platform. It validates election eligibility, checks voter and candidate data, prevents duplicate votes per position, persists vote records, and emits a vote-casted event to the Kafka event bus for downstream consumers such as result aggregation.

## Overview

This service is responsible for:

- accepting vote submissions from eligible voters
- validating election openness and voter eligibility
- preventing duplicate voting for the same election position
- persisting votes in PostgreSQL
- publishing vote events to Kafka through the outbox pattern
- integrating with voter, election, position, and candidate services via Feign clients

## Runtime and technology stack

- Java 25
- Spring Boot 3.5.0
- Spring Cloud 2025.0.0
- Spring Cloud Stream with Kafka
- Spring Data JPA / PostgreSQL
- Spring Security + JWT
- OpenFeign
- Resilience4j circuit breakers
- Maven

## Service port and configuration

The service runs on port 8085.

Main configuration is defined in [src/main/resources/application.yaml](src/main/resources/application.yaml):

- database: PostgreSQL at localhost:5432 / vote_votingdb
- Kafka broker: localhost:9092
- output topic: vote-casted
- Feign circuit breakers enabled
- actuator health endpoints exposed
- logging enabled for SQL, Spring Cloud Stream, and OpenFeign

## Security model

Security is configured in [src/main/java/com/online/voting/voting/config/SecurityConfig.java](src/main/java/com/online/voting/voting/config/SecurityConfig.java).

Current rules:

- `/actuator/**` → public
- `/elections/health` → public
- `/votes/**` → requires role `VOTER`
- all other requests → authenticated
- CSRF disabled
- JWT filter applied before the Spring Security authentication filter

The service validates bearer tokens through the custom JWT filter before allowing access to protected vote endpoints.

## API endpoint

Base path: `/votes`

### Cast a vote

- Method: `POST`
- Endpoint: `/votes`
- Description: validates election status and voter eligibility, writes the vote, and returns confirmation metadata
- Request body: `CastVoteRequest`
- Response: `ApiResponse<CastVoteResponse>`

### Example request payload

```json
{
  "voterId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "electionId": "f0f10a40-5a5a-4d6d-a83b-1d34edc3c2d5",
  "positionId": "8f5c6c0e-3608-4d0b-9a04-3f8a7139d123",
  "candidateId": "8d3d7f48-cc77-43ef-a4e0-f7b1b100f5a7"
}
```

## Business rules

The vote flow enforces several key rules before a vote is accepted:

1. the election must exist
2. the election must be open for voting
3. the voter must exist
4. the position must exist
5. the candidate must exist
6. the voter cannot vote twice for the same position in the same election
7. the vote is persisted transactionally with an outbox event for reliability

## Data model

The core persistence entity is `Vote`.

Key constraints:

- one vote record per voter-position pair within the election context
- unique index on `voter_id` + `position_id`
- candidate and election metadata are not denormalized in the vote table; they are validated through remote service calls

The entity also stores:

- vote ID
- voter ID
- election ID
- position ID
- candidate ID
- voted timestamp

## Integration with other services

This service calls other platform services through Feign clients:

- voter service: validates the voter account
- election service: validates the election and voting state
- position service: checks the target position
- candidate service: confirms the candidate exists

If any of those services fail or return a missing-resource response, the vote request fails fast with a domain-specific exception.

## Event flow

The service publishes vote events for downstream processing.

### Outbound event

- topic: `vote-casted`
- event type: `VoteEvent`
- payload includes vote ID, election ID, position ID, candidate ID, voter ID, and vote timestamp

This is emitted through the outbox publisher and Spring Cloud Stream binding defined in the application config.

## Duplicate protection and idempotency

The service prevents duplicate votes through database integrity constraints and business validation.

Relevant logic includes:

- repository checks for voter-position voting duplicates
- transaction-bound save and outbox persistence
- domain exception `DuplicateVoteException` for duplicate attempts

## Project structure

```text
voting-service/
├── src/
│   ├── main/
│   │   ├── java/com/online/voting/voting/
│   │   │   ├── clients/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dtos/
│   │   │   ├── handler/
│   │   │   ├── models/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── VotingServiceApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── target/
```

## Prerequisites

Before running the service, ensure:

- Java 25 is installed
- PostgreSQL is running and the database `vote_votingdb` exists
- Kafka is running on localhost:9092
- the auth, election, candidate, position, and voter services are reachable
- the `common-events` shared module is available to Maven

## Run locally

From the service folder:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-25.0.4.1'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
./mvnw clean install
./mvnw spring-boot:run
```

## Health and verification

You can verify the service is healthy with:

- http://localhost:8085/actuator/health
- application logs showing Kafka and database startup without errors

## Notes

- Vote submission is intentionally guarded by external service validation before the database write occurs.
- The service uses the outbox pattern to make vote publication resilient and consistent with eventual event processing.
- The project uses `ddl-auto: update` for local development; production settings should be reviewed before deployment.

## Summary

The voting service is responsible for the final business action in the election workflow: accepting a valid vote, enforcing election rules, persisting the record, and publishing an event that result aggregation services can process. It acts as the authoritative vote-capture layer between user actions and downstream results processing.
