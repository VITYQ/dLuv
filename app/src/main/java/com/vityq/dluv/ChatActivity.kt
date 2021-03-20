package com.vityq.dluv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vityq.dluv.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        chatInit()


    }

    private fun chatInit() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}/messages")
    }
}