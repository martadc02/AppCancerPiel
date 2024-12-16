package com.example.appcancerpiel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClickHome(v: View?){
        when(v?.id){
            R.id.button -> {
                intent = Intent(this, TipoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}