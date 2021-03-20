package com.vityq.dluv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vityq.dluv.databinding.ActivityAboutUserBinding

class AboutUserActivity : AppCompatActivity() {
    lateinit var binding : ActivityAboutUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_user)

        addListenerForContinue()


    }

    private fun addListenerForContinue() {
        binding.buttonContinue.setOnClickListener {
            val name = binding.textFieldName.editText?.text?.toString()
            val surname = binding.textFieldSurname.editText?.text?.toString()
            val height = binding.textFieldHeight.editText?.text?.toString()
            val age = binding.textFieldAge.editText?.text?.toString()
            val weight = binding.textFieldWeight.editText?.text.toString()
            val gender = binding.textFieldGender.editText?.text?.toString()
            val about = binding.textFieldAboutMe.editText?.text?.toString()

            if (name.isNullOrEmpty() || surname.isNullOrEmpty()
                || weight.isNullOrEmpty() || age.isNullOrEmpty()
                || height.isNullOrEmpty() || gender.isNullOrEmpty()
                || about.isNullOrEmpty()){
                Toast.makeText(this, "Enter the missing data", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, GetPictureActivity::class.java)
                user.name = name
                user.surname = surname
                user.height = height
                user.age = age
                user.weight = weight
                user.gender = gender
                user.about = about
                val db = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}")
                db.setValue(user)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }
    }
}