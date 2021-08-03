# [CQRS Pattern] Product System

## Module

- Batch
- Command
- Common
- Domain
- Query

## Library & Framework

- JDK 11
- Spring Web
- Spring Batch
- Spring Data JPA
- Spring Data MongoDB
- Spring AMQP (RabbitMQ)
- Kotlinx Coroutines Core
- JUnit 5

## Issue 해결 과정

- [Mongo DB 인증 기능 추가 하기(Docker 기반)](https://hyos-dev-log.tistory.com/4)

## CQRS 패턴

![image](https://user-images.githubusercontent.com/23515771/128012900-d897a8de-ce09-4c19-a6bd-9207044500a3.png)

## 헥사고날 아키텍쳐

- `Command`, `Query` 모듈은 `헥사고날 아키텍쳐`를 따른다.
