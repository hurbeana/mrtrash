package at.mrtrash

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import at.mrtrash.models.Wasteplace
import at.mrtrash.network.DataService
import at.mrtrash.network.WasteplaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

}
