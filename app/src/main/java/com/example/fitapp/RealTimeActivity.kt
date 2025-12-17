package com.example.fitapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealTimeActivity : AppCompatActivity() {

    // Sensores y ubicación
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null
    private lateinit var acelerometroListener: SensorEventListener

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    // UI
    private lateinit var movimientoTextView: TextView
    private lateinit var latitudTextView: TextView
    private lateinit var longitudTextView: TextView
    private var isFirstLocation = true

    // Lista de sesiones y adaptador
    private lateinit var sesiones: MutableList<ActivitySession>
    private lateinit var sesionAdapter: SesionAdapter

    // Launcher para recibir actividad en tiempo real
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val nuevaSesion = result.data?.getParcelableExtra<ActivitySession>("nuevaSesion")
            nuevaSesion?.let {
                sesiones.add(it)
                sesionAdapter.notifyItemInserted(sesiones.size - 1)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_time)

        // Recuperar elementos UI
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizarSesion)
        val iconoImageView = findViewById<ImageView>(R.id.ivIconoActividad)
        movimientoTextView = findViewById(R.id.tvMovimientoEstado)
        latitudTextView = findViewById(R.id.tvLatitud)
        longitudTextView = findViewById(R.id.tvLongitud)

        // Recuperar la actividad pasada por intent
        val actividad = intent.getStringExtra("actividad")

        // Mostrar icono según la actividad
        val iconoResId = when (actividad) {
            "Caminar" -> R.drawable.caminar
            "Correr" -> R.drawable.correr
            "Bicicleta" -> R.drawable.bicicleta
            else -> null
        }
        iconoResId?.let {
            iconoImageView.setImageResource(it)
        }

        val cronometro = findViewById<Chronometer>(R.id.chronometer)
        // Guarda el tiempo inicial
        cronometro.base = SystemClock.elapsedRealtime()
        // Listener para actualizar el formato del cronómetro
        cronometro.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - cronometro.base
            val segundosTotales = elapsedMillis / 1000
            val segundos = (elapsedMillis / 1000) % 60
            val minutos = (elapsedMillis / 1000) / 60
            val horas = segundosTotales / 3600

            cronometro.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
        }

        // Iniciar el cronómetro
        cronometro.start()


        // Botón para finalizar sesión
        btnFinalizar.setOnClickListener {
            val fechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val duracionMs = SystemClock.elapsedRealtime() - cronometro.base
            val duracionMin = (duracionMs / 60000).toInt()
            val nuevaSesion = ActivitySession(
                nombre = actividad ?: "Desconocida",
                duracion = duracionMin,
                fechaHora = fechaHora
            )
            val intent = Intent()
            intent.putExtra("nuevaSesion", nuevaSesion)
            setResult(RESULT_OK, intent)
            finish()
        }

        // Inicializar sensores
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        acelerometroListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    val fuerzaMovimiento = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val GRAVEDAD = SensorManager.GRAVITY_EARTH
                    val diferencia = kotlin.math.abs(fuerzaMovimiento - GRAVEDAD)

                    val texto = when {
                        diferencia < 0.5 -> "Sin movimiento"
                        diferencia < 3 -> "Movimiento suave"
                        else -> "Movimiento sexy"
                    }
                    movimientoTextView.text = texto
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        acelerometro?.let {
            sensorManager.registerListener(
                acelerometroListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        // Inicializar GPS
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            startLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val lat = location.latitude
                val lon = location.longitude

                latitudTextView.text = lat.toString()
                longitudTextView.text = lon.toString()

                if (isFirstLocation) {
                    Toast.makeText(
                        this@RealTimeActivity,
                        "Tracking iniciado: GPS activo",
                        Toast.LENGTH_SHORT
                    ).show()
                    isFirstLocation = false
                }
            }
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000L,
            1f,
            locationListener
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(acelerometroListener)
        if (::locationListener.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }
}

