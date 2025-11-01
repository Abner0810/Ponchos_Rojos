package com.example.ponchos_rojos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TagSpinnerAdapter(context: Context, private val items: List<String>) :
    ArrayAdapter<String>(context, 0, items) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // Spinner cuando está CERRADO.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "FILTER BY"
        return view
    }

    // Cómo se ve la lista DESPLEGABLE.
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        return view
    }
}
