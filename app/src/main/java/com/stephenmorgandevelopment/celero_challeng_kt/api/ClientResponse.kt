package com.stephenmorgandevelopment.celero_challeng_kt.api

import com.stephenmorgandevelopment.celero_challeng_kt.models.Client

data class ClientResponse(
    val clients: List<Client>?,
    val message: String?
)
