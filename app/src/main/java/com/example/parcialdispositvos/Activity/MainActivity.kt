package com.example.parcialdispositvos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.example.parcialdispositvos.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.ktx.storage
class MainActivity : AppCompatActivity() {

    lateinit var  menu_bottom : BottomNavigationView
    lateinit var nav_host : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        val storageReference = Firebase.storage("gs://finalandroid-7013b.appspot.com").reference

       menu_bottom= findViewById(R.id.tb_bottom)
       menu_bottom.visibility= View.GONE

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


    }
}
