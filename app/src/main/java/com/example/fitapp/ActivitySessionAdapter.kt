package com.example.fitapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Clase para representar una sesión de actividad física

class SesionAdapter(private val sesiones: List<ActivitySession>) :
    RecyclerView.Adapter<SesionAdapter.SesionViewHolder>() {

    // ViewHolder para cada elemento de la lista
    class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icono: ImageView = itemView.findViewById(R.id.ivIcono)
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val duracion: TextView = itemView.findViewById(R.id.tvDuracion)
        val fechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
    }

    //Creamos el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    //Vinculamos los datos de cada sesión con el ViewHolder, para mostrarlos en la lista
    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        holder.nombre.text = sesion.nombre
        holder.duracion.text = "${sesion.duracion} minutos"
        holder.fechaHora.text = sesion.fechaHora

        // Establecemos el icono correspondiente a la actividad según su nombre
        val iconoResId = when (sesion.nombre) {
            "Caminar" -> R.drawable.caminar
            "Correr" -> R.drawable.correr
            "Bicicleta" -> R.drawable.bicicleta
            else -> R.drawable.ic_launcher_foreground
        }
        holder.icono.setImageResource(iconoResId)
    }

    //Devolvemos el número de elementos en la lista
    override fun getItemCount(): Int = sesiones.size
}
