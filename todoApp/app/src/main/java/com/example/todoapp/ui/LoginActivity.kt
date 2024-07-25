// LoginActivity.kt
package com.example.todoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val user = AppDatabase.getDatabase(applicationContext).userDao().loginUser(username, password)
                    runOnUiThread {
                        if (user != null) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("userId", user.userId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
