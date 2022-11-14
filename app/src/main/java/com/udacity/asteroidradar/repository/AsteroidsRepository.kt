package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.DataBaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await


enum class AsteroidListDatabaseFilter{ TODAY,WEEK,ALL}


class AsteroidsRepository(private val database: AsteroidDao) {

    //gets all asteroids in database
    fun getDatabaseAsteroids(): LiveData<List<Asteroid>> {
        val asteroidsAll: LiveData<List<Asteroid>> =
            Transformations.map(database.getAllAsteroids()) {
                it.asDomainModel()
            }
        return asteroidsAll
    }
    //get asteroid from database for today
    fun getDatabaseAsteroids(today :String): LiveData<List<Asteroid>> {
        val asteroidsToday: LiveData<List<Asteroid>> =
            Transformations.map(database.getTodayAsteroids(today)){
                it.asDomainModel()
            }
        return asteroidsToday
    }
    //get asteroids from database for this week
    fun getDatabaseAsteroids(today: String , weekAfterToday:String): LiveData<List<Asteroid>> {
        val asteroidsInWeek :LiveData<List<Asteroid>> =
            Transformations.map(database.getWeekAsteroids(today,weekAfterToday)){
                it.asDomainModel()
            }
        return asteroidsInWeek
    }

    //refresh asteroids for the week
    suspend fun refreshAsteroids(filter: AsteroidFilter){
        withContext(Dispatchers.IO){
            Log.i("RefreshRepo", filter.value)
            val asteroidListString = AsteroidApi.retrofitService.getAsteroids(filter.value).await()
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidListString)).map {
                DataBaseAsteroid(
                    id = it.id,
                    name = it.name,
                    closeApproachDate = it.closeApproachDate,
                    absoluteMagnitude = it.absoluteMagnitude,
                    estimatedDiameter = it.estimatedDiameter,
                    relativeVelocity = it.relativeVelocity,
                    distanceFromEarth = it.distanceFromEarth,
                    isPotentiallyHazardous = it.isPotentiallyHazardous
                )
            }.toTypedArray()
           database.insertAll(*asteroidList)
        }
    }

}
