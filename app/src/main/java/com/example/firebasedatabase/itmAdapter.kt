package com.example.firebasedatabase

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class itmAdapter(private val itmlst:ArrayList<itemDs>):RecyclerView.Adapter<itmAdapter.itmHolder>() {
    private lateinit var mListner:onItemClickListner
    interface onItemClickListner{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listner: onItemClickListner){
        mListner = listner
    }
    class itmHolder(itmView: View, listner: onItemClickListner):RecyclerView.ViewHolder(itmView){
        val itmname:EditText =itmView.findViewById(R.id.edtname)
        val itmrate:EditText =itmView.findViewById(R.id.edtrate)
        val itmunit:EditText =itmView.findViewById(R.id.edtunit)
        val itmimg:ImageView =itmView.findViewById(R.id.itm_img)
        init{
            itmView.setOnClickListener{
                listner.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itmHolder {
        val itmView = LayoutInflater.from(parent.context).inflate(R.layout.item,
        parent,false)
        return  itmHolder(itmView, mListner)
    }

    override fun getItemCount(): Int {
        return itmlst.size
    }

    override fun onBindViewHolder(holder: itmHolder, position: Int) {
        val currentitem =itmlst[position]
        holder.itmname.setText(currentitem.itemName.toString())
        holder.itmrate.setText(currentitem.itemRate.toString())
        holder.itmunit.setText(currentitem.itemUnit.toString())
        val bytes = android.util.Base64.decode(currentitem.itemImg,
            android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.itmimg.setImageBitmap(bitmap)
    }
}