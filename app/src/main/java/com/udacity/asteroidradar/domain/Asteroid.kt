package com.udacity.asteroidradar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Asteroid(
     val id: Long= 0,
     val name: String="",
     val closeApproachDate: String="",
     val absoluteMagnitude: Double=0.0,
     val estimatedDiameter: Double=0.0,
     val relativeVelocity: Double=0.0,
     val distanceFromEarth: Double=0.0,
     val isPotentiallyHazardous: Boolean= true) : Parcelable