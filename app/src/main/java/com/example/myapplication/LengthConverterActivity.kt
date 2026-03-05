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

class LengthConverterActivity : AppCompatActivity() {

    private val toMeters = mapOf(
        "Meter" to 1.0,
        "Kilometer" to 1000.0,
        "Centimeter" to 0.01,
        "Millimeter" to 0.001,
        "Inch" to 0.0254,
        "Foot" to 0.3048,
        "Yard" to 0.9144,
        "Mile" to 1609.344
    )

    private var fromUnit = "Meter"
    private var toUnit = "Meter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        enableEdgeToEdge()

        val category = intent.getStringExtra("CATEGORY")
        println("clicked on $category.")

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
            spinner.adapter = makeAdapter(R.array.length_array_from)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    fromUnit = parent.getItemAtPosition(position).toString()
                    convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        findViewById<Spinner>(R.id.length_array_to).also { spinner ->
            spinner.adapter = makeAdapter(R.array.length_array_to)
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
        if (input == null) {
            output.setText("")
            return
        }
        val meters = input * (toMeters[fromUnit] ?: 1.0)
        val result = meters / (toMeters[toUnit] ?: 1.0)
        output.setText(if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString())
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.length_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.length)
    }

    private fun backButton() {
        val toolbar = findViewById<ImageButton>(R.id.back_button)
        toolbar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
