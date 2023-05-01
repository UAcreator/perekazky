package com.example.myapplication.pereskazki

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import android.view.View
import com.example.utils.splitIntoSyllables
import java.io.File
import java.io.InputStream

import com.example.utils.OnSwipeTouchListener
import com.example.utils.OnSwipeTouchListener

class MainActivity : AppCompatActivity() {
    private var phrases = emptyArray<String>()
    private var skoromovki = emptyArray<String>()
    private lateinit var textView: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var swipeListener: setupSwipeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)

        button1.text = "ПРИКАЗКИ"
        button2.text = "СКОРОМОВКИ"

        loadPhrasesFromFile("phrases.txt")
        loadPhrasesFromFile("skoromovki.txt")

        button1.setOnClickListener {
            button1.visibility = View.GONE
            button2.visibility = View.GONE

            val randomPhrase = getRandomPhrase()
            textView.text = randomPhrase
            textView.visibility = View.VISIBLE

            swipeListener = setupSwipeListener(this)
            swipeListener.setupSwipeListener()
        }

        button2.setOnClickListener {
            button1.visibility = View.GONE
            button2.visibility = View.GONE

            val randomSkoromovka = getRandomSkoromovka()
            textView.text = randomSkoromovka
            textView.visibility = View.VISIBLE

            swipeListener = setupSwipeListener(this)
            swipeListener.setupSwipeListener()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущий текст в TextView в Bundle
        outState.putString("text", findViewById<TextView>(R.id.textView).text.toString())
    }

    private fun getRandomPhrase(): String {
        // Генерируем случайный индекс из массива блоков текста phrases
        val random = Random()
        val index = random.nextInt(phrases.size)

        // Возвращаем случайный блок текста из массива блоков текста phrases
        return phrases[index]
    }

    private fun getRandomSkoromovka(): String {
        // Генерируем случайный индекс из массива блоков текста skoromovki
        val random = Random()
        val index = random.nextInt(skoromovki.size)

        // Возвращаем случайный блок текста из массива блоков текста skoromovki
        return skoromovki[index]
    }


    private fun loadPhrasesFromFile(filename: String) {
        // Получаем InputStream из файла с заданным именем
        val inputStream = assets.open(filename)

        // Создаем BufferedReader для чтения из InputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

// Создаем список для хранения блоков текста
        val blocks = mutableListOf<String>()

// Читаем строки из файла и добавляем их в соответствующий блок текста
        try {
            var block = ""
            var line = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty()) {
                    // Добавляем строку в текущий блок текста
                    block += line + "\n"
                } else {
                    // Добавляем текущий блок текста в список, если он не пустой
                    if (block.isNotEmpty()) {
                        blocks.add(block)
                    }
                    // Создаем новый блок текста
                    block = ""
                }
                line = reader.readLine()
            }
            // Добавляем последний блок текста в список, если он не пустой
            if (block.isNotEmpty()) {
                blocks.add(block)
            }
            // Проверяем список blocks. Если он состоит из одного блока, разбиваем его на список блоков
            if (blocks.size == 1) {
                val singleBlock = blocks[0]
                val singleBlockLines = singleBlock.trim().split("\n")
                blocks.clear()
                for (line in singleBlockLines) {
                    blocks.add("$line\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            reader.close()
        }

// Сохраняем массив блоков текста в соответствующее свойство
        when (filename) {
            "phrases.txt" -> phrases = blocks.toTypedArray()
            "skoromovki.txt" -> skoromovki = blocks.toTypedArray()
        }

    }
}