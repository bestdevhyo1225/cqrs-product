rootProject.name = "cqrs-product"
include("command", "common", "domain:mysql", "domain:mongo", "external:rabbitmq", "query")
