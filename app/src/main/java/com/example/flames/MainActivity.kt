package com.example.flames

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var name1EditText: EditText
    private lateinit var name2EditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultText: TextView

    private var remainingCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name1EditText = findViewById(R.id.name1)
        name2EditText = findViewById(R.id.name2)
        calculateButton = findViewById(R.id.calculate)
        resultText = findViewById(R.id.result)

        calculateButton.setOnClickListener {
            try {
                hideKeyboard(it)
                removeMatchedChar()
                val result = flamesResult(remainingCount)
                printResult(result)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
                Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun removeMatchedChar() {
        val name1str = name1EditText.text.toString()
        val name2str = name2EditText.text.toString()

        if (name1str.isEmpty() || name2str.isEmpty()) {
            Toast.makeText(this, "Please enter valid names!", Toast.LENGTH_SHORT).show()
            return
        }

        val name1Builder = StringBuilder(name1str)
        val name2Builder = StringBuilder(name2str)

        for (i in name1str.indices) {
            for (j in name2str.indices) {
                if (name1Builder[i] == name2Builder[j] && name1Builder[i] != '*') {
                    name1Builder.setCharAt(i, '*')
                    name2Builder.setCharAt(j, '*')
                    break
                }
            }
        }
        name1EditText.text.clear()
        name2EditText.text.clear()
        remainingCount = countRemainingChar(name1Builder.toString(), name2Builder.toString())
    }

    private fun countRemainingChar(name1st: String, name2nd: String): Int {
        var count = 0
        for (ch in name1st) {
            if (ch != '*') {
                count++
            }
        }
        for (ch in name2nd) {
            if (ch != '*') {
                count++
            }
        }
        return count
    }

    private fun flamesResult(count: Int): Char {
        var flames = "FLAMES"
        var pos = 0

        for (i in flames.length downTo 2) {
            pos = (pos + count - 1) % i
            val flamesArray = flames.toCharArray()
            for (j in pos until i - 1) {
                flamesArray[j] = flamesArray[j + 1]
            }
            flamesArray[i - 1] = '\u0000'
            flames = String(flamesArray)
        }
        return flames[0]
    }
    private fun printResult(result: Char) {
        when(result){
            'F' -> resultText.text = String.format(Locale.getDefault(), "You both are good friends!")
            'L' -> resultText.text = String.format(Locale.getDefault(), "There is love between you both!")
            'A' -> resultText.text = String.format(Locale.getDefault(), "You both have deep affection for each other!")
            'M' -> resultText.text = String.format(Locale.getDefault(), "Relationship between you two is Marriage, indicating a strong marital potential!")
            'E' -> resultText.text = String.format(Locale.getDefault(), "Unfortunately, you both are enemies.")
            'S' ->resultText.text = String.format(Locale.getDefault(), "You both are like brother and sister, sharing a close bond")
        }

    }
}
