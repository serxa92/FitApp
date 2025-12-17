package com.example.fitapp

//Imports
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

    //Creamos la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuramos RecyclerView
        recyclerView = findViewById(R.id.recyclerViewActividades)
        adapter = SesionAdapter(listaSesiones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configuramos UI y listeners
        val actividades = listOf("Caminar", "Correr", "Bicicleta")
        val spinner = findViewById<Spinner>(R.id.spinnerActividad)
        val numberPicker = findViewById<NumberPicker>(R.id.numberPickerDuracion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarActividad)
        val btnRealTime = findViewById<Button>(R.id.btnIrASesionTiempoReal)
        // Configuramos el adaptador del Spinner
        val spinnerAdapter = ArrayAdapter(
            this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, actividades
        )
        spinner.adapter = spinnerAdapter
        // Configuramos el valor mínimo y máximo del numberPicker
        numberPicker.minValue = 1
        numberPicker.maxValue = 180
        numberPicker.value = 30
        numberPicker.wrapSelectorWheel = true

        //Listener para el botón de guardar
        btnGuardar.setOnClickListener {
            //Si el spinner no está vacío, creamos una nueva sesión
            val actividadSeleccionada = spinner.selectedItem?.toString() ?: ""
            //La duración la cogemos del numberPicker
            val duracion = numberPicker.value
            //Si la actividad no está vacía, creamos una nueva sesión
            if (actividadSeleccionada.isNotEmpty()) {
                //Pasamos la fecha y hora actual
                val fechaHora =
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                //Creamos la nueva sesión
                val nuevaSesion = ActivitySession(actividadSeleccionada, duracion, fechaHora)

                // Añadimos la nueva sesión a la lista y actualizamos el RecyclerView
                listaSesiones.add(nuevaSesion)
                // Notificamos al adaptador que se ha insertado un nuevo elemento,
                adapter.notifyItemInserted(listaSesiones.size - 1)
                //Lanzamos un toast para notificar que la actividad se ha guardado
                Toast.makeText(this, "Actividad guardada", Toast.LENGTH_SHORT).show()
            } else {
                //Si no está seleccionada ninguna actividad, mostramos un toast para notificarlo
                Toast.makeText(this, "Selecciona una actividad", Toast.LENGTH_SHORT).show()
            }
        }

        //El launcher se encarga de recibir la actividad en tiempo real y añadirla a la lista
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

        //Lanzamos la actividad en tiempo real al pulsar el botón
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
