package com.ajay.trackerapp.DI

import android.content.Context
import androidx.room.Room
import com.ajay.trackerapp.db.RunDao
import com.ajay.trackerapp.db.RunningDatabase
import com.ajay.trackerapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.ajay.trackerapp.other.Constants.KEY_NAME
import com.ajay.trackerapp.other.Constants.KEY_WEIGHT
import com.ajay.trackerapp.other.Constants.RUNNING_DATABASE_NAME
import com.ajay.trackerapp.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(@ApplicationContext app: Context)
    = Room.databaseBuilder(app, RunningDatabase::class.java, RUNNING_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) : RunDao {
        return db.getRunDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: android.content.SharedPreferences)
    = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: android.content.SharedPreferences)
    = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: android.content.SharedPreferences)
    = sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)


}