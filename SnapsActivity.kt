package com.example.elavi.snapchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapsActivity : AppCompatActivity() {
    val mauth= FirebaseAuth.getInstance()
    var snaplistview:ListView?=null
    var emailsreceived:ArrayList<String> = ArrayList()
    var snaps:ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        snaplistview=findViewById(R.id.snapslistview)
        var arrayadapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,emailsreceived)
        snaplistview?.adapter=arrayadapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mauth.currentUser?.uid).child("snaps").addChildEventListener(object :ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                emailsreceived?.add(p0?.child("from")?.value as String)
                snaps.add(p0!!)//imporatant that you should have added there the !!
                arrayadapter.notifyDataSetChanged()

            }

            override fun onCancelled(p0: DatabaseError?) {
            }
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
        snaplistview?.onItemClickListener=AdapterView.OnItemClickListener { adapterView, view, i, l ->
            var snapshot=snaps.get(i)
            var intent=Intent(this,ViewSnapActivity::class.java)
            intent.putExtra("imagename",snapshot.child("imagename").value as String)
            intent.putExtra("imageurl",snapshot.child("imageurl").value as String)
            intent.putExtra("message",snapshot.child("message").value as String)
            intent.putExtra("snapkey",snapshot.key)//it is basically the uuid
            startActivity(intent)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.snaps,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.createsnap)
        {
            val intent=Intent(this,CreateSnapActivity::class.java)
            startActivity(intent)
        }
        else if(item?.itemId==R.id.logout)
        {
            mauth.signOut()//sign out the user
            finish()//takes the user backs to the log in screen

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mauth.signOut()
        super.onBackPressed()
    }
}
