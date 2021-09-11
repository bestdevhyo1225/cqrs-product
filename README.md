# [CQRS Pattern] Product System

## Module

- Batch
- Command
- Common
- Domain
- Query
- Infrastructure
  - RDBMS
  - NoSQL
  - RabbitMQ
  - RestClient

## Library & Framework

- JDK 11
- Spring Web
- Spring Batch
- Spring Data JPA
- Spring Data MongoDB
- Spring Data Redis
- Spring AMQP (RabbitMQ)
- Kotlinx Coroutines Core
- JUnit 5

## Issue 해결 과정

- [Mongo DB 인증 기능 추가 하기(Docker 기반)](https://hyos-dev-log.tistory.com/4)

## CQRS 패턴

![image](https://user-images.githubusercontent.com/23515771/132529489-8b277d9c-87c7-424a-a849-c3fd9fd0b91c.png)

## 헥사고날 아키텍쳐

- `Command`, `Query` 모듈은 `헥사고날 아키텍쳐`를 따른다.

![image](https://user-images.githubusercontent.com/23515771/129570406-7cbf95b2-7745-4125-99cf-dbd73ae60c1d.png)
