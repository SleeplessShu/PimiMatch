package com.sleeplessdog.matchthewords

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.sleeplessdog.matchthewords.di.domainModule
import com.sleeplessdog.matchthewords.di.presentationModule
import com.sleeplessdog.matchthewords.game.data.database.AppDatabase
import com.sleeplessdog.matchthewords.settings.domain.api.SettingsInteractor

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Log.d("DEBUG", "onCreate: ${appContext.getDatabasePath("dictionary_default.db")}")
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, presentationModule)
        }
        deleteExistingDatabase(appContext, "dictionary.db")

        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        databaseSelector()





        val settingsInteractor: SettingsInteractor = getKoin().get()
        val isNightModeOn = settingsInteractor.getThemeSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

    }
    private fun deleteExistingDatabase(context: Context, dbName: String) {
        val dbPath = context.getDatabasePath(dbName)
        if (dbPath.exists()) {
            dbPath.delete()
            Log.d("DEBUG", "Старая база данных удалена.")
        }
    }

    private fun databaseSelector() {
        val dbFile = File(applicationContext.filesDir, "latest_db.db")
        if (dbFile.exists()) {
            val targetFile = File(applicationContext.getDatabasePath("latest_db.db").path)
            if (!targetFile.exists()) {
                dbFile.copyTo(targetFile, overwrite = true)
            }
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "latest_db.db"
            ).build()
        } else {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "dictionary_default.db"
            ).createFromAsset("databases/dictionary_default.db")
                .build()
        }
    }

    companion object {

        lateinit var database: AppDatabase
        lateinit var appContext: Context
            private set
    }
}