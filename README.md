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

## 시스템 아키텍쳐

![image](https://user-images.githubusercontent.com/23515771/127999493-441ce63c-1ad3-4abe-bc6c-6808f13b2c9e.png)

## 헥사고날 아키텍쳐

- `Command`, `Query` 모듈은 `헥사고날 아키텍쳐`를 따른다.

![image](https://user-images.githubusercontent.com/23515771/128000044-b4520de0-035e-4930-8a81-95bf3e8c9e59.png)
