In IntelliJ set .env:

run->Edit configurations->env variables-> "fronted/.env"

Open Run → Edit Configurations…
Select your BackendApplication run config.
In the field VM options, add: (modify options, if no vm options showing)
-Dspring.profiles.active=dev


# Neiwa Backend

Spring Boot (Java 21, Gradle) backend service for the [Neiwa project](https://neiwa.eu).  
Deployed alongside the frontend with **blue/green zero-downtime deploys** on the Hetzner VM.

---

## Endpoints

- **GET `/hello`** → `{ "message": "hello" }`

Frontend and Nginx mount the backend under `/api/*`.  
From the browser you call `/api/hello`, but inside the backend the route is just `/hello`.

---

## Local Development

### Requirements
- Java 21 (OpenJDK)
- Gradle (wrapper included)
- Node.js (for frontend dev server)

### Run backend only
```bash
./gradlew bootRun
Backend runs on the port from .env (PORT_DEV_BACKEND).
Default: 8080 (we use 7778 in .env locally).

Run frontend + backend together
In frontend/.env set:

dotenv
Copy code
PORT_DEV=1337
PORT_DEV_BACKEND=7778
Start backend:

bash
Copy code
./gradlew bootRun
Start frontend:

bash
Copy code
npm run dev
Open http://localhost:1337/api/hello

Production (Hetzner VM)
Repos live in:

~/neiwa/frontend

~/neiwa/backend

Shared env: ~/neiwa/frontend/.env

Ports
Internal (Docker):

Frontend → 1337

Backend → 7777

External (per-branch in .env):

dotenv
Copy code
PORT_MAIN_BLUE=13337
PORT_MAIN_GREEN=1337

PORT_BACKEND_MAIN_BLUE=2222
PORT_BACKEND_MAIN_GREEN=2112
Nginx Config
nginx
Copy code
location / {
    proxy_pass http://127.0.0.1:__PORT__;
}

location /api/ {
    proxy_pass http://127.0.0.1:__PORT_BACKEND_EXTERNAL__/;
}
⚠️ The trailing / after /api/ strips the prefix → backend only needs /hello.

Blue/Green Deploys
Each branch has two slots: blue and green.

On deploy:

Build new containers ($BRANCH-$NEWCOLOR)

Rewrite Nginx to point at new ports

Stop old containers ($BRANCH-$CURRENTCOLOR)

Reload Nginx → zero downtime

Repo Structure
css
Copy code
backend/
 ├── src/
 │   └── main/java/com/neiwa/backend/
 │        ├── BackendApplication.java
 │        └── HelloController.java
 ├── build.gradle
 ├── settings.gradle
 └── Dockerfile
Useful Commands
Tail backend logs:

bash
Copy code
docker logs -f main-green-backend-1
Check backend port inside container:

bash
Copy code
docker exec -it main-green-backend-1 curl http://localhost:7777/hello
Curl via host external port:

bash
Copy code
curl http://127.0.0.1:2112/hello