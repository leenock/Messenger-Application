package com.example.adventuremessenger

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.adventuremessenger.NewMessageActivity.Companion.USER_KEY
import com.example.adventuremessenger.models.chatMessage
import com.example.adventuremessenger.views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class LatestMessagesActivity : AppCompatActivity() {


        companion object{

        var currentUser: User? = null
            var TAG = "LATESTMESSAGES"

            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        recycle_view_latest_message.adapter = adapter
        recycle_view_latest_message.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->

            Log.d(TAG, "123")
            var intent = Intent(this, ChatLogActivity::class.java)

            //chatpartner

            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser )
            startActivity(intent)
        }

      //  setUpDummyrows()
        listenForLatestMessages()

        fetchCurrentUser()

        verifyUserIsLoggedIn()
    }

    val latestmessagesMap = HashMap<String, chatMessage>()

    private fun refreshrecycleViewMessages(){
        adapter.clear()
        latestmessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
        }
    private fun listenForLatestMessages(){

        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(chatMessage::class.java)?: return
                latestmessagesMap[p0.key!!] = chatMessage
                refreshrecycleViewMessages()

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(chatMessage::class.java)?: return
                latestmessagesMap[p0.key!!] = chatMessage
                refreshrecycleViewMessages()

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    val adapter = GroupAdapter<ViewHolder>()

    /*
    private fun setUpDummyrows(){
    adapter.add(LatestMessageRow())
    adapter.add(LatestMessageRow())
    adapter.add(LatestMessageRow())
    }
    */

    private fun fetchCurrentUser(){
  val uid = FirebaseAuth.getInstance().uid
  val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessage", "Current user ${currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    private fun verifyUserIsLoggedIn() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {

            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
         R.id.menu_new_message ->{
          val intent = Intent(this, NewMessageActivity::class.java)
             startActivity(intent)

         }
        R.id.menu_sign_out ->{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}