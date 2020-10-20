package com.stephenmorgandevelopment.celero_challeng_kt.data

import androidx.room.TypeConverter

class DatabaseConverter {
    val delimiter = " "

    @TypeConverter
    fun fromString(stringList: String): List<String> {
        return stringList.split(delimiter)
    }

    @TypeConverter
    fun toStringFromList(list: List<String>): String {
        return list.joinToString(delimiter)
    }
}