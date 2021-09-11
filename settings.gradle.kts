rootProject.name = "cqrs-product"
include(
    "batch",
    "command",
    "common",
    "domain:mysql",
    "domain:mongo",
    "infrastructure:jpa",
    "infrastructure:querydsl",
    "infrastructure:mongo",
    "infrastructure:redis",
    "infrastructure:rabbitmq",
    "infrastructure:restclient",
    "query"
)
