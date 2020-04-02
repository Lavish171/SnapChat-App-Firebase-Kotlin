package com.example.elavi.snapchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import com.google.firebase.storage.UploadTask

class CreateSnapActivity : AppCompatActivity() {

    var createsnapimageview:ImageView?=null
    var messageedittext:EditText?=null
    var chooseimagebutton:Button?=null
    var nextbutton:Button?=null
    val imagename=UUID.randomUUID().toString()+".jpg"//for the unique image name

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        createsnapimageview=findViewById(R.id.createSnapimageView)
        messageedittext=findViewById(R.id.messageEditText);
        chooseimagebutton=findViewById(R.id.chooseImageButton)
        nextbutton=findViewById(R.id.nextbutton)
        
    }

    fun chooseimageclicked(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
               createsnapimageview?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }
//FirebaseStorage.getInstance().getReference().child("images")
   fun nextbuttonclicked(view:View)
   {
       // Get the data from an ImageView as bytes
       createsnapimageview?.setDrawingCacheEnabled(true)
       createsnapimageview?.buildDrawingCache()
       val bitmap = ( createsnapimageview?.getDrawable() as BitmapDrawable).bitmap
       val baos = ByteArrayOutputStream()
       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
       val data = baos.toByteArray()

       val uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imagename).putBytes(data)
       uploadTask.addOnFailureListener(OnFailureListener {
           // Handle unsuccessful uploads
           Toast.makeText(applicationContext,"Upload Failed",Toast.LENGTH_SHORT).show()
       }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {taskSnapshot ->
           // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
           // ...
           val downloadurl=taskSnapshot.downloadUrl
           Log.i("URL",downloadurl.toString())
           val intent=Intent(this,ChooseUserActivity::class.java)
           intent.putExtra("imageurl",downloadurl.toString())
           intent.putExtra("imagename",imagename)
           intent.putExtra("message",messageedittext?.text.toString())

           startActivity(intent)
       })
   }

}
