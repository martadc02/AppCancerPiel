package com.example.appcancerpiel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcancerpiel.modelo.Paciente
import com.google.firebase.firestore.FirebaseFirestore

class ListadoPacientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pacienteAdapter: PacienteAdapter
    private val db = FirebaseFirestore.getInstance()
    private val listaPacientes = mutableListOf<Paciente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_pacientes)

        recyclerView = findViewById(R.id.recyclerViewPacientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pacienteAdapter = PacienteAdapter(listaPacientes)
        recyclerView.adapter = pacienteAdapter

        cargarPacientes()
    }

    private fun cargarPacientes() {
        db.collection("Pacientes")
            .get()
            .addOnSuccessListener { result ->
                listaPacientes.clear()
                for (document in result) {
                    val paciente = document.toObject(Paciente::class.java)
                    listaPacientes.add(paciente)
                }
                pacienteAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al cargar pacientes", exception)
            }
    }
}

