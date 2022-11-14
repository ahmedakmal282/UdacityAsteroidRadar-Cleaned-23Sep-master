package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
// Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi object.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface PictureOfTheDayAdapter {
        @GET("planetary/apod?api_key=dTAQJHS17CQoa5HRVnh8IGLG8MkbZKn4hngxEGBJ")
        suspend fun getPicture(): PictureOfDay
}

object PictureApi {
    val retrofitService2 : PictureOfTheDayAdapter by lazy { retrofit.create(PictureOfTheDayAdapter::class.java) }
}