package com.example.ponchos_rojos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityPagarBinding

class PagarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPagarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pagos = resources.getStringArray(R.array.pagos)
        val meses = resources.getStringArray(R.array.meses)
        val anios = resources.getStringArray(R.array.anios)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pagos)
        val adapter_mes = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, meses
        )
        val adapter_anio = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, anios
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter_anio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.menuPago.adapter = adapter
        binding.fechaMes.adapter = adapter_mes
        binding.fechaAnio.adapter = adapter_anio
        //escogerpago
        binding.menuPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val seleccionado = pagos[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.fechaMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val seleccionado_mes = meses[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.fechaAnio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val seleccionado_año = anios[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.flecha.setOnClickListener {
            finish()
        }

        binding.btnPagar.setOnClickListener {
            if (!areAllFieldsFilled()) {
                return@setOnClickListener
            }
            Toast.makeText(
                this@PagarActivity, "Pago realizado correctamente", Toast.LENGTH_SHORT
            ).show()
            finish()

        }
        binding.avatarBtn.setOnClickListener {
            startActivity(Intent(this@PagarActivity, MainPerfilActivity::class.java))
        }


    }

    private fun areAllFieldsFilled(): Boolean {
        val fields = listOf<EditText>(
            binding.rellenarTarjeta,
            binding.codigoSegRellenar,
            binding.rellenarNombre,
            binding.rellenarApellido,
            binding.rellenarDireccion,
            binding.rellenarCiudad,
            binding.rellenarCodigoPostal,
            binding.rellenarPais,
            binding.rellenarCelular
        )

        for (field in fields) {
            val text = field.text.toString().trim()
            if (text.isEmpty()) {
                field.requestFocus()
                Toast.makeText(this@PagarActivity, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

}
