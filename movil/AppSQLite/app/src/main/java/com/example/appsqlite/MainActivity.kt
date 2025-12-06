package com.example.appsqlite

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import  com.example.appsqlite.R

/*Tarea SQLite
* Desarrollar una app que ghaga uso de un sqlite
* funciones crud
* Luis Arturo Vazquez Rosales*/
class MainActivity : AppCompatActivity() {

    // Vistas de la UI
    private lateinit var dbHelper: MiBaseDatosHelper
    private lateinit var editTextId: EditText
    private lateinit var editTextNombre: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnLeer: Button
    private lateinit var btnModificar: Button
    private lateinit var btnBorrar: Button
    private lateinit var textViewResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar la base de datos
        dbHelper = MiBaseDatosHelper(this)

        // 1. Inicializar las Vistas
        editTextId = findViewById(R.id.editTextId)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        btnInsertar = findViewById(R.id.btnInsertar)
        btnLeer = findViewById(R.id.btnLeer)
        btnModificar = findViewById(R.id.btnModificar)
        btnBorrar = findViewById(R.id.btnBorrar)
        textViewResultado = findViewById(R.id.textViewResultado)

        // 2. Configurar los Listeners de los Botones

        btnInsertar.setOnClickListener {
            insertarContacto()
        }

        btnLeer.setOnClickListener {
            leerContactos()
        }

        btnModificar.setOnClickListener {
            modificarContacto()
        }

        btnBorrar.setOnClickListener {
            borrarContacto()
        }
    }

    // --- Funciones CRUD de la Actividad ---

    private fun insertarContacto() {
        // La ID no es necesaria para insertar, ya que es AUTOINCREMENT
        val nombre = editTextNombre.text.toString().trim()
        val telefono = editTextTelefono.text.toString().trim()

        if (nombre.isNotEmpty() && telefono.isNotEmpty()) {
            dbHelper.insertarDato(nombre, telefono)
            // Limpiar campos después de la inserción
            limpiarCampos()
            // Mostrar los contactos actualizados
            leerContactos()
        } else {
            Toast.makeText(this, "Debe ingresar Nombre y Teléfono para insertar.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerContactos() {
        val cursor: Cursor = dbHelper.leerTodosLosDatos()
        val stringBuilder = StringBuilder()

        if (cursor.count == 0) {
            stringBuilder.append("No hay contactos registrados.")
        } else {
            stringBuilder.append("--- LISTA DE CONTACTOS ---\n")
            // Recorrer el Cursor para obtener los datos
            while (cursor.moveToNext()) {
                // Obtener los índices de las columnas
                val idIndex = cursor.getColumnIndexOrThrow("id")
                val nombreIndex = cursor.getColumnIndexOrThrow("nombre")
                val telefonoIndex = cursor.getColumnIndexOrThrow("telefono")

                // Obtener los valores por índice
                val id = cursor.getString(idIndex)
                val nombre = cursor.getString(nombreIndex)
                val telefono = cursor.getString(telefonoIndex)

                stringBuilder.append("ID: $id | Nombre: $nombre | Teléfono: $telefono\n")
            }
        }
        cursor.close()
        textViewResultado.text = stringBuilder.toString()
    }

    private fun modificarContacto() {
        val id = editTextId.text.toString().trim()
        val nombre = editTextNombre.text.toString().trim()
        val telefono = editTextTelefono.text.toString().trim()

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Debe ingresar ID, Nombre y Teléfono para modificar.", Toast.LENGTH_LONG).show()
            return
        }

        dbHelper.actualizarDato(id, nombre, telefono)
        limpiarCampos()
        leerContactos()
    }

    private fun borrarContacto() {
        val id = editTextId.text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el ID del contacto a borrar.", Toast.LENGTH_LONG).show()
            return
        }

        dbHelper.eliminarDato(id)
        limpiarCampos()
        leerContactos()
    }

    // --- Utilidades ---

    private fun limpiarCampos() {
        editTextId.text.clear()
        editTextNombre.text.clear()
        editTextTelefono.text.clear()
    }

    // Cierra la conexión a la base de datos cuando la actividad es destruida.
    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
        Log.d("SQLite", "Conexión a la base de datos cerrada.")
    }
}