package kugong.start.example

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
//        supportActionBar?.hide()
//        startLoading()
    }

//    fun startLoading() {
//        val handler = Handler()
//        handler.postDelayed(Runnable {
//            finish()
//        }, 3000)
//    }
}