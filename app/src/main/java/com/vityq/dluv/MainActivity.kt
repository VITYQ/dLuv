package com.vityq.dluv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.vityq.dluv.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.vityq.dluv.adapters.CardStackAdapter
import com.vityq.dluv.model.User
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

public val url = "https://dluv-95376-default-rtdb.europe-west1.firebasedatabase.app/"
var user : User = User()
lateinit var manager: CardStackLayoutManager
lateinit var adapter: CardStackAdapter
val people = mutableListOf<User>()
class MainActivity : AppCompatActivity(), CardStackListener {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        checkUserLogin()




        adapter = CardStackAdapter(this, people)
        val cardStackView = findViewById<CardStackView>(R.id.card_stack_view)
        manager = CardStackLayoutManager(this, this)
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        manager.cardStackListener


        binding.chatsbutton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchPeople() {
        val ref = FirebaseDatabase.getInstance(url).getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.key != user.username){
                        var a = User()

                        Log.d("dsaga", it.toString())
                        val username = it.child("username").value.toString()
                        val name = it.child("name").value.toString()
                        val surname = it.child("surname").value.toString()
                        val age = it.child("age").value
                        val img_url = it.child("img_url").value.toString()
                        val height = it.child("height").value
                        val weight = it.child("weight").value
                        val about = it.child("about").value.toString()
                        val gender = it.child("gender").value.toString()

                        val data = User(username, "", img_url, name, surname, weight.toString(), about, age.toString(), gender, height.toString())
                        if (!people.contains(data)){
                            people.add(data)
                        }

                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCardSwiped(direction: Direction) {

        val position = manager.topPosition - 1
        Log.d("checkdrag", "d: $direction, top: ${people[position]}")
        var likes = 0
        var dislikes = 0
        val ref = FirebaseDatabase.getInstance(url).getReference("/users/${people[position].username}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val l = snapshot.child("likes").getValue()
                val d = snapshot.child("dislikes").getValue()
                if(l == null || l == ""){
                    likes = 0
                }
                else{
                    likes = l.toString().toInt()
                }
                if (d == null || d == ""){
                    dislikes = 0
                }
                else{
                    dislikes = d.toString().toInt()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        if (direction.name == "Right"){
            likes++
            ref.child("likes").setValue(likes)
        }
        else{
            dislikes++
            ref.child("dislikes").setValue(dislikes)
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardRewound() {

    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d("checkdrag", "p: $position, user: ${people[position]}")
        if (position+1 == people.size){

        }
    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    private fun checkUserLogin() {
        val sharedpref = getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE)
        val username = sharedpref.getString("username", "")
        if(!username.isNullOrEmpty()){
            fetchUser(username)

        }
        else{
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun fetchUser(username: String) {
        val db = FirebaseDatabase.getInstance(url).getReference("/users/$username")
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").value.toString()
                val name = snapshot.child("name").value.toString()
                val surname = snapshot.child("surname").value.toString()
                val age = snapshot.child("age").value.toString()
                val img_url = snapshot.child("img_url").value.toString()
                val height = snapshot.child("height").value.toString()
                val weight = snapshot.child("weight").value.toString()
                val about = snapshot.child("about").value.toString()
                val gender = snapshot.child("gender").value.toString()
                if(snapshot != null){
                    user = User(username, "", img_url, name, surname, weight, about, age, gender, height)
                    fetchPeople()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}