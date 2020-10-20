package com.stephenmorgandevelopment.celero_challeng_kt.models

import androidx.room.ColumnInfo

data class SimpleClient(
    @ColumnInfo(name = "identifier") val identifier: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "serviceReason") val serviceReason: String
)