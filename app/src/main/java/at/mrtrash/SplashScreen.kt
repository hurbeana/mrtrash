package at.mrtrash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout)

        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}