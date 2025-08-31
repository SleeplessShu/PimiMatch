package com.sleeplessdog.matchthewords.di

import android.content.Context
import java.io.File


fun provideDatabaseName(context: Context): String {
    val dbFile = File(context.filesDir, "latest_db.db")
    return if (dbFile.exists()) {
        val targetFile = File(context.getDatabasePath("latest_db.db").path)
        if (!targetFile.exists()) {
            dbFile.copyTo(targetFile, overwrite = true)
        }
        "latest_db.db"
    } else {
        "dictionary.db"
    }
}
