package at.mrtrash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Simple placeholder SplashScreen Activity that goes to MainActivity as soon as loading is done
 */
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