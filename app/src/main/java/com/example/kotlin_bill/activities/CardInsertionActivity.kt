package com.example.kotlin_bill.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlin_bill.models.CardModel
import com.example.kotlin_bill.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CardInsertionActivity : AppCompatActivity() {
    //initializing variables

    private lateinit var etCardName: EditText
    private lateinit var etCardNumber: EditText
    private lateinit var etCardDate: EditText
    private lateinit var etCardCvv: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_insertion)

        etCardName = findViewById(R.id.etCardName)
        etCardNumber = findViewById(R.id.etCardNumber)
        etCardDate = findViewById(R.id.etCardDate)
        etCardCvv = findViewById(R.id.etCardCvv)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB")

        btnSaveData.setOnClickListener {
            saveCardData()
        }

    }

    private fun saveCardData() {

        //Geting Values
        val cardName = etCardName.text.toString()
        val cardNumber = etCardNumber.text.toString()
        val cardDate = etCardDate.text.toString()
        val cardCvv = etCardCvv.text.toString()

        //validation
        if(cardName.isEmpty() || cardNumber.isEmpty() || cardDate.isEmpty() || cardCvv.isEmpty()){
        if (cardName.isEmpty()) {
            etCardName.error = "Please enter card Name"
        }
        if (cardNumber.isEmpty()) {
            etCardNumber.error = "Please enter card Number"
        }
        if (cardDate.isEmpty()) {
            etCardDate.error = "Please enter card Date"
        }
        if (cardCvv.isEmpty()) {
            etCardDate.error = "Please enter cvv"
        }
            Toast.makeText(this, "Some areas are not filled", Toast.LENGTH_LONG).show()
        }
        else{
        //genrate unique ID
        val cardId = dbRef.push().key!!

        val card = CardModel(cardId, cardName, cardNumber, cardDate, cardCvv)

        dbRef.child(cardId).setValue(card)
            .addOnCompleteListener {
                Toast.makeText(this,"data insert successfully",Toast.LENGTH_SHORT).show()

                //clear data after insert
                etCardName.text.clear()
                etCardNumber.text.clear()
                etCardDate.text.clear()
                etCardCvv.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }

}}