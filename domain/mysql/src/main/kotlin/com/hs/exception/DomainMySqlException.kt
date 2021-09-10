package com.hs.exception

class DomainMySqlException(exceptionMessage: DomainMysqlExceptionMessage) :
    RuntimeException(exceptionMessage.localizedMessage)
