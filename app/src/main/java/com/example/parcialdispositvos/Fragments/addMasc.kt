package com.example.parcialdispositvos.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.example.parcialdispositvos.Apis.check_empty
import com.example.parcialdispositvos.Apis.send_message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




import com.example.parcialdispositvos.R

class AddMascFragment : Fragment() {
    lateinit var addMascview : View

    var listMascotas : MutableList<Mascota> = ArrayList()


    val db = Firebase.firestore
    lateinit var edt_nombre : EditText
    lateinit var edt_raza : EditText
    lateinit var edt_edad : EditText


    lateinit var btn_Agregar : Button
    lateinit var btn_Regresar : Button

    var actualUser: String? = null

    companion object{
        fun newInstance() = AddMascFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addMascview = inflater.inflate(R.layout.fragment_add_masc, container, false)

        edt_nombre = addMascview.findViewById(R.id.edt_nombre)
        edt_raza = addMascview.findViewById(R.id.edt_raza)
        edt_edad = addMascview.findViewById(R.id.edt_edad)
        btn_Agregar = addMascview.findViewById(R.id.btn_det_agregar)
        btn_Regresar = addMascview.findViewById(R.id.btn_Regresar)

        return addMascview
    }

    override fun onStart() {
        super.onStart()
        actualUser = addMascArgs.fromBundle(requireArguments()).userName


        btn_Regresar.setOnClickListener()
        {
            val directions = addMascDirections.actionAddMascToListFragment()
            addMascview.findNavController().navigate(directions)
        }

        btn_Agregar.setOnClickListener()
        {
            var flag = check_empty(this.addMascview, "el nombre", edt_nombre)
            if (!flag)
                flag = check_empty(this.addMascview, "la raza", edt_raza)
            if (!flag)
                flag = check_empty(this.addMascview, "la edad", edt_edad)
            if (!flag)
            {
                val newMascota = Mascota(
                    edt_nombre.text.toString() + actualUser ,edt_edad.text.toString().toInt(),edt_nombre.text.toString(),edt_raza.text.toString(),actualUser!!)
                db.collection("prueba").document(newMascota.nombre).set(newMascota)




                send_message(this.addMascview, "Mascota agregada satisfactoriamente")
                val directions = addMascDirections.actionAddMascToListFragment()
                addMascview.findNavController().navigate(directions)
            }
        }



    }


}
