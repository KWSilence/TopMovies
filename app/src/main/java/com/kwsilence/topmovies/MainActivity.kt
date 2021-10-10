package com.kwsilence.topmovies

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.kwsilence.topmovies.util.InternetChecker

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
//    applicationContext.deleteDatabase("movie_database")
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    setupActionBarWithNavController(navHostFragment.navController)

    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    InternetChecker.setConnectivityManager(cm)
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.nav_host).navigateUp() || super.onNavigateUp()
  }
}
