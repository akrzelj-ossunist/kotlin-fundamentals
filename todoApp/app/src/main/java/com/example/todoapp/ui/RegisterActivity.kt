// RegisterActivity.kt
package com.example.todoapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = User(username = username, password = password)
                GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(applicationContext).userDao().registerUser(user)
                    runOnUiThread {
                        Toast.makeText(applicationContext, "User registered", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
