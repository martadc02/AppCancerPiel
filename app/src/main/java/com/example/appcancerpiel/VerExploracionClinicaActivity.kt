package com.example.appcancerpiel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appcancerpiel.R
import com.google.firebase.firestore.FirebaseFirestore

class VerExploracionClinicaActivity : AppCompatActivity() {

    private lateinit var nombre: String
    private lateinit var apellidos: String
    private lateinit var dni: String

    private lateinit var tvNombre: TextView
    private lateinit var tvApellidos: TextView
    private lateinit var tvDni: TextView
    private lateinit var tvLesionCicatriz: TextView
    private lateinit var tvLesionHiperqueratosica: TextView
    private lateinit var tvLesionQueratocica: TextView
    private lateinit var tvLesionesCicatrizales: TextView
    private lateinit var tvLesionesCrecimiento: TextView
    private lateinit var tvPustulaHemorragica: TextView
    private lateinit var tvQuemaduras: TextView
    private lateinit var tvUlcerasCuracion: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_exploracion_clinica)

        // Obtener los datos del Intent
        nombre = intent.getStringExtra("NOMBRE") ?: ""
        apellidos = intent.getStringExtra("APELLIDOS") ?: ""
        dni = intent.getStringExtra("DNI") ?: ""

        // Inicializar las vistas
        tvNombre = findViewById(R.id.tvNombre)
        tvApellidos = findViewById(R.id.tvApellidos)
        tvDni = findViewById(R.id.tvDni)
        tvLesionCicatriz = findViewById(R.id.tvLesionCicatriz)
        tvLesionHiperqueratosica = findViewById(R.id.tvLesionHiperqueratosica)
        tvLesionQueratocica = findViewById(R.id.tvLesionQueratocica)
        tvLesionesCicatrizales = findViewById(R.id.tvLesionesCicatrizales)
        tvLesionesCrecimiento = findViewById(R.id.tvLesionesCrecimiento)
        tvPustulaHemorragica = findViewById(R.id.tvPustulaHemorragica)
        tvQuemaduras = findViewById(R.id.tvQuemaduras)
        tvUlcerasCuracion = findViewById(R.id.tvUlcerasCuracion)

        // Mostrar los valores recibidos del Intent
        tvNombre.text = nombre
        tvApellidos.text = apellidos
        tvDni.text = dni

        // Obtener los datos de Firebase para este DNI
        obtenerDatosExploracionClinica()
    }

    private fun obtenerDatosExploracionClinica() {
        // Buscar en la colección "ExploracionesClinicas" utilizando el campo "dni"
        db.collection("ExploracionesClinicas")
            .whereEqualTo("dni", dni) // Filtramos por el dni
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si encontramos un documento, lo usamos
                    val document = documents.documents[0]

                    val lesionCicatriz = document.getBoolean("lesionCicatriz") ?: false
                    val lesionHiperqueratosica = document.getBoolean("lesionHiperqueratosica") ?: false
                    val lesionQueratocica = document.getBoolean("lesionQueratocica") ?: false
                    val lesionesCicatrizales = document.getBoolean("lesionesCicatrizales") ?: false
                    val lesionesCrecimiento = document.getBoolean("lesionesCrecimiento") ?: false
                    val pustulaHemorragica = document.getBoolean("pustulaHemorragica") ?: false
                    val quemaduras = document.getBoolean("quemaduras") ?: false
                    val ulcerasCuracion = document.getBoolean("ulceraCuracion") ?: false

                    // Mostrar los valores en los TextViews
                    tvLesionCicatriz.text = "Lesión Cicatriz: $lesionCicatriz"
                    tvLesionHiperqueratosica.text = "Lesión Hiperqueratósica: $lesionHiperqueratosica"
                    tvLesionQueratocica.text = "Lesión Queratósica: $lesionQueratocica"
                    tvLesionesCicatrizales.text = "Lesiones Cicatrizales: $lesionesCicatrizales"
                    tvLesionesCrecimiento.text = "Lesiones de Crecimiento: $lesionesCrecimiento"
                    tvPustulaHemorragica.text = "Pústula Hemorrágica: $pustulaHemorragica"
                    tvQuemaduras.text = "Quemaduras: $quemaduras"
                    tvUlcerasCuracion.text = "Úlceras en Curación: $ulcerasCuracion"
                } else {
                    // Si no encontramos un documento con ese DNI, podemos mostrar un mensaje
                    tvLesionCicatriz.text = "No se encontraron datos."
                    tvLesionHiperqueratosica.text = "No se encontraron datos."
                    tvLesionQueratocica.text = "No se encontraron datos."
                    tvLesionesCicatrizales.text = "No se encontraron datos."
                    tvLesionesCrecimiento.text = "No se encontraron datos."
                    tvPustulaHemorragica.text = "No se encontraron datos."
                    tvQuemaduras.text = "No se encontraron datos."
                    tvUlcerasCuracion.text = "No se encontraron datos."
                }
            }
            .addOnFailureListener { e ->
                // Manejo de errores
                tvLesionCicatriz.text = "Error al cargar datos."
                tvLesionHiperqueratosica.text = "Error al cargar datos."
                tvLesionQueratocica.text = "Error al cargar datos."
                tvLesionesCicatrizales.text = "Error al cargar datos."
                tvLesionesCrecimiento.text = "Error al cargar datos."
                tvPustulaHemorragica.text = "Error al cargar datos."
                tvQuemaduras.text = "Error al cargar datos."
                tvUlcerasCuracion.text = "Error al cargar datos."
            }
    }
}
