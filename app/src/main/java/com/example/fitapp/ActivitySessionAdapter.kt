package com.example.fitapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SesionAdapter(private val sesiones: List<ActivitySession>) :
    RecyclerView.Adapter<SesionAdapter.SesionViewHolder>() {

    class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icono: ImageView = itemView.findViewById(R.id.ivIcono)
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val duracion: TextView = itemView.findViewById(R.id.tvDuracion)
        val fechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        holder.nombre.text = sesion.nombre
        holder.duracion.text = "${sesion.duracion} minutos"
        holder.fechaHora.text = sesion.fechaHora

        val iconoResId = when (sesion.nombre) {
            "Caminar" -> R.drawable.caminar
            "Correr" -> R.drawable.correr
            "Bicicleta" -> R.drawable.bicicleta
            else -> R.drawable.ic_launcher_foreground
        }
        holder.icono.setImageResource(iconoResId)
    }

    override fun getItemCount(): Int = sesiones.size
}
