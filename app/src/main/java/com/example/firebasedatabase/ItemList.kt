package com.example.firebasedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedatabase.databinding.ActivityMainBinding
import com.google.firebase.database.*

class ItemList : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var itemRecyclerview: RecyclerView
    private lateinit var itemArraylist:ArrayList<itemDs>
    private  val nodeList =ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        itemRecyclerview =findViewById(R.id.item_list)
        itemRecyclerview.layoutManager = LinearLayoutManager(this)
        itemRecyclerview.hasFixedSize()
        itemArraylist = arrayListOf<itemDs>()
        getItemData()
    }
    private fun getItemData(){
        db = FirebaseDatabase.getInstance().getReference("items")
        db.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(itmsnapshot in snapshot.children){
                        val item = itmsnapshot.getValue(itemDs::class.java)
                        itemArraylist.add(item!!)
                        nodeList.add(itmsnapshot.key.toString())
                    }
                    var adapter = itmAdapter(itemArraylist)
                    itemRecyclerview.adapter = adapter
                    adapter.setOnItemClickListner(object : itmAdapter.onItemClickListner{
                        override fun onItemClick(position: Int) {
                            val nodePath:String =nodeList[position]
                            val intent = Intent()
                            intent.putExtra("itm_id",nodePath)
                            setResult(2,intent)
                            finish()

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}