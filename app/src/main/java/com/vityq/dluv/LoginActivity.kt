package com.vityq.dluv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.vityq.dluv.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        addListenerForRegisterButton()
        addListenerForLoginButton()

    }

    private fun addListenerForRegisterButton() {
            binding.buttonRegister.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

    }

    private fun addListenerForLoginButton() {

    }
}