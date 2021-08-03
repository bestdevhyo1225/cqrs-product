package com.hs.application.exception

import com.hs.message.CommandAppExceptionMessage

class ApplicationLayerException(exceptionMessage: CommandAppExceptionMessage) :
    RuntimeException(exceptionMessage.localizedMessage)
