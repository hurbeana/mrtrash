package at.mrtrash

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import at.mrtrash.models.displayOption.DisposalOptionFilterViewModel


/**
 * main activity that starts after app has finished with startup
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewModelFilter: DisposalOptionFilterViewModel

    /**
     * Called when activity is started
     *
     * @param savedInstanceState The SavedInstanceState for restoring Instance States
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // setup stuff
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        initViewModels()

        navController = findNavController(R.id.nav_host_fragment)
        // Set up the ActionBar to stay in sync with the NavController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Check permission and request them
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

    /**
     * Init ViewModel that need data loading
     */
    private fun initViewModels() {
        viewModelFilter = ViewModelProviders.of(this).get(DisposalOptionFilterViewModel::class.java)

    }

    /**
     * Setup up navigation
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
