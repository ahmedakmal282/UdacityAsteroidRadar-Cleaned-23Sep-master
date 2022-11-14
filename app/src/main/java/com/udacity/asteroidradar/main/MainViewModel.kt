package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.network.AsteroidFilter

import com.udacity.asteroidradar.network.PictureApi
import com.udacity.asteroidradar.network.getToday
import com.udacity.asteroidradar.network.getWeekAfter
import com.udacity.asteroidradar.repository.AsteroidListDatabaseFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.security.auth.login.LoginException


// creating the Status enum
enum class AsteroidApiStatus {LOADING, ERROR }

class MainViewModel(val database:AsteroidDao, application: Application) : AndroidViewModel(application) {


    //Status Checker
    private val _status = MutableLiveData<AsteroidApiStatus>()
    // The external LiveData interface for the API status later while waiting on the connection.
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    //testing..
    private val _response = MutableLiveData<String>()
    // The external LiveData interface for the response.
    val response
        get() = _response


    //To retrieve the Picture of the day
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    //To set the data in the recycleView
    private val _asteroid = MutableLiveData<Asteroid>()
    val asteroid : LiveData<Asteroid>
        get() = _asteroid

    //Navigation
    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails : LiveData<Asteroid?>
        get() = _navigateToAsteroidDetails

    private val asteroidsRepository = AsteroidsRepository(database)


    var _asteroids = asteroidsRepository.getDatabaseAsteroids(getToday())

    val asteroids :LiveData<List<Asteroid>>
        get() = _asteroids



    init {
        /*_asteroids = Transformations.switchMap(asteroidsRepository.getDatabaseAsteroids(getToday()) ) {it}*/
        viewModelScope.launch {
            try{
                asteroidsRepository.refreshAsteroids(AsteroidFilter.SHOW_WEEK)

            }catch (e:Exception){
                _response.value =AsteroidFilter.SHOW_WEEK.value
                Toast.makeText(application,"Could not connect to the server! Please refresh", Toast.LENGTH_LONG).show()
            }
        }
        getPictureOfTheDay()
    }

    private suspend fun changeAsteroidList(date : AsteroidListDatabaseFilter) {

           _asteroids = when (date) {
               AsteroidListDatabaseFilter.TODAY -> {
                   Log.i("DODO", "")
                  asteroidsRepository.getDatabaseAsteroids(getToday())
               }

               AsteroidListDatabaseFilter.WEEK -> asteroidsRepository.getDatabaseAsteroids(
                   getToday(),
                   getWeekAfter()
               ).asFlow().asLiveData()
               else -> {
                   asteroidsRepository.getDatabaseAsteroids()
               }
           }
           Log.i("DODO1", "${_asteroids.value}")

    }
    fun checkValueChange(asteroids :MutableLiveData<List<Asteroid>>, repository: AsteroidsRepository): MutableLiveData<List<Asteroid>> {
        asteroids.value = repository.getDatabaseAsteroids(getToday()).value
        return asteroids

    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                val picHolder = PictureApi.retrofitService2.getPicture()
                _pictureOfTheDay.value = picHolder
            }catch (_:Exception){}
        }
    }


    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidClickedDone(){
        _navigateToAsteroidDetails.value = null
    }
    suspend fun updateDatabaseFilter(date: AsteroidListDatabaseFilter){
        changeAsteroidList(date)
    }

}
