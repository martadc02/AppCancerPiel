package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class RegistroPacienteActivity : AppCompatActivity() {

    private lateinit var btnRegistro: Button
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var telefono: EditText
    private lateinit var letreroSex: TextView
    private lateinit var email: EditText
    private lateinit var fechaNac: EditText
    private lateinit var nombre: EditText
    private lateinit var apellidos: EditText
    private lateinit var dni: EditText
    private lateinit var gruposexo: RadioGroup
    private lateinit var spinnerMedicos: Spinner

    private lateinit var listaMedicos: MutableList<String> // Almacenará los UID de los médicos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Registrar Nuevo Paciente" // Título de la Toolbar
        }

        // Inicialización de los componentes
        btnRegistro = findViewById(R.id.botonRegistro)
        auth = FirebaseAuth.getInstance()

        telefono = findViewById(R.id.editTextPhone)
        letreroSex = findViewById(R.id.textViewSex)
        email = findViewById(R.id.correoinfopersonal)
        nombre = findViewById(R.id.nombreinfopersonal)
        apellidos = findViewById(R.id.apellidosinfopersonal)
        fechaNac = findViewById(R.id.fechanacimientoinfopersonal)
        dni = findViewById(R.id.editdni)
        gruposexo = findViewById(R.id.radioGroupsexo)
        spinnerMedicos = findViewById(R.id.spinnerMedicos)

        listaMedicos = mutableListOf()

        cargarMedicosDisponibles()
        setup()
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

    private fun cargarMedicosDisponibles() {
        db.collection("Medicos")
            .get()
            .addOnSuccessListener { result ->
                val nombresMedicos = mutableListOf<String>()
                for (document in result) {
                    val nombreMedico = document.getString("nombre") + " " + document.getString("apellidos")
                    val uidMedico = document.id
                    listaMedicos.add(uidMedico)
                    nombresMedicos.add(nombreMedico!!)
                }
                // Configura el Spinner con los nombres de los médicos
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresMedicos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMedicos.adapter = adapter
            }
            .addOnFailureListener { exception ->
                showAlert("Error al cargar médicos: ${exception.message}")
                Log.e("ERROR", "Error al cargar médicos", exception)
            }
    }

    private fun setup() {
        btnRegistro.setOnClickListener {
            if (validarCampos()) {
                registrarPaciente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        if (email.text.isBlank() || nombre.text.isBlank() || apellidos.text.isBlank() ||
            fechaNac.text.isBlank() || telefono.text.isBlank() || dni.text.isBlank()
        ) {
            showAlert("Por favor, complete todos los campos")
            return false
        }

        // Validar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            showAlert("Por favor, ingrese un correo electrónico válido")
            return false
        }

        // Validar formato de DNI
        if (!dni.text.toString().matches(Regex("\\d{8}[A-HJ-NP-TV-Z]"))) {
            showAlert("Por favor, ingrese un DNI válido")
            return false
        }

        return true
    }

    private fun registrarPaciente() {
        val medicoSeleccionadoIndex = spinnerMedicos.selectedItemPosition
        if (medicoSeleccionadoIndex == -1) {
            showAlert("Por favor, seleccione un médico para asignar al paciente.")
            return
        }

        val medicoUid = listaMedicos[medicoSeleccionadoIndex]
        val paciente = hashMapOf(
            "nombre" to nombre.text.toString(),
            "apellidos" to apellidos.text.toString(),
            "fechaNacimiento" to fechaNac.text.toString(),
            "telefono" to telefono.text.toString(),
            "dni" to dni.text.toString(),
            "sexo" to when (gruposexo.checkedRadioButtonId) {
                R.id.radioButtonFemale -> "Femenino"
                R.id.radioButtonMale -> "Masculino"
                else -> "No especificado"
            },
            "email" to email.text.toString(),
            "medicoUid" to medicoUid // Referencia al médico asignado
        )

        // Guardar el paciente en la colección global "Pacientes"
        db.collection("Pacientes")
            .add(paciente)
            .addOnSuccessListener { documentRef ->
                val pacienteId = documentRef.id
                // Guardar el paciente en la subcolección "Pacientes" del médico asignado
                db.collection("Medicos").document(medicoUid)
                    .collection("Pacientes").document(pacienteId)
                    .set(paciente)
                    .addOnSuccessListener {
                        showSuccessDialog("Paciente registrado correctamente y asignado al médico.")
                    }
                    .addOnFailureListener { e ->
                        showAlert("Error al asignar paciente al médico: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                showAlert("Error al registrar paciente: ${e.message}")
                Log.e("ERROR", "Error al registrar paciente", e)
            }
    }

    private fun showSuccessDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
            .setMessage(message)
            .setPositiveButton("Aceptar") { _, _ ->
                val intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
                finish()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
