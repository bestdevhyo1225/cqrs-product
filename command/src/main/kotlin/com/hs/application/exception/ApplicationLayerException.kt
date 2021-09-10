package com.hs.application.exception

class ApplicationLayerException(commandAppExceptionMessage: CommandAppExceptionMessage) :
    RuntimeException(commandAppExceptionMessage.localizedMessage)
