package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.domain.Asteroid

@Database(entities = [DataBaseAsteroid::class], version = 1 , exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase(){
    abstract val asteroidDao : AsteroidDao

    companion object{
        @Volatile
        private var INSTACNE :AsteroidDatabase? = null

        fun getInstance (context: Context):AsteroidDatabase{

            //to keep threads from init two the DB two times
            synchronized(this){
                var instance = INSTACNE

                if (instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_near_earth_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTACNE = instance
                }

                return instance
            }
        }
    }


}