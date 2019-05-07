package at.mrtrash

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.Toast
import android.view.MenuItem
import android.view.Menu
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import at.mrtrash.vuforia.ObjectTargetRenderer
import at.mrtrash.vuforia.ObjectTargets
import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import at.mrtrash.fragments.WasteTypeFragmentDirections
import at.mrtrash.models.wasteType.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        initViewModels()

        navController = findNavController(R.id.nav_host_fragment)
        // Set up the ActionBar to stay in sync with the NavController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA
                ),
                1
            )
        }
    }

    private fun initViewModels() {
        //TODO: maybe init this here so that data is only loaded once.. Attention with location permissions!
        //ViewModelProviders.of(this, DisposalOptionViewModelFactory(application)).get(DisposalOptionViewModel::class.java)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
