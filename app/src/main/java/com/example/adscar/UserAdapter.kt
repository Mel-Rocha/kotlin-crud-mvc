package com.example.adscar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserAdapter(
    private val context: Context,
    private var users: MutableList<User>
) : BaseAdapter() {

    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)

        val user = users[position]

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewEmail = view.findViewById<TextView>(R.id.textViewEmail)
        val textViewCpf = view.findViewById<TextView>(R.id.textViewCpf)
        val textViewBirthDate = view.findViewById<TextView>(R.id.textViewBirthDate)
        val textViewAccessLevel = view.findViewById<TextView>(R.id.textViewAccessLevel)

        textViewName.text = user.name
        textViewEmail.text = user.email
        textViewCpf.text = user.cpf
        textViewBirthDate.text = user.birthDate
        textViewAccessLevel.text = if (user.accessLevel == 1) "Admin" else "User"

        return view
    }

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}
