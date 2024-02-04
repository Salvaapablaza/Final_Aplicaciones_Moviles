package com.example.parcialdispositvos.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.parcialdispositvos.R
import com.example.parcialdispositvos.Entities.Mascotas.Mascota
import com.example.parcialdispositvos.Holder.MascotaHolder


class MascotaListAdapter (private var mascotasList: MutableList<Mascota>,val adapterOnClick : (Int) -> Unit) : RecyclerView.Adapter<MascotaListAdapter.MascotaHolder>() {

    companion object {

        private val TAG = "MascotaListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_mascota,parent,false)
        return (MascotaHolder(
            view
        ))
    }

    override fun getItemCount(): Int {

        return mascotasList.size
    }


    override fun onBindViewHolder(holder: MascotaHolder, position: Int) {

        holder.setName(mascotasList[position].nombre)
        holder.getCardLayout().setOnClickListener {
            adapterOnClick(position)
        }

    }

    class MascotaHolder (v: View) : RecyclerView.ViewHolder(v){

        private var view: View

        init {
            this.view = v
        }

        fun setName(name : String) {
            val txt : TextView = view.findViewById(R.id.txt_name_item)
            txt.text = name
        }

        fun getCardLayout ():CardView{

            return view.findViewById(R.id.card_package_item)
        }

    }

}