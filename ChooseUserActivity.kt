package com.example.elavi.snapchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.*
import kotlinx.android.synthetic.main.activity_choose_user.*

class ChooseUserActivity : AppCompatActivity() {
    var chooselistview:ListView?=null
    val emailList = ArrayList<String>()
    val  keys= ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)
        chooselistview=findViewById(R.id.chooseuserlistview)
        var arrayadpater=ArrayAdapter(this,android.R.layout.simple_list_item_1,emailList)
        chooselistview?.adapter =arrayadpater
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object :ChildEventListener
        {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                //To change body of created functions use File | Settings | File Templates.
                val email=p0?.child("email")?.value as String
                emailList.add(email)
                keys.add(p0.key)

                arrayadpater.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
               //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
              //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                //To change body of created functions use File | Settings | File Templates.
            }

        })

        chooseuserlistview.setOnItemClickListener { apapterview,view, i, l ->
           val snapmap:Map<String,String> = mapOf("from" to FirebaseAuth.getInstance().currentUser?.email!!,
                   "imagename" to intent.getStringExtra("imagename"),"imageurl" to intent.getStringExtra("imageurl"),
                   "message" to intent.getStringExtra("message"))
            FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(i)).child("snaps").push().setValue(snapmap)
            val intent = Intent(this,SnapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
