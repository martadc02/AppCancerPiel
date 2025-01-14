package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class DermatoscopiaActivity : AppCompatActivity() {

    private lateinit var checkboxCarcinomaPigmentado: CheckBox
    private lateinit var checkboxCarcinomaNoPigmentado: CheckBox
    private lateinit var checkboxCarcinomaCelEscamosa: CheckBox
    private lateinit var botonGuardar: Button
    private lateinit var nombrePacienteView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dermatoscopia)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Rellenar Dermatoscopia"
        }

        // Obtener datos del intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val dni = intent.getStringExtra("DNI")


        // Inicializar vistas
        checkboxCarcinomaPigmentado = findViewById(R.id.checkbox_carcinoma_pigmentado)
        checkboxCarcinomaNoPigmentado = findViewById(R.id.checkbox_carcinoma_no_pigmentado)
        checkboxCarcinomaCelEscamosa = findViewById(R.id.checkbox_carcinoma_cel_escamosa)
        botonGuardar = findViewById(R.id.boton_guardar_dermatoscopia)
        nombrePacienteView = findViewById(R.id.nombre_paciente)

        // Mostrar el nombre del paciente
        nombrePacienteView.text = "$nombre $apellidos"


        // Configurar el botón Guardar
        botonGuardar.setOnClickListener {
            guardarDermatoscopia(nombre.toString(), apellidos.toString(), dni.toString())
        }
    }

    private fun guardarDermatoscopia(nombre: String, apellidos: String, dni: String) {
        // Crear mapa con los datos seleccionados
        val dermatoscopia = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "dni" to dni,
            "carcinomaPigmentado" to checkboxCarcinomaPigmentado.isChecked,
            "carcinomaNoPigmentado" to checkboxCarcinomaNoPigmentado.isChecked,
            "carcinomaCelEscamosa" to checkboxCarcinomaCelEscamosa.isChecked
        )

        // Guardar en Firebase
        db.collection("Dermatoscopias")
            .add(dermatoscopia)
            .addOnSuccessListener {
                Toast.makeText(this, "Dermatoscopia guardada correctamente.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
                val intent = Intent(this, HomeMedicoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


