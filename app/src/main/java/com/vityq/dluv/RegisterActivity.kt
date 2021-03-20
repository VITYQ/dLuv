package com.vityq.dluv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.vityq.dluv.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        addRegisterButtonListener()



    }

    private fun addRegisterButtonListener() {
        binding.buttonRegister.setOnClickListener {
            val username = binding.textFieldLogin.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()
            if(!username.isNullOrEmpty() && !password.isNullOrEmpty()){
                var found = false
                val db = FirebaseDatabase.getInstance(url).getReference("/users")
                db.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            if (it.key == username){
                                Snackbar.make(binding.constraintRegister, "This nickname is already taken", Snackbar.LENGTH_SHORT).show()
                                found = true
                            }
                        }
                        if(!found){
                            user.username = username
                            user.password = password
                            db.child(username).setValue(user)
                            val sharedPref = getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE)
                            with(sharedPref.edit()){
                                putString("username", username)
                                commit()
                            }
                            val intent = Intent(this@RegisterActivity, AboutUserActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }



        }
    }
}