//package com.example.parcialdispositvos.Activity
//
//import android.app.AlertDialog
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.parcialdispositvos.R
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_auth.*
/////import kotlinx.parcelize.Parcelize
//
//
//
//class AuthActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_auth)
//
//        setup()
//
//    }
//
//    private fun setup(){
//
//        btn_Registrar.setOnClickListener {
//            if( (edt_User.length() > 0) && (edt_Password.length() > 0) ) {
//
//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(edt_User.text.toString(),edt_Password.text.toString()).addOnCompleteListener {
//
//                    if(it.isSuccessful){
////                        ShowAlert()
//
//                        val intent = Intent(this,MainActivity::class.java)
//                        startActivity(intent)
//                    }
//                    else
//                    {
//                       ShowAlert()
//
//                    }
//                }
//            }
//        }
//
//
//        btn_Ingresar.setOnClickListener {
//            if( (edt_User.length() > 0) && (edt_Password.length() > 0) ) {
//
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(edt_User.text.toString(),
//                    edt_Password.text.toString()).addOnCompleteListener {
//
//                    if(it.isSuccessful){
//                        val intent = Intent(this, MainActivity::class.java).apply {
//                            putExtra("usuario",edt_User.text.toString())
//                        }
//                        startActivity(intent)
//                    }
//                    else{
//                        ShowAlert()
//                    }
//                }
//            }
//        }
//    }
//
//    fun ShowAlert(){
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Error")
//        builder.setMessage("Se ha producido un error autenticando al usuario")
//        builder.setPositiveButton("Aceptar", null)
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }
//}
//
//
//
package com.example.parcialdispositvos.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parcialdispositvos.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        binding.btnRegistrar.setOnClickListener {
            if (binding.edtUser.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.edtUser.text.toString(),
                    binding.edtPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        binding.btnIngresar.setOnClickListener {
            if (binding.edtUser.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.edtUser.text.toString(),
                    binding.edtPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("usuario", binding.edtUser.text.toString())
                        }
                        startActivity(intent)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}