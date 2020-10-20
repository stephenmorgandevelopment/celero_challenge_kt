package com.stephenmorgandevelopment.celero_challeng_kt.models

import androidx.room.Embedded

data class Location(
    @Embedded val address: Address,
    @Embedded val coordinate: Coordinate
)

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)