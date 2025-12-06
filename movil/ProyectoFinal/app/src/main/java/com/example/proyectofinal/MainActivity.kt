package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity principal que maneja la interfaz de usuario de Login.
 * La lógica de autenticación se delega a AuthManager.kt.
 */
class MainActivity : AppCompatActivity() {

    // 1. CORRECCIÓN: Usamos 'lateinit var' para declarar la propiedad
    // y la inicializamos dentro de onCreate.
    private lateinit var authManager: AuthManager

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usar el layout con la nueva estética
        setContentView(R.layout.activity_main)

        // 2. CORRECCIÓN: Asignamos la instancia Singleton a la propiedad global
        // 'authManager', inicializando así la lateinit property.
        authManager = AuthManager.instance // AHORA la propiedad global queda inicializada

        // Referencias a los elementos de la UI
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        // 3. Revisar si hay un usuario logueado al iniciar la Activity
        if (authManager.getCurrentUserId() != null) {
            // Si hay sesión activa, ir directamente al Panel
            navigateToPanel()
        }

        // 4. Configuración del botón de Registro
        registerButton.setOnClickListener {
            handleRegistration() // Llama a la función que ahora usa la 'authManager' inicializada
        }

        // 5. Configuración del botón de Inicio de Sesión
        loginButton.setOnClickListener {
            handleLogin()
        }
    }

    /**
     * Valida los campos y llama a la función de registro en AuthManager.
     */
    private fun handleRegistration() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
            return
        }

        // Deshabilitar botones para evitar clics múltiples
        setLoadingState(true)

        // Aquí se usa la propiedad global 'authManager' que fue inicializada en onCreate.
        authManager.registerUser(email, password,
            onSuccess = {
                // Éxito: Mostrar mensaje y navegar al Panel
                Toast.makeText(this, "Registro exitoso.", Toast.LENGTH_SHORT).show()
                navigateToPanel()
                setLoadingState(false)
            },
            onFailure = { errorMessage ->
                // Falla: Mostrar error de Firebase
                Toast.makeText(this, "Error de Registro: $errorMessage", Toast.LENGTH_LONG).show()
                setLoadingState(false)
            }
        )
    }

    /**
     * Valida los campos y llama a la función de login en AuthManager.
     */
    private fun handleLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Deshabilitar botones para evitar clics múltiples
        setLoadingState(true)

        // Aquí se usa la propiedad global 'authManager'
        authManager.loginUser(email, password,
            onSuccess = {
                // Éxito: Navegar al Panel
                Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                navigateToPanel()
                setLoadingState(false)
            },
            onFailure = { errorMessage ->
                // Falla: Mostrar error de Firebase
                Toast.makeText(this, "Error de Login: $errorMessage", Toast.LENGTH_LONG).show()
                setLoadingState(false)
            }
        )
    }

    /**
     * Cambia la Activity actual a PanelActivity.
     */
    private fun navigateToPanel() {
        val intent = Intent(this, PanelActivity::class.java)
        // Flag para limpiar la pila de actividades (no puede volver al login)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finaliza esta Activity para que no quede en la pila
    }

    /**
     * Habilita o deshabilita los botones y campos de texto durante la operación de red.
     */
    private fun setLoadingState(isLoading: Boolean) {
        loginButton.isEnabled = !isLoading
        registerButton.isEnabled = !isLoading
        emailEditText.isEnabled = !isLoading
        passwordEditText.isEnabled = !isLoading

        // Opcional: Mostrar un ProgressBar aquí si tuvieras uno en el layout
        // progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}