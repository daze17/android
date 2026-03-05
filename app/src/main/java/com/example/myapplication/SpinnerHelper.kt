package com.example.myapplication

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setupSpinner(spinnerId: Int, arrayRes: Int) {
    fun makeAdapter(arrayRes: Int): ArrayAdapter<CharSequence> {
        val adapter = ArrayAdapter.createFromResource(this, arrayRes, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        return adapter
    }

    val listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val selected = parent.getItemAtPosition(position).toString()
            println("Selected: $selected")
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }


    findViewById<Spinner>(spinnerId).also {
        it.adapter = makeAdapter(arrayRes)
        it.onItemSelectedListener = listener
    }
}
