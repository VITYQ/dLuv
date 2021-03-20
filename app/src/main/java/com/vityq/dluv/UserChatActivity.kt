package com.vityq.dluv

import android.R
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vityq.dluv.databinding.ActivityMessagingBinding
import com.vityq.dluv.databinding.ItemMessageBinding
import com.vityq.dluv.model.MessageClass
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem



class UserChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityMessagingBinding
    var username = ""
    var dialogWith = ""
    lateinit var adapter : GroupAdapter<GroupieViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_messaging)
        dialogWith = intent.getStringExtra("username")!!
        adapter = GroupAdapter<GroupieViewHolder>()
//        binding.recyclerMessages.adapter = adapter


        fetchChat()

    }

    private fun fetchChat() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}/messages/${dialogWith}")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue()
                val message = snapshot.child("message").getValue()
//                adapter += Message(username, message)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }





}