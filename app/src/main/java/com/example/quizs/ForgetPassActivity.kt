package com.example.quizs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ForgetPassActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backarror = findViewById<ImageView>(R.id.backarrorforgetpass)

        backarror.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        etPassword = findViewById(R.id.resetmail)
        btnResetPassword = findViewById(R.id.resetpass)

        auth = FirebaseAuth.getInstance()

        btnResetPassword.setOnClickListener {

            val sPassword = etPassword.text.toString()

            if (sPassword.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(sPassword)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Reset Link Sent, Please Check Your Email..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }
}