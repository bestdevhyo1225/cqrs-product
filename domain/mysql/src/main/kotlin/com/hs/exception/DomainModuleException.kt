package com.hs.exception

class DomainModuleException(exceptionMessage: ExceptionMessage) : RuntimeException(exceptionMessage.str)
