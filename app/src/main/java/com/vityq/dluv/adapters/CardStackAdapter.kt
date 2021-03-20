package com.vityq.dluv.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.provider.Contacts
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vityq.dluv.R
import com.vityq.dluv.UserActivity
import com.vityq.dluv.databinding.CardStackViewBinding
import com.vityq.dluv.model.User
import java.lang.IllegalArgumentException



class CardStackAdapter(val context: Context, val people: List<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


    inner class People(val binding: CardStackViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(context).load(people[position].img_url).into(binding.imageViewProfile)
            binding.textViewNameAge.text = "${people[position].name}, ${people[position].age} years old"
            binding.textViewUnder.text = "I'm ${people[position].gender}, ${people[position].weight}kg, ${people[position].height}cm"

            binding.imageViewProfile.setOnClickListener {
                val intent = Intent(context, UserActivity::class.java)
                intent.putExtra("username", people[position].username)
                context.startActivity(intent)
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStackViewBinding.inflate(inflater, parent, false)
        return People(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

       (holder as People).bind(position)

    }

    override fun getItemCount() = people.size

}