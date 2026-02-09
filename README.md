# Bank application
Bank application to manage accounts and initiate transactions.

# Application structure
- Frontend application 
- account-service (accounts store and managing)
- cash-service (for balance update)
- transfer-service (for money transfer initiation)
- notification-service (all actions notifications)
- API Gateway (fronend requests routing)
- Config server (Externalized configuration server)
- Authorization service (keycloak to store all users)

# Application launch

## 1. Prepare DataBase
1. Create new PostgreSQL database.
2. Execute DDL for schema creation (schema.sql).

## 2. Configure users and roles in keycloak

## 3. Configure environment variables

example of .env file:
```
AUTH_DB_USERNAME=postgres
AUTH_DB_PASSWORD=postgres
KC_ADMIN_USERNAME=admin
KC_ADMIN_PASSWORD=admin
AUTH_SERVER_HOST=auth-server.com
AUTH_SERVER_PORT=8090
ACCOUNT_DB_USERNAME=postgres
ACCOUNT_DB_PASSWORD=postgres
BANK_FRONT_UI_SECRET={SECRET}
ACCOUNT_SERVICE_SECRET={SECRET}
CASH_SERVICE_SECRET={SECRET}
TRANSFER_SERVICE_SECRET={SECRET}
```

## 4. Start application

There are 4 launch options: from IDE, local launch, docker launch, k8s via Helm chart.

1. Start from IDE (specify environment variables inside IDE)

2. Start locally
- Launch compiled .jar files one by one

3. Start as a Docker containers
- Launch Docker locally
- Execute ```docker compose up --build``` to start Docker containers.
- No need to specify ports / profiles manually when using Docker compose. Ports will be set automatically.

4. Launch in Kubernetes using Helm:
```
helm install bank-chart ./bank-chart
```