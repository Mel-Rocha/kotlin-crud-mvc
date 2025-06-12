package com.example.adscar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewCarActivity : AppCompatActivity() {
    private lateinit var editTextBrand: EditText
    private lateinit var editTextModel: EditText
    private lateinit var editTextYear: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonSaveCar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_car)

        editTextBrand = findViewById(R.id.editTextBrand)
        editTextModel = findViewById(R.id.editTextModel)
        editTextYear = findViewById(R.id.editTextYear)
        editTextPrice = findViewById(R.id.editTextPrice)
        buttonSaveCar = findViewById(R.id.buttonSaveCar)

        buttonSaveCar.setOnClickListener {
            val brand = editTextBrand.text.toString()
            val model = editTextModel.text.toString()
            val year = editTextYear.text.toString().toIntOrNull() ?: 0
            val price = editTextPrice.text.toString().toDoubleOrNull() ?: 0.0

            val newCar = Car(
                id = 0,
                brand = brand,
                model = model,
                year = year,
                price = price
            )

            val carDAO = CarDAO(this)
            carDAO.insertCar(newCar)

            val resultIntent = Intent()
            resultIntent.putExtra("newCar", newCar)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

