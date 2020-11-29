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

# Run Locally
```$xslt
chmod +x start.sh
chmod +x stop.sh

./start.sh
./stop.sh
```


Open in browser `localhost:8080/bank/api`

#Api -  Swagger Documentation
http://localhost:8080/swagger-ui.html


# Run Locally in dev mode
Carry out in terminal:
```$xslt
./gradlew clean build
./gradlew jibDockerBuild
docker-compose up -d
```


