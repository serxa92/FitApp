package com.example.fitapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//Cada vez que el usuario pulse guardar actividad, se creara un objeto de esta clase
//con los datos de la actividad guardados

@Parcelize
data class ActivitySession(
    val nombre: String,
    val duracion: Int,
    val fechaHora: String
) : Parcelable

