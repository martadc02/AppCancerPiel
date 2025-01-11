package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ValoracionResultadosActivity : AppCompatActivity() {

    private lateinit var btnCuidadosPaliativos: Button
    private lateinit var btnTratamientoMedico: Button
    private lateinit var btnCirugia: Button

    private val db = FirebaseFirestore.getInstance() // Instancia de Firebase Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valoracion_resultados)

        // Inicialización de botones
        btnCuidadosPaliativos = findViewById(R.id.btnCuidadosPaliativos)
        btnTratamientoMedico = findViewById(R.id.btnTratamientoMedico)
        btnCirugia = findViewById(R.id.btnCirugia)

        // Obtener datos del Intent (nombre y apellido del paciente)
        val nombrePaciente = intent.getStringExtra("NOMBRE_PACIENTE")
        val apellidoPaciente = intent.getStringExtra("APELLIDO_PACIENTE")

        if (nombrePaciente.isNullOrEmpty() || apellidoPaciente.isNullOrEmpty()) {
            Toast.makeText(this, "Error: Nombre y apellido del paciente no encontrados.", Toast.LENGTH_SHORT).show()
            finish() // Finaliza la actividad si no se encuentra la información del paciente
            return
        }

        // Configurar acciones de los botones
        btnCuidadosPaliativos.setOnClickListener {
            guardarCuidadosPaliativos(nombrePaciente, apellidoPaciente)
        }

        btnTratamientoMedico.setOnClickListener {
            Toast.makeText(this, "Seleccionado: Tratamiento Médico", Toast.LENGTH_SHORT).show()
        }

        btnCirugia.setOnClickListener {
            Toast.makeText(this, "Seleccionado: Cirugía", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarCuidadosPaliativos(nombre: String, apellido: String) {
        // Buscar el paciente por nombre y apellido
        db.collection("Pacientes")
            .whereEqualTo("nombre", nombre)
            .whereEqualTo("apellido", apellido)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Paciente no encontrado.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // Actualizar el primer documento encontrado (debe haber un único documento por nombre y apellido)
                for (document in documents) {
                    db.collection("Pacientes").document(document.id)
                        .update("estado", "Cuidados Paliativos", "fecha", System.currentTimeMillis())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Estado actualizado a Cuidados Paliativos.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al buscar paciente: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
