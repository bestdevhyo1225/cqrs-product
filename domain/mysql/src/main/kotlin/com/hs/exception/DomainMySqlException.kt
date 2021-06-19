package com.hs.exception

import com.hs.message.CommandAppExceptionMessage

class DomainMySqlException(exceptionMessage: CommandAppExceptionMessage) :
    RuntimeException(exceptionMessage.localizedMessage)
