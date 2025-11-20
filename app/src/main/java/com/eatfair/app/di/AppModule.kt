package com.eatfair.app.di


import android.content.Context
import androidx.room.Room
import com.eatfair.app.data.AppDatabase
import com.eatfair.shared.data.dao.AddressDao
//import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a Singleton instance of FirebaseAuth.
     * @Singleton ensures only one instance exists across the entire app lifetime.
     */
//    @Provides
//    @Singleton
//    fun provideFirebaseAuth(): FirebaseAuth {
//        return FirebaseAuth.getInstance()
//    }



    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "eatfair_ddb"
        ).build()
    }

    @Provides
    fun provideAddressDao(appDatabase: AppDatabase): AddressDao {
        return appDatabase.addressDao()
    }

    // If you plan to use Firestore for profile details:
    /*
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    */
}
