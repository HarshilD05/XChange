# XChange Main Server

## Prerequisites

- Java 21 or newer
- Maven 3.9+
- PostgreSQL

## Database setup

```sql
CREATE DATABASE xchange;
CREATE USER xchange_admin WITH PASSWORD 'admin-password';
CREATE USER xchange_user WITH PASSWORD 'application-password';
```

The intended roles are `xchange_admin` for schema ownership and `xchange_user`
for application CRUD:

- Run `src/main/resources/schema.sql` as `xchange_admin`.
- For an existing database, also run
  `src/main/resources/migrations/001_align_existing_schema.sql` as `xchange_admin`.
- Configure the application with `xchange_user`; do not enable `spring.sql.init`.
- Grant `xchange_user` SELECT, INSERT, UPDATE, and DELETE permissions on `users`,
  `players`, and `lobbies`.

Copy `src/main/resources/application.properties.example` to
`src/main/resources/application.properties`, then update the database
credentials.

## Run

```bash
mvn spring-boot:run
```

The server listens on port `8080` by default.

## Key endpoints

- `GET /api/health` - XChange identity and activity response
- `GET /api/users`
- `POST /api/users/admin`
- `POST /api/users/player`
- `PATCH /api/users/{username}/password`
- `GET /api/players`
- `PATCH /api/players/{username}/status`
- `PATCH /api/players/{username}/account-status`
- `GET /api/lobbies`
- `GET /api/lobbies/code/{lobbyCode}`
- `POST /api/lobbies`
- `PATCH /api/lobbies/{id}/status`

Lobby creation accepts only `name`. The server generates a unique six-character
uppercase alphanumeric code that is not used by an active `OPEN` or `ONGOING`
lobby. `GET /api/lobbies/code/{lobbyCode}` returns every lobby with that code.
Lobby statuses are `OPEN`, `ONGOING`, and `CLOSED`.

Account statuses are `ACTIVE` and `SUSPENDED`. Admins can suspend or reactivate a
player with `PATCH /api/players/{username}/account-status`.

Passwords are changed with `PATCH /api/users/{username}/password`. The request
must contain `oldPassword` and `newPassword`; an incorrect old password returns
HTTP 401.
