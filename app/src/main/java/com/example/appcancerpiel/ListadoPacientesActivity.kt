package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import com.example.appcancerpiel.modelo.Paciente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListadoPacientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var pacienteAdapter: PacienteAdapter
    private val db = FirebaseFirestore.getInstance()
    private val listaPacientes = mutableListOf<Paciente>() // Lista completa
    private val filteredList = mutableListOf<Paciente>() // Lista filtrada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_pacientes)

        // Inicialización del RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPacientes) // Asegúrate de que el ID es correcto
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Listado de Pacientes" // Título de la Toolbar
        }

        // Configurar el adaptador con el callback para el clic
        pacienteAdapter = PacienteAdapter(filteredList) { paciente ->
            // Este código se ejecuta cuando un paciente es clickeado
            val intent = Intent(this, DetallePacienteActivity::class.java)
            intent.putExtra("NOMBRE", paciente.nombre)
            intent.putExtra("APELLIDOS", paciente.apellidos)
            intent.putExtra("EMAIL", paciente.email)
            intent.putExtra("TELEFONO", paciente.telefono)
            intent.putExtra("DNI", paciente.dni)
            intent.putExtra("FECHA_NACIMIENTO", paciente.fechaNacimiento)
            intent.putExtra("SEXO", paciente.sexo)

            startActivity(intent)
        }
        recyclerView.adapter = pacienteAdapter

        // Inicializar campo de búsqueda
        searchField = findViewById(R.id.editTextSearch)
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterPacientes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Verificar el rol del usuario y cargar los pacientes correspondientes
        verificarRolYcargarPacientes()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu) // Inflar el menú si existe
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Botón "Atrás" en la Toolbar
                finish()
                true
            }
            R.id.action_home -> { // Icono de Home en el menú
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterPacientes(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(listaPacientes) // Mostrar todos si el campo está vacío
        } else {
            val lowercaseQuery = query.lowercase()
            filteredList.addAll(
                listaPacientes.filter {
                    it.nombre.lowercase().startsWith(lowercaseQuery)
                }
            )
        }
        pacienteAdapter.notifyDataSetChanged()
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
                filteredList.clear()
                filteredList.addAll(listaPacientes)
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
                filteredList.clear()
                filteredList.addAll(listaPacientes)
                pacienteAdapter.notifyDataSetChanged()
                Log.i("INFO", "Pacientes asignados cargados correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Error al cargar pacientes asignados: ${exception.message}", exception)
            }
    }
}

