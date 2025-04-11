## Build PostgreSQL Docker Image with Preloaded Data

This directory contains the configuration to build a PostgreSQL Docker image preloaded with data from a backup file. It is intended for local development and testing. On first run, the container will automatically import data from `full_backup.sql`. 

### Build the Image

Run the build command:

```bash
docker build -t tourwise-postgresdb-build-from-backend-file .
```

### Run the Container

Make sure that the value of `POSTGRES_DB` matches the database name **`mydatabase`** used in the `full_backup.sql` file, otherwise the data may not be imported properly during container initialization.

```bash
docker run -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -d --name tourwise-database-build-from-file -p 5432:5432 tourwise-postgresdb-build-from-backend-file
```

