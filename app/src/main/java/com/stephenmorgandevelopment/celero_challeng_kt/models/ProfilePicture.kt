package com.stephenmorgandevelopment.celero_challeng_kt.models

data class ProfilePicture(
    val large: String,
    val medium: String,
    val thumbnail: String
) {
    companion object {
        fun getEmpty() : ProfilePicture {
            return ProfilePicture(
                "large",
                "medium",
                "thumbnail"
            )
        }
    }
}