package com.example.adscar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CarAdapter(
    private val context: Context,
    private var cars: MutableList<Car>
) : BaseAdapter() {

    override fun getCount(): Int = cars.size

    override fun getItem(position: Int): Any = cars[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_car, parent, false)

        val car = cars[position]

        val textViewBrand = view.findViewById<TextView>(R.id.textViewBrand)
        val textViewModel = view.findViewById<TextView>(R.id.textViewModel)
        val textViewYear = view.findViewById<TextView>(R.id.textViewYear)
        val textViewPrice = view.findViewById<TextView>(R.id.textViewPrice)

        textViewBrand.text = car.brand
        textViewModel.text = car.model
        textViewYear.text = car.year.toString()
        textViewPrice.text = "R$ %.2f".format(car.price)

        return view
    }

    fun updateCars(newCars: List<Car>) {
        cars.clear()
        cars.addAll(newCars)
        notifyDataSetChanged()
    }
}
