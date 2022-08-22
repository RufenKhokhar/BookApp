package com.rkgroup.bookapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rkgroup.bookapp.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password1 = binding.etPassword1.text.toString()
            val password2 = binding.etPassword2.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
                binding.etEmail.error = "Invalid Email!"
            } else if (password1.isEmpty()) {
                binding.etPassword1.error = "Password can't be empty!"
            } else if (password2.isEmpty()) {
                binding.etPassword2.error = "Password can't be empty!"
            } else if (password1 != password2) {
                binding.etPassword1.error = "Passwords not match"
                binding.etPassword2.error = "Passwords not match"
            } else {
                startSignUp(email, password1)
            }
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun startSignUp(email: String, password: String) {
        showProgress()
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            hideProgress()
            startActivity(Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }.addOnFailureListener {
            hideProgress()
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}