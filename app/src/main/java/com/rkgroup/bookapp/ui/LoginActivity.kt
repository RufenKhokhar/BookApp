package com.rkgroup.bookapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rkgroup.bookapp.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
                binding.etEmail.error = "Invalid Email!"
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password can't be empty!"
            } else {
                startLogin(email, password)
            }
        }
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.btnForget.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
                binding.etEmail.error = "Invalid Email!"
                Toast.makeText(this, "Type Email", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }


    }

    private fun resetPassword(email: String) {
        showProgress()
        Firebase.auth.sendPasswordResetEmail(email).addOnSuccessListener {
            hideProgress()
            Toast.makeText(this, "Please check you emails to Reset Password!", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            hideProgress()
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLogin(email: String, password: String) {
        showProgress()
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            hideProgress()
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }.addOnFailureListener {
            hideProgress()
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()

        }
    }


}