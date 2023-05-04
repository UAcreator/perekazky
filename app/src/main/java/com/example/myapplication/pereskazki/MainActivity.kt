package com.example.myapplication.pereskazki

import android.content.res.Resources
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.util.TypedValue
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
import android.graphics.Paint
import android.text.TextPaint
import com.example.utils.OnSwipeTouchListener


class MainActivity : AppCompatActivity() {
    private var phrases = emptyArray<String>()
    private var skoromovki = emptyArray<String>()
    private var abetka = emptyArray<String>()
    private lateinit var textView: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button1.text = "ПРИКАЗКИ"
        button2.text = "СКОРОМОВКИ"
        button3.text = "АБЕТКА"
        loadPhrasesFromFile("phrases.txt")
        loadPhrasesFromFile("skoromovki.txt")
        loadPhrasesFromFile("abetka.txt")

        button1.setOnClickListener {
            button1.visibility = View.GONE
            button2.visibility = View.GONE
            button3.visibility = View.GONE

            val randomPhrase = getRandomPhrase()
            textView.text = randomPhrase
            textView.visibility = View.VISIBLE

            // Включаем слушатель свайпа на весь макет
            val mainLayout = findViewById<LinearLayout>(R.id.layout)
            mainLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
                override fun onSwipeRight() {
                    // обработчик свайпа влево
                    button1.visibility = View.VISIBLE
                    button2.visibility = View.VISIBLE
                    button3.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    mainLayout.setOnTouchListener(null)
                }

                override fun onSwipeLeft() {
                    // обработчик свайпа вправо
                    val randomPhrase = getRandomPhrase()
                    textView.text = randomPhrase
                    button1.visibility = View.GONE
                    button2.visibility = View.GONE
                    button3.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
            })
        }

        button2.setOnClickListener {
            button1.visibility = View.GONE
            button2.visibility = View.GONE
            button3.visibility = View.GONE

            val randomSkoromovka = getRandomSkoromovka()
            textView.text = randomSkoromovka
            textView.visibility = View.VISIBLE

            // Включаем слушатель свайпа на весь макет
            val mainLayout = findViewById<LinearLayout>(R.id.layout)
            mainLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
                override fun onSwipeRight() {
                    // обработчик свайпа влево
                    button1.visibility = View.VISIBLE
                    button2.visibility = View.VISIBLE
                    button3.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    mainLayout.setOnTouchListener(null)
                }

                override fun onSwipeLeft() {
                    // обработчик свайпа вправо
                    val randomSkoromovka = getRandomSkoromovka()
                    textView.text = randomSkoromovka
                    button1.visibility = View.GONE
                    button2.visibility = View.GONE
                    button3.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
            })
        }

        button3.setOnClickListener {
            button1.visibility = View.GONE
            button2.visibility = View.GONE
            button3.visibility = View.GONE

            //текст, который вы хотите вывести на экран
            val randomAbetka = getRandomAbetka()
            setTextViewText(textView, randomAbetka)
            textView.visibility = View.VISIBLE

            val mainLayout = findViewById<LinearLayout>(R.id.layout)
            mainLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
                override fun onSwipeRight() {
                    // обработчик свайпа влево
                    button1.visibility = View.VISIBLE
                    button2.visibility = View.VISIBLE
                    button3.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    mainLayout.setOnTouchListener(null)
                }

                override fun onSwipeLeft() {
                    // обработчик свайпа вправо
                    val randomAbetka = getRandomAbetka()
                    textView.text = randomAbetka
                    button1.visibility = View.GONE
                    button2.visibility = View.GONE
                    button3.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
            })
        }
    }
    private fun setTextViewText(textView: TextView, text: String) {
            // Получаем размер текста в пикселях
            val textSize = textView.paint.measureText(text)

            // Получаем ширину экрана
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels

            // Вычисляем соотношение ширины текста к ширине экрана
            val textWidthRatio = textSize / screenWidth

            // Получаем текущий размер шрифта
            val originalTextSize = textView.textSize

            // Если соотношение больше 1, то текст не влезает на экран
            if (textWidthRatio > 1) {
                // Уменьшаем размер текста на соответствующий коэффициент
                val textSizeRatio = 3 / textWidthRatio
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.textSize * textSizeRatio)
            }
            // Устанавливаем текст в TextView
            textView.text = text
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
    private fun getRandomAbetka(): String {
        // Генерируем случайный индекс из массива блоков текста skoromovki
        val random = Random()
        val index = random.nextInt(abetka.size)

        // Возвращаем случайный блок текста из массива блоков текста skoromovki
        return abetka[index]
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
            "abetka.txt" -> abetka = blocks.toTypedArray()
        }
    }
}