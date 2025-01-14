package com.example.appcancerpiel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class VerDermatoscopiaActivity : AppCompatActivity() {

    private lateinit var nombre: String
    private lateinit var apellidos: String
    private lateinit var dni: String

    private lateinit var tvNombre: TextView
    private lateinit var tvApellidos: TextView
    private lateinit var tvDni: TextView
    private lateinit var tvCarcinomaCelEscamosa: TextView
    private lateinit var tvCarcinomaNoPigmentado: TextView
    private lateinit var tvCarcinomaPigmentado: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_dermatoscopia)

        // Obtener los datos del Intent
        nombre = intent.getStringExtra("NOMBRE") ?: ""
        apellidos = intent.getStringExtra("APELLIDOS") ?: ""
        dni = intent.getStringExtra("DNI") ?: ""

        // Inicializar las vistas
        tvNombre = findViewById(R.id.tvNombre)
        tvApellidos = findViewById(R.id.tvApellidos)
        tvDni = findViewById(R.id.tvDni)
        tvCarcinomaCelEscamosa = findViewById(R.id.tvCarcinomaCelEscamosa)
        tvCarcinomaNoPigmentado = findViewById(R.id.tvCarcinomaNoPigmentado)
        tvCarcinomaPigmentado = findViewById(R.id.tvCarcinomaPigmentado)

        // Mostrar los valores recibidos del Intent
        tvNombre.text = nombre
        tvApellidos.text = apellidos
        tvDni.text = dni

        // Obtener los datos de Firebase para este DNI
        obtenerDatosDermatoscopia()
    }

    private fun obtenerDatosDermatoscopia() {
        // Buscar en la colección "Dermatoscopias" utilizando el campo "dni"
        db.collection("Dermatoscopias")
            .whereEqualTo("dni", dni) // Filtramos por el dni
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si encontramos un documento, lo usamos
                    val document = documents.documents[0]

                    val carcinomaCelEscamosa = document.getBoolean("carcinomaCelEscamosa") ?: false
                    val carcinomaNoPigmentado = document.getBoolean("carcinomaNoPigmentado") ?: false
                    val carcinomaPigmentado = document.getBoolean("carcinomaPigmentado") ?: false

                    // Mostrar los valores en los TextViews
                    tvCarcinomaCelEscamosa.text = "Carcinoma Celular Escamoso: $carcinomaCelEscamosa"
                    tvCarcinomaNoPigmentado.text = "Carcinoma No Pigmentado: $carcinomaNoPigmentado"
                    tvCarcinomaPigmentado.text = "Carcinoma Pigmentado: $carcinomaPigmentado"
                } else {
                    // Si no encontramos un documento con ese DNI, podemos mostrar un mensaje
                    tvCarcinomaCelEscamosa.text = "No se encontraron datos."
                    tvCarcinomaNoPigmentado.text = "No se encontraron datos."
                    tvCarcinomaPigmentado.text = "No se encontraron datos."
                }
            }
            .addOnFailureListener { e ->
                // Manejo de errores
                tvCarcinomaCelEscamosa.text = "Error al cargar datos."
                tvCarcinomaNoPigmentado.text = "Error al cargar datos."
                tvCarcinomaPigmentado.text = "Error al cargar datos."
            }
    }
}
