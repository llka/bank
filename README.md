# Run Locally

#### Requirements:
- Docker
- MacOS

Open terminal in project folder and execute:
```$xslt
./start.sh
./stop.sh
```

If not enough permissions to execute scripts, run:
```$xslt
chmod +x start.sh
chmod +x stop.sh
```
#Api -  Swagger Documentation
http://localhost:8080/swagger-ui.html

# Connect to PgAdmin
1. Open in browser http://localhost:8000
2. Enter credentials: 
    ```
    admin@mail.ru
    admin
    ```
3. Add New Server -> Connection Tab
   ```$xslt
    host: postgre-db
    port: 5432
    database: bank
    username: admin
    password: admin
    ```

# Run Locally in dev mode
Carry out in terminal:
```$xslt
./gradlew clean build
./gradlew jibDockerBuild
docker-compose up -d
```


