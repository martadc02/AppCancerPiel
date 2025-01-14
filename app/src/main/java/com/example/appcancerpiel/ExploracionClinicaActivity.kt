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

class ExploracionClinicaActivity : AppCompatActivity() {

    private lateinit var checkboxLesionesCrecimiento: CheckBox
    private lateinit var checkboxUlceraCuracion: CheckBox
    private lateinit var checkboxPustulaHemorragica: CheckBox
    private lateinit var checkboxLesionesCicatrizales: CheckBox
    private lateinit var checkboxLesionCicatriz: CheckBox
    private lateinit var checkboxLesionHiperqueratosica: CheckBox
    private lateinit var checkboxLesionQueratocica: CheckBox
    private lateinit var checkboxQuemaduras: CheckBox
    private lateinit var botonGuardar: Button
    private lateinit var nombrePacienteView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exploracion_clinica)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Rellenar Exploración Clínica"
        }

        // Obtener datos del intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val dni = intent.getStringExtra("DNI")


        // Inicializar vistas
        checkboxLesionesCrecimiento = findViewById(R.id.checkbox_lesiones_crecimiento)
        checkboxUlceraCuracion = findViewById(R.id.checkbox_ulcera_curacion)
        checkboxPustulaHemorragica = findViewById(R.id.checkbox_pustula_hemorragica)
        checkboxLesionesCicatrizales = findViewById(R.id.checkbox_lesiones_cicatrizales)
        checkboxLesionCicatriz = findViewById(R.id.checkbox_lesion_cicatriz)
        checkboxLesionHiperqueratosica = findViewById(R.id.checkbox_lesion_hiperqueratosica)
        checkboxLesionQueratocica = findViewById(R.id.checkbox_lesion_queratosica)
        checkboxQuemaduras = findViewById(R.id.checkbox_quemaduras)
        botonGuardar = findViewById(R.id.boton_guardar_exploracion)
        nombrePacienteView = findViewById(R.id.nombre_paciente)

        // Mostrar el nombre del paciente
        nombrePacienteView.text = "$nombre $apellidos"

        // Configurar el botón Guardar
        botonGuardar.setOnClickListener {
            guardarExploracionClinica(nombre.toString(), apellidos.toString(), dni.toString())
        }
    }

    private fun guardarExploracionClinica(nombre: String, apellidos: String, dni: String) {
        // Crear un mapa con los datos seleccionados
        val exploracion = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "dni" to dni,
            "lesionesCrecimiento" to checkboxLesionesCrecimiento.isChecked,
            "ulceraCuracion" to checkboxUlceraCuracion.isChecked,
            "pustulaHemorragica" to checkboxPustulaHemorragica.isChecked,
            "lesionesCicatrizales" to checkboxLesionesCicatrizales.isChecked,
            "lesionCicatriz" to checkboxLesionCicatriz.isChecked,
            "lesionHiperqueratosica" to checkboxLesionHiperqueratosica.isChecked,
            "lesionQueratocica" to checkboxLesionQueratocica.isChecked,
            "quemaduras" to checkboxQuemaduras.isChecked
        )

        // Guardar en Firebase
        db.collection("ExploracionesClinicas")
            .add(exploracion)
            .addOnSuccessListener {
                Toast.makeText(this, "Exploración clínica guardada correctamente.", Toast.LENGTH_SHORT).show()
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
