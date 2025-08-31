package com.sleeplessdog.matchthewords.server.data

import android.content.SharedPreferences
import com.sleeplessdog.matchthewords.server.domain.ServerDateRepository

class ServerDateRepositoryImpl(private var sharedPreferences: SharedPreferences) : ServerDateRepository  {

    override fun getLocalDbDate(): String {
        return sharedPreferences.getString(KEY_DB_DATE, "1970-01-01T00:00:00Z")!!
    }

    override fun saveLocalDbDate(date: String) {
        sharedPreferences.edit().putString(KEY_DB_DATE, date).apply()
    }

    companion object {
        private val KEY_DB_DATE = "local_db_date"
    }
}