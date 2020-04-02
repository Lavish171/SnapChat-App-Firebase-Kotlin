package com.example.elavi.snapchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var emailedittext:EditText?=null
    var passwordedittext:EditText?=null
    val mauth=FirebaseAuth.getInstance()
    var gobutton:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailedittext=findViewById(R.id.emailedittext)
        passwordedittext=findViewById(R.id.passwordeidttext)
        gobutton=findViewById(R.id.gobutton)

        if(mauth.currentUser!=null)
        {
            login()
        }
    }

    fun goclicked(view : View)
    {
     //check if we can login the user
        mauth.signInWithEmailAndPassword(emailedittext?.text.toString(), passwordedittext?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                      login()
                    } else {
                        //if uper one doesn't works then check if we ca sign in the user
                        mauth.createUserWithEmailAndPassword(emailedittext?.text.toString(), passwordedittext?.text.toString())
                                .addOnCompleteListener(this) { task ->
                                    if(task.isSuccessful) {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid).child("email").setValue(emailedittext?.text.toString())

                                        //upto the get reference which is the starting point of the data base
                                        //and the .child() after that is exact same as the crating or pressing the + button
                                        login()
                                    }
                                    else
                                    {
                                       Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                    // ...
                }
    }
    fun login()
    {
        Toast.makeText(applicationContext,"The new Screen",Toast.LENGTH_SHORT).show()
      val intent= Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}
