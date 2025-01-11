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

        // Cargar dermatólogos desde Firestore
        cargarDermatologos()

        // Configurar botón para asignar el paciente al dermatólogo seleccionado
        botonAsignar.setOnClickListener {
            asignarPaciente()
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

    private fun asignarPaciente() {
        val indexSeleccionado = spinnerDermatologos.selectedItemPosition
        if (indexSeleccionado == -1) {
            Toast.makeText(this, "Por favor selecciona un dermatólogo", Toast.LENGTH_SHORT).show()
            return
        }

        val uidDermatologoSeleccionado = dermatologosUid[indexSeleccionado]
        val pacienteId = intent.getStringExtra("pacienteId") // Pasado desde la actividad anterior

        if (pacienteId == null) {
            Toast.makeText(this, "Error: No se pudo obtener el ID del paciente.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener los datos del paciente desde Firestore
        db.collection("Pacientes").document(pacienteId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val paciente = document.data
                    // Guardar los datos del paciente en la subcolección del dermatólogo seleccionado
                    db.collection("Medicos").document(uidDermatologoSeleccionado)
                        .collection("Pacientes").document(pacienteId)
                        .set(paciente!!)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Paciente asignado correctamente al dermatólogo.", Toast.LENGTH_SHORT).show()
                            finish() // Cierra la actividad al completar la asignación
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al asignar paciente: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Paciente no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener datos del paciente: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
