package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

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

        // Obtener datos del Intent (ID, nombre y apellido del paciente)
        val pacienteId = intent.getStringExtra("PACIENTE_ID") ?: ""
        val nombrePaciente = intent.getStringExtra("NOMBRE") ?: "Nombre no disponible"
        val apellidoPaciente = intent.getStringExtra("APELLIDOS") ?: "Apellidos no disponibles"

        if (pacienteId.isEmpty()) {
            Toast.makeText(this, "Error: ID del paciente no encontrado.", Toast.LENGTH_SHORT).show()
            finish() // Finaliza la actividad si no se encuentra la información del paciente
            return
        }

        // Configurar acciones de los botones
        btnCuidadosPaliativos.setOnClickListener {
            actualizarEstadoPaciente(pacienteId, "Cuidados Paliativos")
        }

        btnTratamientoMedico.setOnClickListener {
            actualizarEstadoPaciente(pacienteId, "Tratamiento Médico")
        }

        btnCirugia.setOnClickListener {
            actualizarEstadoPaciente(pacienteId, "Cirugía")
        }
    }

    private fun actualizarEstadoPaciente(pacienteId: String, estado: String) {
        // Actualizar el estado del paciente en la base de datos
        db.collection("Pacientes").document(pacienteId)
            .update("estado", estado, "fecha", System.currentTimeMillis())
            .addOnSuccessListener {
                Toast.makeText(this, "Estado actualizado a $estado.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar el estado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
