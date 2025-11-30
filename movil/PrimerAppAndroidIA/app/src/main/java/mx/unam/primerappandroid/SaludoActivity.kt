package mx.unam.primerappandroid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SaludoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saludo)

        // 1. Obtener las referencias de los TextViews del layout
        val saludoTextView = findViewById<TextView>(R.id.textViewSaludo)
        val cartoonTextView = findViewById<TextView>(R.id.textViewCartoon)
        val foodTextView = findViewById<TextView>(R.id.textViewFood)

        // 2. Obtener los Intent Extras (los datos enviados desde MainActivity)
        val name = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val cartoon = intent.getStringExtra(MainActivity.EXTRA_CARTOON)
        val food = intent.getStringExtra(MainActivity.EXTRA_FOOD)

        // 3. Construir y mostrar el mensaje de saludo principal (Nombre)
        // Usamos .capitalize() para asegurar que el nombre empiece con mayúscula (si existe)
        val formattedName = if (name.isNullOrEmpty()) "aventurero/a misterioso/a" else name.capitalize()
        saludoTextView.text = "¡Hola, $formattedName! 🚀"

        // 4. Mostrar la Caricatura Favorita (con manejo de campo vacío)
        if (cartoon.isNullOrEmpty()) {
            cartoonTextView.text = "No nos contaste tu caricatura favorita. ¡Qué pena! 🤔"
        } else {
            cartoonTextView.text = "Tu caricatura favorita es: ${cartoon.capitalize()}. ¡Genial! ✨"
        }

        // 5. Mostrar la Comida Favorita (con manejo de campo vacío)
        if (food.isNullOrEmpty()) {
            foodTextView.text = "No sabemos cuál es tu comida favorita. ¡A comer se ha dicho! 🍔"
        } else {
            foodTextView.text = "¡Y adoras la comida: ${food.capitalize()}! 😋"
        }
    }

    // Función de extensión simple para la primera letra en mayúscula
    // Nota: En Kotlin moderno, podrías usar '.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }'
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}