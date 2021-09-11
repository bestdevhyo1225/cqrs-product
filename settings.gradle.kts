rootProject.name = "cqrs-product"
include(
    "batch",
    "command",
    "common",
    "domain:mysql",
    "domain:mongo",
    "infrastructure:nosql",
    "infrastructure:rabbitmq",
    "infrastructure:rdbms",
    "infrastructure:restclient",
    "query"
)
