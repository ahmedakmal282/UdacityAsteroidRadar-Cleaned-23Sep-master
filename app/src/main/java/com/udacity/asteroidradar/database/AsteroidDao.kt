package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.Asteroid
import org.joda.time.TimeOfDay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DataBaseAsteroid)

    @Query("SELECT * FROM asteroid_table ORDER BY id")
    fun getAllAsteroids(): LiveData<List<DataBaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_data=:today ")
    fun getTodayAsteroids(today:String): LiveData<List<DataBaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_data BETWEEN :today AND :weekAfterToday ")
    fun getWeekAsteroids(today:String,weekAfterToday:String): LiveData<List<DataBaseAsteroid>>


    @Insert
    fun insert(asteroid: DataBaseAsteroid)

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC LIMIT 1")
    fun getAsteroid(): DataBaseAsteroid?

    @Query("SELECT * FROM asteroid_table WHERE id= :key")
    fun get(key :Long): DataBaseAsteroid

    @Query("DELETE FROM asteroid_table")
    fun clear()

    @Update
    fun update(asteroid: DataBaseAsteroid)

    @Query("DELETE FROM asteroid_table WHERE id = :key")
    fun deleteAsteroid(key: Long)


}
