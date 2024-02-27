
package com.example.parcialdispositvos.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
            }else {
                showErrorDialog("Por favor, ingrese usuario y contraseña para crear al usuario.")
            }

        }

        binding.btnIngresar.setOnClickListener {
            if (binding.edtUser.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                showProgressBar()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.edtUser.text.toString(),
                    binding.edtPassword.text.toString()
                ).addOnCompleteListener { task ->
                    hideProgressBar()
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("usuario", binding.edtUser.text.toString())
                        }
                        startActivity(intent)
                    } else {
                        showAlert()
                    }
                }
            }else {
                showErrorDialog("Por favor, ingrese usuario y contraseña.")
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
    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}