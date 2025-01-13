package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.appcancerpiel.R

class DatosPacienteActivity : AppCompatActivity() {

    private lateinit var textNombre: TextView
    private lateinit var textApellidos: TextView
    private lateinit var textFechaNacimiento: TextView
    private lateinit var textTelefono: TextView
    private lateinit var textDNI: TextView
    private lateinit var textEmail: TextView
    private lateinit var textSexo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_paciente)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Datos del Paciente"
        }

        // Vincular TextViews con el layout
        textNombre = findViewById(R.id.textNombre)
        textApellidos = findViewById(R.id.textApellidos)
        textFechaNacimiento = findViewById(R.id.textFechaNacimiento)
        textTelefono = findViewById(R.id.textTelefono)
        textDNI = findViewById(R.id.textDNI)
        textEmail = findViewById(R.id.textEmail)
        textSexo = findViewById(R.id.textSexo)

        // Recuperar los datos del Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val email = intent.getStringExtra("EMAIL")
        val telefono = intent.getStringExtra("TELEFONO")
        val dni = intent.getStringExtra("DNI")
        val fechaNacimiento = intent.getStringExtra("FECHA_NACIMIENTO")
        val sexo = intent.getStringExtra("SEXO")

        // Mostrar los datos en los TextViews
        textNombre.text = "Nombre: $nombre"
        textApellidos.text = "Apellidos: $apellidos"
        textFechaNacimiento.text = "Fecha de Nacimiento: $fechaNacimiento"
        textTelefono.text = "Teléfono: $telefono"
        textDNI.text = "DNI: $dni"
        textEmail.text = "Email: $email"
        textSexo.text = "Sexo: $sexo"
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




