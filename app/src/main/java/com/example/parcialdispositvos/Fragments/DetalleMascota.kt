package com.example.parcialdispositvos.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.example.parcialdispositvos.Holder.MascotaHolder

import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.example.parcialdispositvos.Apis.send_message

import com.example.parcialdispositvos.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetalleMascota : Fragment() {

    lateinit var MascotaDetalleView : View



    lateinit var img_raza_actual : ImageView

    lateinit var edt_nombre : EditText
    lateinit var edt_raza : EditText
    lateinit var edt_edad : EditText
    lateinit var  menu_bottom : BottomNavigationView
    lateinit var nav_host : NavHostFragment
    var mascotactual = Mascota()

    lateinit var btn_Modificar : Button
    lateinit var btn_Regresar : Button
    var identifier: String? =null
    var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MascotaDetalleView = inflater.inflate(R.layout.fragment_detalle_mascota, container, false)

        edt_nombre = MascotaDetalleView.findViewById(R.id.edt_nombredet)
        edt_raza = MascotaDetalleView.findViewById(R.id.edt_razadet)
        edt_edad = MascotaDetalleView.findViewById(R.id.edt_edaddet)
        btn_Modificar = MascotaDetalleView.findViewById(R.id.btn_det_Modificar)
        btn_Regresar = MascotaDetalleView.findViewById(R.id.btn_cat_Regresar)
        img_raza_actual = MascotaDetalleView.findViewById(R.id.img_CatActual)

      // menu_bottom= MascotaDetalleView.findViewById(R.id.tb_bottom)
        //menu_bottom.visibility= View.VISIBLE
      //  nav_host = .findFragmentById(R.id.mascotasnav) as NavHostFragment
        //NavigationUI.setupWithNavController(menu_bottom, nav_host.navController)


        return MascotaDetalleView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //menu_bottom= MascotaDetalleView.findViewById(R.id.tb_bottom)
        //menu_bottom.visibility= View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        identifier = DetalleMascotaArgs.fromBundle(requireArguments()).identifier
        when(item.itemId) {

            R.id.action_editar -> {
                edt_raza.isEnabled = true
                edt_edad.isEnabled = true
                edt_nombre.isEnabled = true
                btn_Modificar.visibility = View.VISIBLE
            }
            R.id.action_eliminar -> {
                val builder = AlertDialog.Builder(MascotaDetalleView.context)
                builder.setMessage("Esta seguro que desea eliminar la mascota?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { _, _ ->

                      //  mascotaDao?.deleteMascota(mascotaDao?.loadMascotabyid(identifier!!))
                        db.collection("prueba").document(identifier!!).delete()
                        send_message(
                            this.MascotaDetalleView,
                            "Mascota  eliminada"
                        )
                        btn_Regresar.callOnClick()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        identifier = DetalleMascotaArgs.fromBundle(requireArguments()).identifier


        var docRef = db.collection("prueba").document(identifier!!)
        docRef.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot != null) {
                    mascotactual  = dataSnapshot.toObject(Mascota::class.java)!!
                    edt_nombre.setText(mascotactual.nombre)
                    edt_nombre.isEnabled = false
                    edt_raza.setText(mascotactual.raza)
                    edt_raza.isEnabled = false
                    edt_edad.setText(mascotactual.edad.toString())
                    edt_edad.isEnabled = false
                    btn_Modificar.visibility = View.VISIBLE


                    when(mascotactual.raza.uppercase()){
                        "PERRO"-> {
                            img_raza_actual.setImageResource(R.mipmap.mila)

                        }
                        "BOXER"-> {
                            img_raza_actual.setImageResource(R.mipmap.mila)

                        }
                        "LABRADOR"-> {
                            img_raza_actual.setImageResource(R.mipmap.labrador)

                        }
                        "GATO"-> {
                            img_raza_actual.setImageResource(R.mipmap.gato)

                        }
                        "PASTOR"-> {
                            img_raza_actual.setImageResource(R.mipmap.pastor)

                        }
                        else-> img_raza_actual.setImageResource(R.mipmap.otras)
                    }


                    btn_Modificar.setOnClickListener{

                        val newMascota = Mascota(
                            mascotactual.identifier  ,edt_edad.text.toString().toInt(),edt_nombre.text.toString(),edt_raza.text.toString(),mascotactual.nombredueno)
                        db.collection("prueba").document(newMascota.nombre).set(newMascota)


                        send_message(this.MascotaDetalleView, "Mascota modificada")
                        btn_Regresar.callOnClick()
                    }

                    // Log.d("Test", "DocumentSnapshot data: ${mascota.toString()}")
                } else {
                    Log.d("Test", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Test", "get failed with ", exception)
            }





        btn_Regresar.setOnClickListener{
            //setHasOptionsMenu(false)
            val action =DetalleMascotaDirections.actionDetalleMascotaToListFragment()
           MascotaDetalleView.findNavController().navigate(action)
        }




    }


}
