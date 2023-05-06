package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_bill.R
import com.example.kotlin_bill.adapters.CardAdapter
import com.example.kotlin_bill.models.CardModel
import com.google.firebase.database.*

class CardFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<CardModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<CardModel>()

        getCardData()


    }

    private fun getCardData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(CardModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = CardAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CardAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@CardFetchingActivity, CardDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("cardId", empList[position].cardId)
                            intent.putExtra("cardName", empList[position].cardName)
                            intent.putExtra("cardNumber", empList[position].cardNumber)
                            intent.putExtra("cardDate", empList[position].cardDate)
                            intent.putExtra("cardCvv", empList[position].cardCvv)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}