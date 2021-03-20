package com.vityq.dluv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vityq.dluv.databinding.ActivityAboutUserBinding
import com.vityq.dluv.databinding.ActivityUserBinding
import com.vityq.dluv.model.User

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    var username = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)

        username = intent.getStringExtra("username")!!

        fetchUser()
        addChatButtonListener()
    }

    private fun addChatButtonListener() {
        binding.buttonChat.setOnClickListener {
            val intent = Intent(this, TelegramActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/$username")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var a = User()

                Log.d("dsaga", snapshot.toString())
                val username = snapshot.child("username").value.toString()
                val name = snapshot.child("name").value.toString()
                val surname = snapshot.child("surname").value.toString()
                val age = snapshot.child("age").value
                val img_url = snapshot.child("img_url").value.toString()
                val height = snapshot.child("height").value
                val weight = snapshot.child("weight").value
                val about = snapshot.child("about").value.toString()
                val gender = snapshot.child("gender").value.toString()
                var likes = snapshot.child("likes").value.toString()
                var dislikes = snapshot.child("dislikes").value.toString()
                binding.textContent.text = about
                binding.textLikes.text = "$likes likes, $dislikes dislikes"
                binding.textName.text = "$name $surname, $age"
                Glide.with(this@UserActivity).load(img_url).into(binding.imageViewProfile)
                val data = User(username, "", img_url, name, surname, weight.toString(), about, age.toString(), gender, height.toString(), likes, dislikes)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}