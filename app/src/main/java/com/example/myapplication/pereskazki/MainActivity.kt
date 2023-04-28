package com.example.myapplication.pereskazki

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import com.example.utils.splitIntoSyllables

class MainActivity : AppCompatActivity() {
    private var phrases = emptyArray<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем ссылку на TextView
        val textView = findViewById<TextView>(R.id.textView)

        // Загружаем фразы из файла
        loadPhrasesFromFile()

        // Если savedInstanceState не равен null, восстанавливаем сохраненное значение текста в TextView
        if (savedInstanceState != null) {
            textView.text = savedInstanceState.getString("text")
        } else {
            // Получаем случайную фразу и устанавливаем её в TextView
            val randomPhrase = getRandomPhrase()
            textView.text = randomPhrase
        }

        // Добавляем слушатель нажатия на весь макет
        val layout = findViewById<LinearLayout>(R.id.layout)
        layout.setOnClickListener {
            // Изменяем текст в TextView на новую случайную фразу
            textView.text = getRandomPhrase()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущий текст в TextView в Bundle
        outState.putString("text", findViewById<TextView>(R.id.textView).text.toString())
    }

      private fun getRandomPhrase(): String {
        // Генерируем случайный индекс из массива строк phrases
        val random = Random()
        val index = random.nextInt(phrases.size)

      // Возвращаем случайную фразу из массива строк phrases
       return phrases[index]
    }

    //private fun getRandomPhrase(): String {
      //  // Генерируем случайный индекс из массива строк phrases
        //val random = Random()
        //val index = random.nextInt(phrases.size)

        // Разбиваем случайную фразу на слова и выводим каждое слово по слогам
        //val words = phrases[index].split(" ")
        //val syllables = words.map { splitIntoSyllables(it) }
        //return syllables.joinToString(separator = " ") { it.joinToString(separator = "-") }
    //}


    private fun loadPhrasesFromFile() {
        // Открываем файл phrases.txt из папки assets и читаем его содержимое в массив строк phrases
        try {
            val inputStream = assets.open("phrases.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val list = mutableListOf<String>()
            var line: String? = reader.readLine()
            while (line != null) {
                list.add(line)
                line = reader.readLine()
            }
            phrases = list.toTypedArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
