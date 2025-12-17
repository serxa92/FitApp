package com.example.fitapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    // Variables para el RecyclerView y la lista
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SesionAdapter
    private val listaSesiones = mutableListOf<ActivitySession>()

    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewActividades)
        adapter = SesionAdapter(listaSesiones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configurar UI
        val actividades = listOf("Caminar", "Correr", "Bicicleta")
        val spinner = findViewById<Spinner>(R.id.spinnerActividad)
        val numberPicker = findViewById<NumberPicker>(R.id.numberPickerDuracion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarActividad)
        val btnRealTime = findViewById<Button>(R.id.btnIrASesionTiempoReal)

        val spinnerAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            actividades
        )
        spinner.adapter = spinnerAdapter

        numberPicker.minValue = 1
        numberPicker.maxValue = 180
        numberPicker.value = 30
        numberPicker.wrapSelectorWheel = true

        btnGuardar.setOnClickListener {
            val actividadSeleccionada = spinner.selectedItem?.toString() ?: ""
            val duracion = numberPicker.value

            if (actividadSeleccionada.isNotEmpty()) {
                val fechaHora =
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val nuevaSesion = ActivitySession(actividadSeleccionada, duracion, fechaHora)

                // ✅ Añadir a la lista y actualizar RecyclerView
                listaSesiones.add(nuevaSesion)
                adapter.notifyItemInserted(listaSesiones.size - 1)

                Toast.makeText(this, "Actividad guardada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Selecciona una actividad", Toast.LENGTH_SHORT).show()
            }
        }

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val nuevaSesion =
                        result.data?.getParcelableExtra("nuevaSesion", ActivitySession::class.java)
                    nuevaSesion?.let {
                        listaSesiones.add(it)
                        adapter.notifyItemInserted(listaSesiones.size - 1)
                    }
                }
            }


        btnRealTime.setOnClickListener {
            val actividadSeleccionada = spinner.selectedItem?.toString() ?: ""
            if (actividadSeleccionada.isNotEmpty()) {
                val intent = Intent(this, RealTimeActivity::class.java)
                intent.putExtra("actividad", actividadSeleccionada)
                launcher.launch(intent)
            } else {
                Toast.makeText(this, "Selecciona una actividad primero", Toast.LENGTH_SHORT).show()
            }
        }


    }//onCreate


}
