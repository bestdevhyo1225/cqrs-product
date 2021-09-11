rootProject.name = "cqrs-product"
include(
    "batch",
    "command",
    "common",
    "domain:mysql",
    "domain:mongo",
    "infrastructure:nosql:mongodb",
    "infrastructure:nosql:redis",
    "infrastructure:rabbitmq",
    "infrastructure:rdbms",
    "infrastructure:restclient",
    "query"
)
