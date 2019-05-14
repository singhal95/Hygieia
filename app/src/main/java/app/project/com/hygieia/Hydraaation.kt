package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import java.util.*


@SuppressLint("ValidFragment")

class Hydraaation (context:Context): Fragment() {


    lateinit var context1:Context
    lateinit var BUTTON:ImageButton
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var firebasedatabase: FirebaseDatabase

    init {
    context1=context
}

    private lateinit var mWaterCountDisplay:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



    val view=inflater.inflate(R.layout.fragment_hydraaation, container, false)
        mWaterCountDisplay=view.findViewById<TextView>(R.id.tv_water_count)

        BUTTON=view.findViewById<ImageButton>(R.id.ib_water_increment)
        firebasedatabase= FirebaseDatabase.getInstance()
        database= context1.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()
        var count=database.getInt("waterintake",0)
        mWaterCountDisplay.setText((count).toString())


         BUTTON.setOnClickListener(){
            updateWaterCount()
        }

        return view
    }

    private fun updateWaterCount() {
        val progressDialog = ProgressDialog(context1)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Adding To Database ...")
        progressDialog.show()
        var count=database.getInt("waterintake",0)
        count=count+1
        mWaterCountDisplay.setText((count).toString())
        editor.putInt("waterintake",count)
        editor.commit()
        var myref=firebasedatabase.getReference("Users")
        val userid=database.getString("userid" ,"TEST")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val date=day.toString()+"-"+(month+1).toString()+"-"+year
        var watercount=Water(count.toString())
        myref.child(userid).child("waterintaker").child(date).setValue(count).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(context1,"One Water Glass is Added", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(context1,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }





    }
    /**
     * Adds one to the water count and shows a toast
     */



// Required empty public constructor
