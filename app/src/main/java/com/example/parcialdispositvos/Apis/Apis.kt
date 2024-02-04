package com.example.parcialdispositvos.Apis

import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

fun check_empty(layout : View, variable : String, txt_input : EditText) : Boolean{
    if (txt_input.text.toString().isEmpty()){
        Snackbar.make(layout, "Falta ingresar $variable", Snackbar.LENGTH_SHORT).show()
        return true
    }
    else
        return false
}

fun send_message(layout : View, mensaje : String)
{
    Snackbar.make(layout, mensaje, Snackbar.LENGTH_SHORT).show()
}