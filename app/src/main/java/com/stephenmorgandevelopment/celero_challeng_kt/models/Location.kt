package com.stephenmorgandevelopment.celero_challeng_kt.models

import androidx.room.Embedded

data class Location(
    @Embedded val address: Address,
    @Embedded val coordinate: Coordinate
) {
    companion object {
        fun getEmpty() : Location {
            return Location(
                Address.getEmpty(),
                Coordinate.getEmpty()
            )
        }
    }
}

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
) {
    companion object {
        fun getEmpty() : Address {
            return Address(
                "street",
                "city",
                "state",
                "postalCode",
                "country"
            )
        }
    }
}

data class Coordinate(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun getEmpty() : Coordinate {
            return Coordinate(
                0.0,
                0.0
            )
        }
    }
}