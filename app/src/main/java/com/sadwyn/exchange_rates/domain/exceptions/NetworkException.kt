package com.sadwyn.exchange_rates.domain.exceptions

class NetworkException(
    message: String,
    throwable: Throwable
) : Throwable(message, throwable)