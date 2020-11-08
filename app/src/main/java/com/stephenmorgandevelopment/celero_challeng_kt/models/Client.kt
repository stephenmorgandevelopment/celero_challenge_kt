package com.stephenmorgandevelopment.celero_challeng_kt.models

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide

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
) {

    companion object {
        fun getEmpty() : Client {
            return Client(
                0,
                0,
                "Name",
                "(000)000-0000",
                ProfilePicture.getEmpty(),
                Location.getEmpty(),
                "serviceReason",
                listOf("problemPictures")
            )
        }
    }

    fun getParsedCityStatePostalCode() : String {
        return "${this.location.address.city}, ${this.location.address.state}, ${this.location.address.postalCode}"
    }

    fun getStreetAddress() : String  {
        return this.location.address.street
    }

    fun getLatitude() = this.location.coordinate.latitude
    fun getLongitude() = this.location.coordinate.longitude

    val profilePictureAsUri get() = this.profilePicture.large.toUri().buildUpon().scheme("https").build()

    fun getProblemPicturesAsUri() : List<Uri> {
        val uris = ArrayList<Uri>()

        this.problemPictures.forEach {
            uris.add(it.toUri().buildUpon().scheme("https").build())
        }

        return uris
    }

    fun getMapsUriString() : Uri {
        return "google.navigation:q=${getLatitude()},${getLongitude()}".toUri()
    }
}
