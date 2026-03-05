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

class AreaConverterActivity : AppCompatActivity() {

    private val toSquareMeters = mapOf(
        "Square Meter" to 1.0,
        "Square Kilometer" to 1_000_000.0,
        "Square Foot" to 0.092903,
        "Square Inch" to 0.00064516,
        "Hectare" to 10_000.0,
        "Acre" to 4046.856
    )

    private var fromUnit = "Square Meter"
    private var toUnit = "Square Meter"

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
            spinner.adapter = makeAdapter(R.array.area_array_from)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    fromUnit = parent.getItemAtPosition(position).toString()
                    convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        findViewById<Spinner>(R.id.length_array_to).also { spinner ->
            spinner.adapter = makeAdapter(R.array.area_array_to)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    toUnit = parent.getItemAtPosition(position).toString()
                    convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun convert() {
        val input = findViewById<EditText>(R.id.lengthFromInput).text.toString().toDoubleOrNull()
        val output = findViewById<EditText>(R.id.lengthToInput)
        if (input == null) { output.setText(""); return }
        val base = input * (toSquareMeters[fromUnit] ?: 1.0)
        val result = base / (toSquareMeters[toUnit] ?: 1.0)
        output.setText(if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString())
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.length_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.area)
    }

    private fun backButton() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
