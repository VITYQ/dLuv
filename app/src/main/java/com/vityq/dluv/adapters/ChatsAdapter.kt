package com.vityq.dluv.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vityq.dluv.TelegramActivity
import com.vityq.dluv.databinding.ItemChatBinding
import com.vityq.dluv.databinding.ItemMessageBinding
import com.vityq.dluv.databinding.ItemMessageFromBinding
import com.vityq.dluv.model.MessageClass
import com.vityq.dluv.model.User
import com.vityq.dluv.user
import java.lang.IllegalArgumentException

class ChatsAdapter (val context: Context, val messages: List<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


    inner class Message(val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            Glide.with(context).load(messages[position].img_url).into(binding.imgProfile)
            binding.name.text = "${messages[position].name} ${messages[position].surname}"
            binding.constraint.setOnClickListener {
                val intent = Intent(context, TelegramActivity::class.java)
                intent.putExtra("username", messages[position].username)
                context.startActivity(intent)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return Message(binding)




    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as Message).bind(position)

    }

    override fun getItemCount() = messages.size



}