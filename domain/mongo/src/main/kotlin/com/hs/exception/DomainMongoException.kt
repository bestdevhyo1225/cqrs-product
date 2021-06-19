package com.hs.exception

import com.hs.message.QueryAppExceptionMessage

class DomainMongoException(exceptionMessage: QueryAppExceptionMessage) :
    RuntimeException(exceptionMessage.localizedMessage)
