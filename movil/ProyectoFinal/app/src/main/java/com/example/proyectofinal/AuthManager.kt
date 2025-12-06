package com.example.proyectofinal

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

/**
 * Gestor de autenticación que implementa el patrón Singleton para asegurar
 * una única instancia de FirebaseAuth en toda la aplicación.
 *
 * Incluye los métodos necesarios para: Registro, Login, Obtener usuario, y Logout.
 */
class AuthManager private constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        // Implementación del patrón Singleton
        val instance: AuthManager by lazy {
            AuthManager()
        }
    }

    // ----------------------------------------------------
    // MÉTODOS REQUERIDOS POR MainActivity.kt
    // ----------------------------------------------------

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     * Requerido en MainActivity para verificar la sesión al inicio.
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Registra un nuevo usuario con email y contraseña.
     */
    fun registerUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Error desconocido en el registro."
                    Log.e("AuthManager", "Error de registro: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    /**
     * Inicia sesión de un usuario con email y contraseña.
     */
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Error desconocido en el inicio de sesión."
                    Log.e("AuthManager", "Error de login: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    // ----------------------------------------------------
    // MÉTODOS REQUERIDOS POR PanelActivity.kt (LA CORRECCIÓN)
    // ----------------------------------------------------

    /**
     * Obtiene el email del usuario actualmente autenticado.
     * Requerido en PanelActivity para mostrar el identificador.
     */
    fun getCurrentUserEmail(): String? {
        // Esta es la función que faltaba
        return auth.currentUser?.email
    }

    /**
     * Cierra la sesión del usuario actual.
     * Requerido en PanelActivity para el botón de Cerrar Sesión.
     */
    fun logout() {
        // Esta es la función que faltaba
        auth.signOut()
    }
}