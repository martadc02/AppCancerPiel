package com.example.appcancerpiel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcancerpiel.modelo.Paciente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListadoPacientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pacienteAdapter: PacienteAdapter
    private val db = FirebaseFirestore.getInstance()
    private val listaPacientes = mutableListOf<Paciente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_pacientes)

        // Inicialización del RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPacientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pacienteAdapter = PacienteAdapter(listaPacientes)
        recyclerView.adapter = pacienteAdapter

        // Verificar el rol del usuario y cargar los pacientes correspondientes
        verificarRolYcargarPacientes()
    }

    private fun verificarRolYcargarPacientes() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            Log.i("DEBUG", "UID del usuario autenticado: $uid")

            // Verificar en la colección "Medicos" primero
            db.collection("Medicos").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val rol = document.getString("rol")
                        Log.i("DEBUG", "Rol del usuario autenticado en Medicos: $rol")

                        when (rol) {
                            "administrador" -> {
                                Log.i("DEBUG", "Cargando todos los pacientes (administrador)...")
                                cargarTodosLosPacientes()
                            }

                            "medico" -> {
                                Log.i("DEBUG", "Cargando pacientes asignados (médico)...")
                                cargarPacientesAsignados(uid)
                            }

                            else -> {
                                Log.e("ERROR", "Rol desconocido en Medicos: $rol")
                            }
                        }
                    } else {
                        // Si no existe en "Medicos", verificar en "Administrador"
                        verificarEnAdministrador(uid)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "ERROR",
                        "Error al obtener el documento del usuario en Medicos: ${exception.message}",
                        exception
                    )
                }
        } else {
            Log.e("ERROR", "Usuario no autenticado")
        }
    }

    private fun verificarEnAdministrador(uid: String) {
        db.collection("Administrador").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val rol = document.getString("rol")
                    Log.i("DEBUG", "Rol del usuario autenticado en Administrador: $rol")

                    if (rol == "admin") {
                        Log.i("DEBUG", "Cargando todos los pacientes (Administrador)...")
                        cargarTodosLosPacientes()
                    } else {
                        Log.e("ERROR", "Rol desconocido en Administrador: $rol")
                    }
                } else {
                    Log.e("ERROR", "Documento no encontrado para el UID en Administrador: $uid")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "ERROR",
                    "Error al obtener el documento del usuario en Administrador: ${exception.message}",
                    exception
                )
            }
    }

    private fun cargarTodosLosPacientes() {
        db.collection("Pacientes")
            .get()
            .addOnSuccessListener { result ->
                listaPacientes.clear()
                for (document in result) {
                    val paciente = document.toObject(Paciente::class.java)
                    listaPacientes.add(paciente)
                    Log.i("Paciente", "Cargado: ${paciente.nombre} ${paciente.apellidos}")
                }
                if (listaPacientes.isEmpty()) {
                    Log.w("INFO", "No hay pacientes registrados en la colección.")
                }
                pacienteAdapter.notifyDataSetChanged()
                Log.i("INFO", "Todos los pacientes cargados correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al cargar todos los pacientes: ${exception.message}", exception)
            }
    }

    private fun cargarPacientesAsignados(medicoUid: String) {
        db.collection("Medicos").document(medicoUid)
            .collection("Pacientes")
            .get()
            .addOnSuccessListener { result ->
                listaPacientes.clear()
                for (document in result) {
                    val paciente = document.toObject(Paciente::class.java)
                    listaPacientes.add(paciente)
                    Log.i("Paciente", "Asignado: ${paciente.nombre} ${paciente.apellidos}")
                }
                if (listaPacientes.isEmpty()) {
                    Log.w("INFO", "No hay pacientes asignados al médico con UID: $medicoUid")
                }
                pacienteAdapter.notifyDataSetChanged()
                Log.i("INFO", "Pacientes asignados cargados correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al cargar pacientes asignados: ${exception.message}", exception)
            }
    }

}
