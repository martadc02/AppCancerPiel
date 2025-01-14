package com.example.appcancerpiel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appcancerpiel.R
import com.google.firebase.firestore.FirebaseFirestore

class VerHistoriaClinicaActivity : AppCompatActivity() {

    private lateinit var nombre: String
    private lateinit var apellidos: String
    private lateinit var dni: String

    private lateinit var tvNombre: TextView
    private lateinit var tvApellidos: TextView
    private lateinit var tvDni: TextView
    private lateinit var tvColorOjos: TextView
    private lateinit var tvColorPelo: TextView
    private lateinit var tvColorPiel: TextView
    private lateinit var tvConsumoAlcohol: TextView
    private lateinit var tvConsumoTabaco: TextView
    private lateinit var tvExposicionSolar: TextView
    private lateinit var tvRadiacionesIonizantes: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_historia_clinica)

        // Obtener los datos del Intent
        nombre = intent.getStringExtra("NOMBRE") ?: ""
        apellidos = intent.getStringExtra("APELLIDOS") ?: ""
        dni = intent.getStringExtra("DNI") ?: ""

        // Inicializar las vistas
        tvNombre = findViewById(R.id.tvNombre)
        tvApellidos = findViewById(R.id.tvApellidos)
        tvDni = findViewById(R.id.tvDni)
        tvColorOjos = findViewById(R.id.tvColorOjos)
        tvColorPelo = findViewById(R.id.tvColorPelo)
        tvColorPiel = findViewById(R.id.tvColorPiel)
        tvConsumoAlcohol = findViewById(R.id.tvConsumoAlcohol)
        tvConsumoTabaco = findViewById(R.id.tvConsumoTabaco)
        tvExposicionSolar = findViewById(R.id.tvExposicionSolar)
        tvRadiacionesIonizantes = findViewById(R.id.tvRadiacionesIonizantes)

        // Mostrar los valores recibidos del Intent
        tvNombre.text = nombre
        tvApellidos.text = apellidos
        tvDni.text = dni

        // Obtener los datos de Firebase para este DNI
        obtenerDatosHistoriaClinica()
    }

    private fun obtenerDatosHistoriaClinica() {
        // Buscar en la colección "HistoriasClinicas" utilizando el campo "dni"
        db.collection("HistoriasClinicas")
            .whereEqualTo("dni", dni) // Filtramos por el dni
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si encontramos un documento, lo usamos
                    val document = documents.documents[0]

                    val colorOjos = document.getString("colorOjos") ?: "No disponible"
                    val colorPelo = document.getString("colorPelo") ?: "No disponible"
                    val colorPiel = document.getString("colorPiel") ?: "No disponible"
                    val consumoAlcohol = document.getBoolean("consumoAlcohol") ?: false
                    val consumoTabaco = document.getBoolean("consumoTabaco") ?: false
                    val exposicionSolar = document.getBoolean("exposicionSolar") ?: false
                    val radiacionesIonizantes = document.getBoolean("radiacionesIonizantes") ?: false

                    // Mostrar los valores en los TextViews
                    tvColorOjos.text = "Color de ojos: $colorOjos"
                    tvColorPelo.text = "Color de pelo: $colorPelo"
                    tvColorPiel.text = "Color de piel: $colorPiel"
                    tvConsumoAlcohol.text = "Consumo de alcohol: $consumoAlcohol"
                    tvConsumoTabaco.text = "Consumo de tabaco: $consumoTabaco"
                    tvExposicionSolar.text = "Exposición solar: $exposicionSolar"
                    tvRadiacionesIonizantes.text = "Radiaciones ionizantes: $radiacionesIonizantes"
                } else {
                    // Si no encontramos un documento con ese DNI, mostramos un mensaje
                    tvColorOjos.text = "No se encontraron datos."
                    tvColorPelo.text = "No se encontraron datos."
                    tvColorPiel.text = "No se encontraron datos."
                    tvConsumoAlcohol.text = "No se encontraron datos."
                    tvConsumoTabaco.text = "No se encontraron datos."
                    tvExposicionSolar.text = "No se encontraron datos."
                    tvRadiacionesIonizantes.text = "No se encontraron datos."
                }
            }
            .addOnFailureListener { e ->
                // Manejo de errores
                tvColorOjos.text = "Error al cargar datos."
                tvColorPelo.text = "Error al cargar datos."
                tvColorPiel.text = "Error al cargar datos."
                tvConsumoAlcohol.text = "Error al cargar datos."
                tvConsumoTabaco.text = "Error al cargar datos."
                tvExposicionSolar.text = "Error al cargar datos."
                tvRadiacionesIonizantes.text = "Error al cargar datos."
            }
    }
}
