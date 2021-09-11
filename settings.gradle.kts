rootProject.name = "cqrs-product"
include(
    "batch",
    "command",
    "common",
    "domain:mysql",
    "domain:mongo",
    "infrastructure:jpa",
    "infrastructure:querydsl",
    "infrastructure:mongodb",
    "infrastructure:redis",
    "infrastructure:rabbitmq",
    "infrastructure:restclient",
    "query"
)
