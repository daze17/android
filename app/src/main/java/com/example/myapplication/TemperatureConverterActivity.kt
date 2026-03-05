package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TemperatureConverterActivity : AppCompatActivity() {

    private var fromUnit = "Celsius"
    private var toUnit = "Celsius"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        backButton()
        setupSpinners()

        findViewById<EditText>(R.id.lengthFromInput).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { convert() }
        })
    }

    private fun setupSpinners() {
        fun makeAdapter(arrayRes: Int): ArrayAdapter<CharSequence> {
            val adapter = ArrayAdapter.createFromResource(this, arrayRes, R.layout.spinner_item)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            return adapter
        }

        findViewById<Spinner>(R.id.length_array_from).also { spinner ->
            spinner.adapter = makeAdapter(R.array.temperature_array_from)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    fromUnit = parent.getItemAtPosition(position).toString()
                    convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        findViewById<Spinner>(R.id.length_array_to).also { spinner ->
            spinner.adapter = makeAdapter(R.array.temperature_array_to)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    toUnit = parent.getItemAtPosition(position).toString()
                    convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun toCelsius(value: Double, unit: String): Double = when (unit) {
        "Celsius" -> value
        "Fahrenheit" -> (value - 32) * 5.0 / 9.0
        "Kelvin" -> value - 273.15
        else -> value
    }

    private fun fromCelsius(celsius: Double, unit: String): Double = when (unit) {
        "Celsius" -> celsius
        "Fahrenheit" -> celsius * 9.0 / 5.0 + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }

    private fun convert() {
        val input = findViewById<EditText>(R.id.lengthFromInput).text.toString().toDoubleOrNull()
        val output = findViewById<EditText>(R.id.lengthToInput)
        if (input == null) { output.setText(""); return }
        val celsius = toCelsius(input, fromUnit)
        val result = fromCelsius(celsius, toUnit)
        output.setText(if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString())
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.length_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.temperature)
    }

    private fun backButton() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
