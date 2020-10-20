package com.stephenmorgandevelopment.celero_challeng_kt.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Client(
    @PrimaryKey val identifier: Long,
    val visitOrder: Long,
    val name: String,
    val phoneNumber: String,
    @Embedded val profilePicture: ProfilePicture,
    @Embedded val location: Location,
    val serviceReason: String,
    val problemPictures: List<String>
)