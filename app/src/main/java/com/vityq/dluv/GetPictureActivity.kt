package com.vityq.dluv

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vityq.dluv.databinding.ActivityGetPictureBinding
import com.vityq.dluv.databinding.CardStackViewBinding
import java.io.File
import java.io.FileNotFoundException

class GetPictureActivity : AppCompatActivity() {
    lateinit var binding: ActivityGetPictureBinding
    var chosen = false
    var imgUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_picture)


        addListenerForPictureButton()
    }

    private fun addListenerForPictureButton() {
        binding.buttonPicture.setOnClickListener {
            if(!chosen){
                val intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(intent, 1)
            }
            else{
                uploadPicture()
            }
        }
        binding.imageViewProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, 1)
        }
    }

    private fun uploadPicture() {
        val storage = Firebase.storage("gs://dluv-95376.appspot.com")
        val ref = storage.reference.child("images").child(user.username)
        val request = ref.putFile(imgUri!!)

        val uriTask = request.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val refDB = FirebaseDatabase.getInstance(url).getReference("/users/${user.username}")
                refDB.child("img_url").setValue(downloadUri.toString())
                Log.d("dsadaf", "url ${downloadUri}")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                // Handle failures
                // ...
            }
        }
//        request.addOnSuccessListener {
//val refDB = FirebaseDatabase.getInstance().getReference("/users/ф")
//                refDB.child("img_url").setValue(uri.toString())
//                Log.d("dsadaf", "url ${uri}")
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            try{
                imgUri = data?.data
                val inputStream = contentResolver.openInputStream(imgUri!!)
                val selectedImage = BitmapFactory.decodeStream(inputStream)
                binding.imageViewProfile.setImageBitmap(selectedImage)
                binding.textViewTapToChange.visibility = View.VISIBLE
                binding.buttonPicture.text = "Continue"
                chosen = true
            }
            catch (e: FileNotFoundException) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

}