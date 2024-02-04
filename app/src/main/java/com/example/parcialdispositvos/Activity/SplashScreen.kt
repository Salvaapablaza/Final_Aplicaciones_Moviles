package com.example.parcialdispositvos.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.parcialdispositvos.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT : Long = 3000 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val parentJob = Job()

        val scope = CoroutineScope(Dispatchers.Main + parentJob)

        scope.launch {
            delay(SPLASH_TIME_OUT)
            launchApp()

        }
    }
    private fun launchApp(){

        val intent = Intent(this,AuthActivity::class.java)
        startActivity(intent)

    }

}
