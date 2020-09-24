package kugong.start.example.SharedPreference

import android.app.Application
import kugong.start.example.MainActivity

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}