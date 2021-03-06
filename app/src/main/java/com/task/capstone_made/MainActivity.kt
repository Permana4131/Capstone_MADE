package com.task.capstone_made

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.task.capstone_made.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.detail_destination -> binding.bottomNavView.visibility = View.GONE
                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp(appBarConfiguration)
        return super.onSupportNavigateUp()
    }
}