package com.example.adscar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewUserActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextCpf: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextBirthDate: EditText
    private lateinit var checkBoxAdminUser: CheckBox
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextCpf = findViewById(R.id.editTextCpf)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextBirthDate = findViewById(R.id.editTextBirthDate)
        checkBoxAdminUser = findViewById(R.id.checkBoxAdminUser)
        buttonSave = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val cpf = editTextCpf.text.toString()
            val password = editTextPassword.text.toString()
            val birthDate = editTextBirthDate.text.toString()
            val accessLevel = if (checkBoxAdminUser.isChecked) 1 else 0

            val newUser = User(0, name, email, cpf, password, birthDate, accessLevel)

            val dbHelper = DatabaseHelper(this, "myAPP.db", null, 1)
            UserDAO(this).insertUser(newUser)

            val resultIntent = Intent()
            resultIntent.putExtra("newUser", newUser)
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Finaliza e retorna para a tela anterior
        }
    }
}

