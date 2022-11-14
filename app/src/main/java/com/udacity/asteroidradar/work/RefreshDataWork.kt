package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWork(appContext :Context, params : WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    override suspend fun doWork(): Result {
        val dataBase = getInstance(applicationContext)
        val repository = AsteroidsRepository(dataBase.asteroidDao)
        return try {
            repository.refreshAsteroids(AsteroidFilter.SHOW_WEEK)
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }

}