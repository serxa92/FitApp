package com.example.fitapp
//IMPORTS
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //Creamos el metodo onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        //Llamamos al metodo onCreate de la clase padre
        super.onCreate(savedInstanceState)
        //Aqui le decimos que layout queremos usar
        setContentView(R.layout.activity_main)

        //VARIABLES

        //Actividades con las diferentes opciones
        val actividades = listOf("Caminar", "Correr", "Bicicleta")
        //Spinner para el selector de actividades
        val spinner = findViewById<Spinner>(R.id.spinnerActividad)
        //numberPicker para la duracion
        val numberPicker = findViewById<NumberPicker>(R.id.numberPickerDuracion)
        //btnGuardar para el boton
        val btnGuardar = findViewById<Button>(R.id.btnGuardarActividad)

        //Configuramos el adaptador para el Spinner, this para el contexto de la actividad, el layout
        //y el objeto sobre el que queremos trabajar
        val adapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            actividades
        )

        // Configuramos el adaptador para el Spinner
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Configuración del NumberPicker
        numberPicker.minValue = 1
        numberPicker.maxValue = 180
        numberPicker.value = 30
        numberPicker.wrapSelectorWheel = true

        //Listener para guardar la actividad
        btnGuardar.setOnClickListener {
            // Obtenemos la actividad seleccionada y la duracion
            val actividadSeleccionada = spinner.selectedItem?.toString() ?: ""
            val duracion = numberPicker.value
            //Si la actividad no esta vacia, guardamos la actividad
            if (actividadSeleccionada.isNotEmpty()) {
                val fechaHora =
                    java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date())
                ActivitySession(actividadSeleccionada, duracion, fechaHora)

                // Mostramos un mensaje de confirmación
                Toast.makeText(this, "Actividad guardada", Toast.LENGTH_SHORT).show()


            } else {
                // Mostramos un mensaje de error
                Toast.makeText(this, "Selecciona una actividad", Toast.LENGTH_SHORT).show()
            }
        }

    }//Oncreate

    //Creamos la clase para guardar las sesiones
    data class ActivitySession(
        val nombre: String,
        val duracion: Int,
        val fechaHora: String
    )


}//class


