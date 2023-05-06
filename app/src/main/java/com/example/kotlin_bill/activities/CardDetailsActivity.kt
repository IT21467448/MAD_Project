package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlin_bill.R
import com.example.kotlin_bill.models.CardModel
import com.google.firebase.database.FirebaseDatabase

class CardDetailsActivity : AppCompatActivity() {

    private lateinit var tvCardId: TextView
    private lateinit var tvCardName: TextView
    private lateinit var tvCardNumber: TextView
    private lateinit var tvCardDate: TextView
    private lateinit var tvCardCvv: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cardId").toString(),
                intent.getStringExtra("cardName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cardId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Card data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, CardFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvCardId = findViewById(R.id.tvCardId)
        tvCardName = findViewById(R.id.tvCardName)
        tvCardNumber = findViewById(R.id.tvCardNumber)
        tvCardDate = findViewById(R.id.tvCardDate)
        tvCardCvv = findViewById(R.id.tvCardCvv)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvCardId.text = intent.getStringExtra("cardId")
        tvCardName.text = intent.getStringExtra("cardName")
        tvCardNumber.text = intent.getStringExtra("cardNumber")
        tvCardDate.text = intent.getStringExtra("cardDate")
        tvCardCvv.text = intent.getStringExtra("cardCvv")

    }

    private fun openUpdateDialog(
        cardId: String,
        cardName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.card_update_dialog, null)

        mDialog.setView(mDialogView)

        val etCardName = mDialogView.findViewById<EditText>(R.id.etCardName)
        val etCardNumber = mDialogView.findViewById<EditText>(R.id.etCardNumber)
        val etCardDate = mDialogView.findViewById<EditText>(R.id.etCardDate)
        val etCardCvv = mDialogView.findViewById<EditText>(R.id.etCardCvv)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etCardName.setText(intent.getStringExtra("cardName").toString())
        etCardNumber.setText(intent.getStringExtra("cardNumber").toString())
        etCardDate.setText(intent.getStringExtra("cardDate").toString())
        etCardCvv.setText(intent.getStringExtra("cardCvv").toString())

        mDialog.setTitle("Updating $cardName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCardData(
                cardId,
                etCardName.text.toString(),
                etCardNumber.text.toString(),
                etCardDate.text.toString(),
                etCardCvv.text.toString()
            )

            Toast.makeText(applicationContext, "Card Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvCardName.text = etCardName.text.toString()
            tvCardNumber.text = etCardNumber.text.toString()
            tvCardDate.text = etCardDate.text.toString()
            tvCardCvv.text = etCardCvv.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateCardData(
        id: String,
        name: String,
        age: String,
        salary: String,
        cvv: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val cardInfo = CardModel(id, name, age, salary, cvv)
        dbRef.setValue(cardInfo)
    }
}