# Платформа шеринга вещей

![API Tests](https://github.com/agluh/java-shareit/actions/workflows/api-tests.yml/badge.svg)

Сервис, во-первых, обеспечивает пользователям возможность рассказывать, какими вещами они готовы поделиться, 
а во-вторых, находить нужную вещь и брать её в аренду на какое-то время.

## Стек
- Java 11
- Spring Boot 2.7.2
- PostgreSQL 42.3
- Maven сборка

## Архитектура

Приложение состоит из двух сервисов:
* server - основной сервис
* gateway - шлюз для валидации запросов

## TODO:

* неплохо бы прикрутить swagger документацию для API

## Сборка и развёртывание
Требуется установленный Apache Maven

```
git clone git@github.com:agluh/java-shareit.git
cd java-shareit
mvn package
echo DB_USER=shareit > .env
echo DB_PASS=shareit >> .env
docker-compose up -d
```