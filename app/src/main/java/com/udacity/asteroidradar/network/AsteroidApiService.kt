package com.udacity.asteroidradar.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


// Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi object.
enum class AsteroidFilter(val value: String){SHOW_WEEK(getToday())}

interface AsteroidApiService {
    @GET("neo/rest/v1/feed?api_key=dTAQJHS17CQoa5HRVnh8IGLG8MkbZKn4hngxEGBJ&")
        fun getAsteroids(@Query("start_date") type : String): Call<String>
}

//A public Api object that exposes the lazy-initialized Retrofit service
object AsteroidApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val retrofitService =  retrofit.create(AsteroidApiService::class.java)
}


//to get this day
fun getToday(): String {
    val formatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
    return LocalDate.now().format(formatter)
}

//to week after --> not needed since I found out the API default is a week after the starting day.
fun getWeekAfter(): String {
    val calendar: Calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    calendar.add(Calendar.DAY_OF_YEAR, +7)
    return dateFormat.format(calendar.time)
}
