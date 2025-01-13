package com.example.appcancerpiel.modelo

data class Paciente(
    val id: String = "", // ID del documento en Firestore
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val telefono: String = "",
    val dni: String = "",
    val sexo: String = ""
)

