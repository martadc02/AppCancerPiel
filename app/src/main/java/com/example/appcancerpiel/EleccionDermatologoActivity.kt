package com.example.appcancerpiel

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EleccionDermatologoActivity : AppCompatActivity() {

    private lateinit var spinnerDermatologos: Spinner
    private lateinit var botonAsignar: Button

    private val db = FirebaseFirestore.getInstance()
    private val listaDermatologos = mutableListOf<String>() // Lista para nombres de dermatólogos
    private val dermatologosUid = mutableListOf<String>()   // Lista para UID de dermatólogos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleccion_dermatologo)

        // Inicializar vistas
        spinnerDermatologos = findViewById(R.id.spinnerDermatologos)
        botonAsignar = findViewById(R.id.botonAsignarDermatologo)

        // Obtener datos del Intent
        val nombrePaciente = intent.getStringExtra("nombrePaciente")
        val apellidosPaciente = intent.getStringExtra("apellidosPaciente")

        if (nombrePaciente == null || apellidosPaciente == null) {
            Toast.makeText(this, "Error: Datos del paciente no disponibles.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Mostrar dermatólogos en el Spinner
        cargarDermatologos()

        // Configurar botón para asignar el paciente al dermatólogo seleccionado
        botonAsignar.setOnClickListener {
            asignarPaciente(nombrePaciente, apellidosPaciente)
        }
    }

    private fun cargarDermatologos() {
        db.collection("Medicos").whereEqualTo("tipo", "Dermatólogo")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombreCompleto = document.getString("nombre") + " " + document.getString("apellidos")
                    val uid = document.id
                    listaDermatologos.add(nombreCompleto!!)
                    dermatologosUid.add(uid)
                }

                // Configurar el Spinner con los nombres de los dermatólogos
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaDermatologos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDermatologos.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar dermatólogos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun asignarPaciente(nombrePaciente: String, apellidosPaciente: String) {
        val indexSeleccionado = spinnerDermatologos.selectedItemPosition
        if (indexSeleccionado == -1) {
            Toast.makeText(this, "Por favor selecciona un dermatólogo", Toast.LENGTH_SHORT).show()
            return
        }

        val uidDermatologoSeleccionado = dermatologosUid[indexSeleccionado]

        // Buscar al paciente en Firestore por nombre y apellidos
        db.collection("Pacientes")
            .whereEqualTo("nombre", nombrePaciente)
            .whereEqualTo("apellidos", apellidosPaciente)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val pacienteDocument = result.documents[0]
                    val pacienteId = pacienteDocument.id
                    val pacienteData = pacienteDocument.data

                    // Asignar paciente al dermatólogo seleccionado
                    db.collection("Medicos").document(uidDermatologoSeleccionado)
                        .collection("Pacientes").document(pacienteId)
                        .set(pacienteData!!)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Paciente asignado correctamente al dermatólogo.", Toast.LENGTH_SHORT).show()
                            finish() // Cierra la actividad al completar la asignación
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al asignar paciente: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Paciente no encontrado en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al buscar paciente: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
