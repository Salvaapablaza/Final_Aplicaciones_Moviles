package com.example.parcialdispositvos.Fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

import com.example.parcialdispositvos.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.example.parcialdispositvos.Holder.MascotaHolder


class listFragment : Fragment() {

    lateinit var v: View

    lateinit var btnAdd : FloatingActionButton

    lateinit var recMascotas : RecyclerView


    val db = Firebase.firestore

    val usuario: String?=null

    private lateinit var adapter: FirestoreRecyclerAdapter<Mascota, MascotaHolder>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.fragment_list, container, false)
        //setHasOptionsMenu(true)
        btnAdd = v.findViewById(R.id.btn_add)

        recMascotas = v.findViewById(R.id.rec_mascotas)
        recMascotas.setHasFixedSize(true)
        recMascotas.layoutManager = LinearLayoutManager(context)


        return v
    }


    override fun onStart() {
        super.onStart()

        val usuario = activity?.intent?.getStringExtra("usuario")
        btnAdd.setOnClickListener {
            var action = listFragmentDirections.actionListFragmentToAddMasc(usuario!!)
            v.findNavController().navigate(action)
        }

        fillRecycler()

    }



    fun fillRecycler(){

        val usuario = activity?.intent?.getStringExtra("usuario")
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("prueba").whereEqualTo("nombredueno",usuario)//

        val options = FirestoreRecyclerOptions.Builder<Mascota>()
            .setQuery(query, Mascota::class.java)
            .build()

        adapter = object :
            FirestoreRecyclerAdapter<Mascota, MascotaHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaHolder{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_mascota, parent, false)
                return MascotaHolder(view)
            }

            override fun onBindViewHolder(holder: MascotaHolder, position: Int, model: Mascota) {
                holder.setName(model.nombre.replaceFirstChar{ it.uppercaseChar() })
                holder.getCardLayout().setOnClickListener {
                    var action = listFragmentDirections.actionListFragmentToDetalleMascota(model.nombre)
                    v.findNavController().navigate(action)
                }
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
        adapter.startListening()
        recMascotas.adapter = adapter
    }

}
