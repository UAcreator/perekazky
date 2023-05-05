package com.example.myapplication.pereskazki

import android.content.res.Resources
import android.os.Bundle
//import android.text.Layout
//import android.text.StaticLayout
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
//import com.example.utils.splitIntoSyllables
//import java.io.File
//import java.io.InputStream
//import android.graphics.Paint
//import android.text.TextPaint
import android.util.Log
import com.example.utils.OnSwipeTouchListener
import android.graphics.Color


class MainActivity : AppCompatActivity() {
    private var phrases = emptyArray<String>()
    private var skoromovki = emptyArray<String>()
    private var abetka = emptyArray<String>()
    private lateinit var textView: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var mainLayout: LinearLayout

    override fun onBackPressed() {
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button3.visibility = View.VISIBLE
        textView.visibility = View.GONE
        textView.textSize = 32f
        mainLayout.setOnTouchListener(null)
    }

    fun setTextViewText(textView: TextView, text: String) {
        val holoLightColors = intArrayOf(
            Color.parseColor("#ffec407a"), // Red
            Color.parseColor("#fffce100"), // Yellow
            Color.parseColor("#ff0099cc"), // Blue
            Color.parseColor("#ff669900"), // Green
            Color.parseColor("#ffff9800"), // Orange
            Color.parseColor("#ff3f51b5"), // Indigo
            Color.parseColor("#ff9c27b0"), // Purple
            Color.parseColor("#ff4caf50") // Green
        )
        // Разбиваем текст на строки
        val lines = text.split("\n")
        if (lines.isNotEmpty() && lines[0].length == 2) {
            // Если первая строка состоит из 2 символов, изменяем ее размер и цвет
            textView.textSize = 42f
            textView.setTextColor(holoLightColors.random())
            // Устанавливаем первую строку без переноса
            textView.text = lines[0]
            for (i in 1 until lines.size) {
                // Добавляем остальные строки, вписывая их по ширине экрана без переноса
                val currentText = textView.text.toString()
                val newText = if (currentText.isEmpty()) {
                    lines[i]
                } else {
                    currentText + "\n" + lines[i]
                }
                textView.text = newText
                val currentLineWidth = textView.paint.measureText(lines[i])
                if (currentLineWidth > textView.width) {
                    // Уменьшаем размер текста до тех пор, пока строка не влезет по ширине экрана
                    var textSize = textView.textSize
                    while (textView.paint.measureText(lines[i]) > textView.width) {
                        textSize -= 1f
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                    }
                }
            }
        } else {
            // Если первая строка состоит из более чем 2 символов, обрабатываем и выводим как и раньше
            textView.textSize = 50f
            // Находим максимальную ширину строки
            val maxLineWidth = lines.maxOfOrNull { line ->
                textView.paint.measureText(line)
            } ?: return // В случае, если строки нет, завершаем метод
            // Получаем ширину экрана
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            // Вычисляем соотношение максимальной ширины строки к ширине экрана
            val textWidthRatio = maxLineWidth / screenWidth
            Log.d("FONT_SIZE", "Screen width is ${screenWidth}px")
            Log.d("FONT_SIZE", "Max line width is ${maxLineWidth}px")
            // Если соотношение больше 1, то текст не влезает на экран
            if (textWidthRatio > 1) {
                // Уменьшаем размер текста на соответствующий коэффициент
                val textSizeRatio = 1 / textWidthRatio
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.textSize * textSizeRatio)
            }
            // Устанавливаем текст в TextView
            textView.text = text
            Log.d("FONT_SIZE", "Font size in setTextViewText is ${textView.textSize}")
        }
    }


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
        mainLayout = findViewById(R.id.layout)
        loadPhrasesFromFile("phrases.txt")
        loadPhrasesFromFile("skoromovki.txt")
        loadPhrasesFromFile("abetka.txt")
        textView.visibility = View.GONE

        // Обработчик нажатия на кнопку 1
        button1.setOnClickListener {
            // Вызываем функцию для обработки нажатия на кнопку 1
            handleButton1Click()
        }

        // Обработчик нажатия на кнопку 2
        button2.setOnClickListener {
            // Вызываем функцию для обработки нажатия на кнопку 2
            handleButton2Click()
        }

        // Обработчик нажатия на кнопку 3
        button3.setOnClickListener {
            // Вызываем функцию для обработки нажатия на кнопку 3
            handleButton3Click()
        }

    }

    private fun handleButton1Click() {
        // Обработка нажатия на кнопку 1
        button1.visibility = View.GONE
        button2.visibility = View.GONE
        button3.visibility = View.GONE
        val randomPhrase = getRandomPhrase()
        textView.text = randomPhrase
        Log.d("FONT_SIZE", "Font size in handleButton1Click 1 is ${textView.textSize}")
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
                Log.d("FONT_SIZE", "Font size in handleButton1Click 1 onSwipeRight is ${textView.textSize}")
            }

            override fun onSwipeLeft() {
                // обработчик свайпа вправо
                val randomPhrase = getRandomPhrase()
                textView.text = randomPhrase
                Log.d("FONT_SIZE", "Font size in handleButton1Click 1 onSwipeLeft is ${textView.textSize}")
                button1.visibility = View.GONE
                button2.visibility = View.GONE
                button3.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        })


    }

    private fun handleButton2Click() {
        // Обработка нажатия на кнопку 2
        button1.visibility = View.GONE
        button2.visibility = View.GONE
        button3.visibility = View.GONE

        val randomSkoromovka = getRandomSkoromovka()
        val textSize = setTextViewText(textView,randomSkoromovka)
        Log.d("FONT_SIZE", "Font size in handleButton1Click 2 is ${textView.textSize}")
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
                textView.textSize = 32f
                mainLayout.setOnTouchListener(null)
                Log.d("FONT_SIZE", "Font size in handleButton1Click 2 onSwipeRight is ${textView.textSize}")
            }

            override fun onSwipeLeft() {
                // обработчик свайпа вправо
                val randomSkoromovka = getRandomSkoromovka()
                val textSize = setTextViewText(textView, randomSkoromovka)
                Log.d("FONT_SIZE", "Font size in handleButton1Click 2 onSwipeLeft is ${textView.textSize}")
                button1.visibility = View.GONE
                button2.visibility = View.GONE
                button3.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        })
    }

    private fun handleButton3Click() {
        button1.visibility = View.GONE
        button2.visibility = View.GONE
        button3.visibility = View.GONE
        Log.d("FONT_SIZE", "Font size in handleButton1Click 3 before randomAbetka is ${textView.textSize}")


        val randomAbetka = getRandomAbetka()
        val textSize = setTextViewText(textView, randomAbetka)
        Log.d("FONT_SIZE", "Font size after setTextViewText 3 is ${textView.textSize}")
        textView.visibility = View.VISIBLE

        val mainLayout = findViewById<LinearLayout>(R.id.layout)
        mainLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeRight() {
                // Обработчик свайпа влево
                button1.visibility = View.VISIBLE
                button2.visibility = View.VISIBLE
                button3.visibility = View.VISIBLE
                textView.visibility = View.GONE
                textView.textSize = 32f
                mainLayout.setOnTouchListener(null)
                Log.d("FONT_SIZE", "Font size in handleButton1Click 3 onSwipeRight is ${textView.textSize}")
            }

            override fun onSwipeLeft() {
                // Обработчик свайпа вправо
                val randomAbetka = getRandomAbetka()
                val textSize = setTextViewText(textView, randomAbetka)
                Log.d("FONT_SIZE", "Font size in handleButton1Click 3 onSwipeLeft is ${textView.textSize}")
                button1.visibility = View.GONE
                button2.visibility = View.GONE
                button3.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        })
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