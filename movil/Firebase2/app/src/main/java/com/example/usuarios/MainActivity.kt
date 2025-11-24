package com.example.usuarios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.usuarios.R


class MainActivity : AppCompatActivity() {

    // Declaración de las vistas
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var statusTextView: TextView

    // Instancia de Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usa la clase R para referenciar tu layout
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 1. Enlazar vistas del layout con variables de Kotlin (usando R.id)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginButton = findViewById(R.id.loginButton)
        statusTextView = findViewById(R.id.statusTextView)

        // 2. Configurar listeners para los botones
        registerButton.setOnClickListener {
            registerUser()
        }

        loginButton.setOnClickListener {
            loginUser()
        }
    }

    // 3. Método para manejar la creación de un nuevo usuario
    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada a la API de Firebase para crear usuario
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, actualizar la UI
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(this, "Registro exitoso.", Toast.LENGTH_SHORT).show()
                } else {
                    // Si falla el registro, mostrar un mensaje de error
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Fallo en el registro: $errorMessage", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    // 4. Método para manejar el inicio de sesión
    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa email y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada a la API de Firebase para iniciar sesión
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, actualizar la UI
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                } else {
                    // Si falla el inicio de sesión, mostrar un mensaje de error
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Fallo al iniciar sesión: $errorMessage", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    // 5. Método para actualizar la UI en función del estado de autenticación
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Usuario autenticado
            statusTextView.text = "¡Autenticado!\nEmail: ${user.email}\nID de usuario: ${user.uid.substring(0, 8)}..."
            // Opcional: deshabilitar campos de texto y botones de acción si ya está logueado
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
            registerButton.isEnabled = false
            loginButton.text = "Cerrar Sesión" // Cambiar la acción del botón

            // Configurar el botón de login/logout
            loginButton.setOnClickListener {
                auth.signOut() // Cerrar sesión
                updateUI(null) // Restablecer la UI
                Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Usuario no autenticado
            statusTextView.text = "No autenticado. Por favor, regístrate o inicia sesión."
            // Habilitar campos y botones
            emailEditText.isEnabled = true
            passwordEditText.isEnabled = true
            registerButton.isEnabled = true
            loginButton.text = "Iniciar Sesión"

            // Restablecer listeners originales
            registerButton.setOnClickListener { registerUser() }
            loginButton.setOnClickListener { loginUser() }
        }
    }

    // 6. Revisar el estado de la sesión al inicio de la actividad
    override fun onStart() {
        super.onStart()
        // Verificar si el usuario ya ha iniciado sesión (no es null) y actualizar la UI
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}