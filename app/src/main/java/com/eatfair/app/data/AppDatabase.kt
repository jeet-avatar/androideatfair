package com.eatfair.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eatfair.app.data.dao.AddressDao
import com.eatfair.app.model.address.AddressDto

@Database(entities = [AddressDto::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun addressDao(): AddressDao
}


