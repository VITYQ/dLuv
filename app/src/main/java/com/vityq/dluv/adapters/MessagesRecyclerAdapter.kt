package com.vityq.dluv.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vityq.dluv.databinding.ItemMessageBinding
import com.vityq.dluv.databinding.ItemMessageFromBinding
import com.vityq.dluv.model.MessageClass
import com.vityq.dluv.user
import java.lang.IllegalArgumentException

class MessagesRecyclerAdapter(val context: Context, val messages: List<MessageClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


    inner class Message(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.textMessage.text = messages[position].message
        }
    }

    inner class MessageFrom(val binding: ItemMessageFromBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.textMessage.text = messages[position].message
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            2 -> {
                val binding = ItemMessageBinding.inflate(inflater, parent, false)
                Message(binding)
            }
            1 -> {
                val binding = ItemMessageFromBinding.inflate(inflater, parent, false)
                MessageFrom(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (user.username != messages[position].username){
            (holder as Message).bind(position)
        }
        else{
            (holder as MessageFrom).bind(position)
        }

    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {

        if (user.username.equals(messages[position].username)){
            Log.d("sfdsaf", "same ${user.username}  ${messages[position].username} ")
            return 1
        }
        Log.d("sfdsaf", "not same ${user.username}  ${messages[position].username} ")
        return 2

    }


}