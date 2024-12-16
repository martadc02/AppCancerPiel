package com.example.appcancerpiel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TipoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipo)
    }

    fun onClickInicio(v: View?){
        when(v?.id){
            R.id.botonAdministrativo -> {
                intent = Intent(this, InicioSesionActivity::class.java)
                startActivity(intent)
            }
            R.id.botonMedicoFam -> {
                intent = Intent(this, InicioSesionActivity::class.java)
                startActivity(intent)
            }
            R.id.botonDermatologo -> {
                intent = Intent(this, InicioSesionActivity::class.java)
                startActivity(intent)
            }
        }
    }
}