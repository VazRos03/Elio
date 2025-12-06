package com.example.proyectofinal

data class Task (
    // ID autoincremental de la base de datos. Es nullable ya que no existirá al crear la tarea.
    val id: Int? = null,
    val userId: String,
    val title: String,
    val deliveryDate: String, // Usaremos String (TEXT) para simplicidad con SQLite
    val description: String,
    // Estado para controlar la expansión en la interfaz (no se almacena en SQLite)
    var isExpanded: Boolean = false
)