package com.hs.exception

class DomainMongoException(exceptionMessage: DomainMongoExceptionMessage) :
    RuntimeException(exceptionMessage.localizedMessage)
