package app.project.com.hygieia

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_weight_tracker.*
import java.util.*


@SuppressLint("ValidFragment")
class WeightTracker (context: Context): Fragment() {


    private lateinit var helo:String
    private lateinit var database: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var weight:EditText
    private lateinit var date1:TextView
    private lateinit var Submit:Button
    lateinit var mcontext:Context
    private lateinit var firebasedatabase: FirebaseDatabase
    private var monthofyear:Int = 0


    init {

        mcontext=context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       var view=inflater.inflate(R.layout.fragment_weight_tracker, container, false)
        firebasedatabase= FirebaseDatabase.getInstance()
        weight=view.findViewById<EditText>(R.id.weight)
        date1=view.findViewById<TextView>(R.id.date)
        Submit=view.findViewById<Button>(R.id.submit)
        database= mcontext.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = database.edit()

        date1.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                date.setText(dayOfMonth.toString()+"/"+monthOfYear.toString()+"/"+year)

                monthofyear=monthOfYear+1
            }, year, month, day)

            dpd.show()
        }
        Submit.setOnClickListener(){
            val progressDialog = ProgressDialog(mcontext)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Adding To Database ...")
            progressDialog.show()

            var weightstring=weight.text.toString()
            val userid=database.getString("userid" ,"TEST")
            var myRef = firebasedatabase.getReference("Users")
            var weighttrack=Weight(weightstring)
            var datestring=date.text.toString()
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val date=day.toString()+"-"+(month+1).toString()+"-"+year
            myRef.child(userid).child("weighttracker").child(date).setValue(weightstring).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(mcontext,"Weight is Added",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(mcontext,"Please Chcek Your Internet Connection", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }





}
