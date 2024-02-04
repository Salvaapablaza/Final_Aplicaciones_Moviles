package com.example.parcialdispositvos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.parcialdispositvos.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var  menu_bottom : BottomNavigationView
    lateinit var nav_host : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       menu_bottom= findViewById(R.id.tb_bottom)
       menu_bottom.visibility= View.GONE
       // nav_host = supportFragmentManager.findFragmentById(R.id.mascotasnav) as NavHostFragment
       // NavigationUI.setupWithNavController(menu_bottom, nav_host.navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)






    }
}
