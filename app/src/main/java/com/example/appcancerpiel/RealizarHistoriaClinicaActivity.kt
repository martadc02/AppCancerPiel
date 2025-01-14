package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class RealizarHistoriaClinicaActivity : AppCompatActivity() {

    private lateinit var colorPiel: EditText
    private lateinit var colorPelo: EditText
    private lateinit var colorOjos: EditText
    private lateinit var checkboxExposicionSolar: CheckBox
    private lateinit var checkboxRadiacionesIonizantes: CheckBox
    private lateinit var checkboxConsumoAlcohol: CheckBox
    private lateinit var checkboxConsumoTabaco: CheckBox
    private lateinit var botonGuardar: Button
    private lateinit var nombrePacienteView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_historia_clinica)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Rellenar Historia Clínica"
        }

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val dni = intent.getStringExtra("DNI")

        // Inicializar vistas
        colorPiel = findViewById(R.id.color_piel)
        colorPelo = findViewById(R.id.color_pelo)
        colorOjos = findViewById(R.id.color_ojos)
        checkboxExposicionSolar = findViewById(R.id.checkbox_exposicion_solar)
        checkboxRadiacionesIonizantes = findViewById(R.id.checkbox_radiaciones_ionizantes)
        checkboxConsumoAlcohol = findViewById(R.id.checkbox_consumo_alcohol)
        checkboxConsumoTabaco = findViewById(R.id.checkbox_consumo_tabaco)
        botonGuardar = findViewById(R.id.boton_guardar_historia)
        nombrePacienteView = findViewById(R.id.nombre_paciente)

        // Mostrar el nombre del paciente
        nombrePacienteView.text = "$nombre $apellidos"

        // Configurar el botón Guardar
        botonGuardar.setOnClickListener {
            guardarHistoriaClinica(nombre.toString(), apellidos.toString(), dni.toString())
        }
    }

    private fun guardarHistoriaClinica(nombre: String, apellidos: String, dni: String) {
        // Obtener datos ingresados por el usuario
        val colorPielTexto = colorPiel.text.toString()
        val colorPeloTexto = colorPelo.text.toString()
        val colorOjosTexto = colorOjos.text.toString()

        // Validar campos obligatorios
        if (colorPielTexto.isBlank() || colorPeloTexto.isBlank() || colorOjosTexto.isBlank()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa con los datos
        val historiaClinica = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "dni" to dni,
            "colorPiel" to colorPielTexto,
            "colorPelo" to colorPeloTexto,
            "colorOjos" to colorOjosTexto,
            "exposicionSolar" to checkboxExposicionSolar.isChecked,
            "radiacionesIonizantes" to checkboxRadiacionesIonizantes.isChecked,
            "consumoAlcohol" to checkboxConsumoAlcohol.isChecked,
            "consumoTabaco" to checkboxConsumoTabaco.isChecked
        )

        // Guardar en Firebase Firestore
        db.collection("HistoriasClinicas")
            .add(historiaClinica)
            .addOnSuccessListener {
                Toast.makeText(this, "Historia clínica guardada correctamente.", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad
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

