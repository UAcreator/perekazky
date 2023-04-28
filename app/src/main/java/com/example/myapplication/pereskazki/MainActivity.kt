package com.example.myapplication.pereskazki

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {
    private val phrases = arrayOf(
        "Самолет иди в поход",
        "Сергей теплоход налей",
        "Вика вкусная клубника",
        "Розовый слоник летит к небу",
        "Мыши кричат в траве",
        "Жаба прыгает на гриб"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем ссылку на TextView
        val textView = findViewById<TextView>(R.id.textView)

        // Добавляем слушатель нажатия на весь макет
        val layout = findViewById<LinearLayout>(R.id.layout)
        layout.setOnClickListener {
            // Изменяем текст в TextView
            textView.text = getRandomPhrase()
        }
    }

    private fun getRandomPhrase(): String {
        // Генерируем случайный индекс из массива строк phrases
        val random = Random()
        val index = random.nextInt(phrases.size)

        // Возвращаем случайную фразу из массива строк phrases
        return phrases[index]
    }
}
