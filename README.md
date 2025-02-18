# CommVault

> An Open Source Password Management Tool

[![CI](https://github.com/shortthirdman/CommVault/actions/workflows/maven-docker-triage.yml/badge.svg)](https://github.com/shortthirdman/CommVault/actions/workflows/maven-docker-triage.yml)

**_CommVault_**, an open-source password management tool designed to securely store and manage passwords. Built using Java with Spring Boot 3.x and Spring Cloud, it leverages Redis Server for fast, in-memory data caching and HashiCorp Vault for robust, centralized secrets management. CommVault provides a highly scalable, secure solution for handling sensitive information, ideal for both small teams and enterprise-level applications.

## Key features:

- Secure password storage and retrieval.
- Integration with HashiCorp Vault for secret management.
- High performance with Redis for caching and faster access.
- Scalable architecture powered by Spring Cloud.
- This tool aims to simplify and enhance the security of password management for developers and organizations.

## Setup Local

#### redis/redis-stack

```sh
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 --restart unless-stopped -e REDIS_ARGS="--requirepass admin --protected-mode yes --save 60 1000 --appendonly yes --loglevel warning" -e REDISTIMESERIES_ARGS="RETENTION_POLICY=20" redis/redis-stack:6.2.6-v17
```

#### redis/redis-stack-server

```sh
docker run -d --name redis-stack-server -p 6379:6379 --restart unless-stopped -e REDIS_ARGS="--requirepass admin --protected-mode yes --save 60 1000 --appendonly yes --loglevel warning" -e REDISTIMESERIES_ARGS="RETENTION_POLICY=20" redis/redis-stack-server:6.2.6-v17
```

#### Docker Build

```
docker build --rm --compress -t shortthirdman/commvault:latest .
```