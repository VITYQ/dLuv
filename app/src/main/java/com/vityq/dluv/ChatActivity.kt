package com.vityq.dluv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.vityq.dluv.adapters.ChatsAdapter
import com.vityq.dluv.databinding.ActivityChatBinding
import com.vityq.dluv.model.User

val chats = mutableListOf<User>()
class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var adapter: ChatsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)


        adapter = ChatsAdapter(this, chats)
        binding.recyclerChats.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        chats.clear()
        chatInit()
    }
    private fun chatInit() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}/messages")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val reg = FirebaseDatabase.getInstance(url).getReference("/users/${it.key}")
                    reg.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val name = snapshot.child("name").getValue().toString()
                            val surname = snapshot.child("surname").getValue().toString()
                            val username = snapshot.child("username").getValue().toString()
                            val img = snapshot.child("img_url").getValue().toString()
                            val us = User(name = name, surname = surname, img_url = img, username = username)
                            chats.add(us)
                            adapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}