package com.vityq.dluv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vityq.dluv.adapters.MessagesRecyclerAdapter
import com.vityq.dluv.databinding.ActivityTelegramBinding
import com.vityq.dluv.model.MessageClass
import kotlin.math.max

val messages = mutableListOf<MessageClass>()
class TelegramActivity : AppCompatActivity() {
    lateinit var binding : ActivityTelegramBinding
    lateinit var adapter : MessagesRecyclerAdapter
    var username = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_telegram)


        username = intent.getStringExtra("username")!!
        adapter = MessagesRecyclerAdapter(this, messages)
        binding.recyclerMessages.adapter = adapter
        fetchImg()
        sendButton()
    }

    private fun fetchImg() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/$username")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val img = snapshot.child("img_url").getValue()
                val name = snapshot.child("name").getValue().toString()
                val surname = snapshot.child("surname").getValue().toString()
                binding.nametext.text = "$name $surname"
                Glide.with(this@TelegramActivity).load(img).into(binding.circleImage)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onStart() {
        super.onStart()
        messages.clear()
        fetchMessages()
    }

    private fun sendButton() {
        binding.buttonSend.setOnClickListener {
            Log.d("dsgfdsg", "clodgs")
            val messageText = binding.textFieldMessage.editText?.text.toString()
            if (!messageText.isNullOrEmpty()){
                binding.textFieldMessage.editText?.setText("")
                val refSender = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}/messages/${username}")
                val refReciever = FirebaseDatabase.getInstance(url).getReference("/users/${username}/messages/${user.username}")
                refSender.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot == null){
                            refSender.child("0").setValue(MessageClass(user.username, messageText))
                            refReciever.child("0").setValue(MessageClass(user.username, messageText))
                        }
                        else{
                            var maxNum = 0
                            snapshot.children.forEach {
                                val tmp = it.key.toString().toInt()
                                if (tmp>maxNum){
                                    maxNum = tmp
                                }
                            }
                            maxNum++
                            refSender.child("$maxNum").setValue(MessageClass(user.username, messageText))
                            refReciever.child("$maxNum").setValue(MessageClass(user.username, messageText))

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

        }
    }

    private fun fetchMessages() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}/messages/${username}")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val username = it.child("username").getValue()
                    val message = it.child("message").getValue()
                    if(username!= null && message != null){
                        val msg = MessageClass(username.toString(), message.toString())
                        Log.d("dgsadg", "${msg.toString()} ${user.username}")
                        if (!messages.contains(msg)){
                            messages.add(msg)
                        }

                    }

                }
                adapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}